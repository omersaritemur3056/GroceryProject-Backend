package com.example.grocery.core.security.services;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.grocery.service.constants.Messages.CreateMessages;
import com.example.grocery.service.constants.Messages.DeleteMessages;
import com.example.grocery.service.constants.Messages.ErrorMessages;
import com.example.grocery.service.constants.Messages.GetByIdMessages;
import com.example.grocery.service.constants.Messages.GetListMessages;
import com.example.grocery.service.constants.Messages.UpdateMessages;
import com.example.grocery.service.constants.Messages.LogMessages.LogInfoMessages;
import com.example.grocery.core.security.DTOs.request.TokenRefreshRequest;
import com.example.grocery.core.security.DTOs.request.UpdateUserRequestDto;
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

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final MapperService mapperService;
    private final UserBusinessRules userBusinessRules;

    @Override
    @Transactional
    public Result register(UserForRegisterDto userForRegisterDto) {
        Result rules = BusinessRules.run(userBusinessRules.isEmailExist(userForRegisterDto.getEmail()),
                userBusinessRules.isUsernameExist(userForRegisterDto.getUsername()),
                userBusinessRules.isValidPassword(userForRegisterDto.getPassword(), userForRegisterDto.getUsername()));
        if (!rules.isSuccess())
            return rules;

        User user = mapperService.forRequest().map(userForRegisterDto, User.class);
        user.setPassword(passwordEncoder.encode(userForRegisterDto.getPassword()));

        Set<String> strRoles = userForRegisterDto.getRole();
        Set<Role> roles = new HashSet<>();

        if (Objects.isNull(strRoles)) {
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

        // system(grocery) mail not created and activated
        // isVerifiedEmail(userForRegisterDto.getEmail());

        userRepository.save(user);
        log.info(LogInfoMessages.USER_CREATED, user.getUsername());
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

        log.info(LogInfoMessages.USER_LOGINED, userForLoginDto.getUsername());
        return new SuccessDataResult<>(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
                userDetails.getUsername(), userDetails.getEmail(), roles));
    }

    @Override
    @Transactional
    public Result update(Long id, UpdateUserRequestDto updateUserRequestDto) {
        User inDbUser = getUserById(id);

        Result rules = BusinessRules.run(userBusinessRules.isEmailExist(updateUserRequestDto.getEmail()),
                userBusinessRules.isUsernameExist(updateUserRequestDto.getUsername()),
                userBusinessRules.isValidPassword(updateUserRequestDto.getPassword(),
                        updateUserRequestDto.getUsername()));
        if (!rules.isSuccess())
            return rules;

        User user = mapperService.forRequest().map(updateUserRequestDto, User.class);
        user.setId(inDbUser.getId());
        user.setPassword(passwordEncoder.encode(updateUserRequestDto.getPassword()));
        user.setCreatedDateTime(inDbUser.getCreatedDateTime());

        Set<String> strRoles = updateUserRequestDto.getRole();
        Set<Role> roles = new HashSet<>();

        if (Objects.isNull(strRoles)) {
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
        log.info(LogInfoMessages.USER_UPDATED, user.getUsername());
        return new SuccessResult(UpdateMessages.USER_UPDATED);
    }

    @Override
    @Transactional
    public Result delete(Long id) {
        User user = getUserById(id);
        log.info(LogInfoMessages.USER_DELETED, user.getUsername(), user.getEmail());
        userRepository.delete(user);
        return new SuccessResult(DeleteMessages.USER_DELETED);
    }

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
                .map(u -> mapperService.forResponse().map(u, GetAllUserResponseDto.class)).toList();

        return new SuccessDataResult<>(returnList, GetListMessages.USERS_LISTED);
    }

    @Override
    public DataResult<GetByIdUserResponseDto> getById(Long id) {
        User inDbUser = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
        GetByIdUserResponseDto returnObj = mapperService.forResponse().map(inDbUser, GetByIdUserResponseDto.class);
        returnObj.setRoles(inDbUser.getRoles());
        return new SuccessDataResult<>(returnObj, GetByIdMessages.USER_LISTED);
    }

    @Override
    public DataResult<List<GetAllUserResponseDto>> getListBySorting(String sortBy) {
        userBusinessRules.isValidSortParameter(sortBy);

        List<User> userList = userRepository.findAll(Sort.by(Sort.Direction.ASC, sortBy));
        List<GetAllUserResponseDto> returnList = userList.stream()
                .map(u -> mapperService.forResponse().map(u, GetAllUserResponseDto.class)).toList();

        return new SuccessDataResult<>(returnList, GetListMessages.USERS_SORTED + sortBy);
    }

    @Override
    public DataResult<List<GetAllUserResponseDto>> getListByPagination(int pageNo, int pageSize) {
        userBusinessRules.isPageNumberValid(pageNo);
        userBusinessRules.isPageSizeValid(pageSize);

        List<User> userList = userRepository.findAll(PageRequest.of(pageNo, pageSize)).toList();
        List<GetAllUserResponseDto> returnList = userList.stream()
                .map(u -> mapperService.forResponse().map(u, GetAllUserResponseDto.class)).toList();

        return new SuccessDataResult<>(returnList, GetListMessages.USERS_PAGINATED);
    }

    @Override
    public DataResult<List<GetAllUserResponseDto>> getListByPaginationAndSorting(int pageNo, int pageSize,
            String sortBy) {
        userBusinessRules.isValidSortParameter(sortBy);
        userBusinessRules.isPageNumberValid(pageNo);
        userBusinessRules.isPageSizeValid(pageSize);

        List<User> userList = userRepository.findAll(PageRequest.of(pageNo, pageSize).withSort(Sort.by(sortBy)))
                .toList();
        List<GetAllUserResponseDto> returnList = userList.stream()
                .map(u -> mapperService.forResponse().map(u, GetAllUserResponseDto.class)).toList();

        return new SuccessDataResult<>(returnList, GetListMessages.USERS_PAGINATED_AND_SORTED + sortBy);
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

    // signout tasarlanacak...

}
