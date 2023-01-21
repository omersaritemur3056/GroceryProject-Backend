package com.example.grocery.core.security.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.grocery.business.constants.Messages.CreateMessages;
import com.example.grocery.business.constants.Messages.DeleteMessages;
import com.example.grocery.business.constants.Messages.ErrorMessages;
import com.example.grocery.business.constants.Messages.GetByIdMessages;
import com.example.grocery.business.constants.Messages.GetListMessages;
import com.example.grocery.core.security.DTOs.request.TokenRefreshRequest;
import com.example.grocery.core.security.DTOs.request.UserForLoginDto;
import com.example.grocery.core.security.DTOs.request.UserForRegisterDto;
import com.example.grocery.core.security.DTOs.response.GetAllUserResponseDto;
import com.example.grocery.core.security.DTOs.response.GetByIdUserResponseDto;
import com.example.grocery.core.security.DTOs.response.JwtResponse;
import com.example.grocery.core.security.DTOs.response.TokenRefreshResponse;
import com.example.grocery.core.security.enums.Authority;
import com.example.grocery.core.security.jwt.JwtUtils;
import com.example.grocery.core.security.models.RefreshToken;
import com.example.grocery.core.security.models.Role;
import com.example.grocery.core.security.models.User;
import com.example.grocery.core.security.repository.RoleRepository;
import com.example.grocery.core.security.repository.UserRepository;
import com.example.grocery.core.utilities.business.BusinessRules;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.exceptions.TokenRefreshException;
import com.example.grocery.core.utilities.mapper.MapperService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.core.utilities.results.SuccessDataResult;
import com.example.grocery.core.utilities.results.SuccessResult;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserManager implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private MapperService mapperService;

    @Override
    public Result register(UserForRegisterDto userForRegisterDto) {
        Result rules = BusinessRules.run(isEmailExist(userForRegisterDto.getEmail()),
                isUsernameExist(userForRegisterDto.getUsername()),
                isValidPassword(userForRegisterDto.getPassword(), userForRegisterDto.getUsername()));

        User user = mapperService.getModelMapper().map(userForRegisterDto, User.class);
        user.setPassword(passwordEncoder.encode(userForRegisterDto.getPassword()));

        Set<String> strRoles = userForRegisterDto.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(Authority.USER)
                    .orElseThrow(() -> new BusinessException(ErrorMessages.ROLE_NOT_FOUND));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "ADMIN":
                        Role adminRole = roleRepository.findByName(Authority.ADMIN)
                                .orElseThrow(() -> new BusinessException(ErrorMessages.ROLE_NOT_FOUND));
                        roles.add(adminRole);

                        break;
                    case "MODERATOR":
                        Role moderatorRole = roleRepository.findByName(Authority.MODERATOR)
                                .orElseThrow(() -> new BusinessException(ErrorMessages.ROLE_NOT_FOUND));
                        roles.add(moderatorRole);

                        break;
                    case "EDITOR":
                        Role editorRole = roleRepository.findByName(Authority.EDITOR)
                                .orElseThrow(() -> new BusinessException(ErrorMessages.ROLE_NOT_FOUND));
                        roles.add(editorRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(Authority.USER)
                                .orElseThrow(() -> new BusinessException(ErrorMessages.ROLE_NOT_FOUND));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);
        log.info("User: {} saved in DB!", user.getUsername());
        return new SuccessResult(CreateMessages.USER_CREATED);
    }

    @Override
    public DataResult<JwtResponse> login(UserForLoginDto userForLoginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userForLoginDto.getUsername(), userForLoginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        log.info("User: {} logined", userForLoginDto.getUsername());
        return new SuccessDataResult<>(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
                userDetails.getUsername(), userDetails.getEmail(), roles));
    }

    // @Override
    // public Result signout() {
    // UserDetailsImpl userDetails = (UserDetailsImpl)
    // SecurityContextHolder.getContext().getAuthentication()
    // .getPrincipal();
    // Long userId = userDetails.getId();
    // refreshTokenService.deleteByUserId(userId);
    // log.info("User: {} signout", userDetails.getUsername());
    // return new SuccessResult(DeleteMessages.SIGN_OUT);
    // }

    @Override
    public DataResult<TokenRefreshResponse> refreshtoken(TokenRefreshRequest tokenRefreshRequest) {
        String requestRefreshToken = tokenRefreshRequest.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return new SuccessDataResult<>(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(
                        () -> new TokenRefreshException(requestRefreshToken, ErrorMessages.REFRESH_TOKEN_NOT_FOUND));
    }

    @Override
    public DataResult<List<GetAllUserResponseDto>> getAll() {
        List<User> userList = userRepository.findAll();
        List<GetAllUserResponseDto> returnList = userList.stream()
                .map(u -> mapperService.getModelMapper().map(u, GetAllUserResponseDto.class)).toList();

        return new SuccessDataResult<>(returnList, GetListMessages.USERS_LISTED);
    }

    @Override
    public DataResult<GetByIdUserResponseDto> getById(Long id) {
        User inDbUser = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
        GetByIdUserResponseDto returnObj = mapperService.getModelMapper().map(inDbUser, GetByIdUserResponseDto.class);
        returnObj.setRoles(inDbUser.getRoles());
        return new SuccessDataResult<>(returnObj, GetByIdMessages.USER_LISTED);
    }

    // Bağımlılığı kontrol altına almak üzere tasarlandılar
    @Override
    public User getUserById(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorMessages.USER_ID_NOT_FOUND));
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorMessages.USER_EMAIL_NOT_FOUND));
    }

    @Override
    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existById(Long id) {
        return userRepository.existsById(id);
    }

    private Result isUsernameExist(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new BusinessException(ErrorMessages.USERNAME_EXIST);
        }
        return new SuccessResult();
    }

    private Result isEmailExist(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(ErrorMessages.EMAIL_REPEATED);
        }
        return new SuccessResult();
    }

    private Result isValidPassword(String password, String username) {
        if (password.contains(username)) {
            throw new BusinessException(ErrorMessages.PASSWORD_NOT_VALID);
        }
        return new SuccessResult();
    }

}
