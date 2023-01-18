package com.example.grocery.core.security.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.grocery.business.constants.Messages.ErrorMessages;
import com.example.grocery.business.constants.Messages.GetByIdMessages;
import com.example.grocery.business.constants.Messages.GetListMessages;
import com.example.grocery.core.security.DTOs.response.GetAllUserResponseDto;
import com.example.grocery.core.security.DTOs.response.GetByIdUserResponseDto;
import com.example.grocery.core.security.models.User;
import com.example.grocery.core.security.repository.UserRepository;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.mapper.MapperService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.SuccessDataResult;

@Service
public class UserManager implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MapperService mapperService;

    @Override
    public DataResult<List<GetAllUserResponseDto>> getAll() {
        List<User> userList = userRepository.findAll();
        List<GetAllUserResponseDto> returnList = userList.stream()
                .map(u -> mapperService.getModelMapper().map(u, GetAllUserResponseDto.class)).toList();
        return new SuccessDataResult<>(returnList, GetListMessages.USERS_LISTED);
    }

    @Override
    public DataResult<GetByIdUserResponseDto> getById(int id) {
        User inDbUser = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
        GetByIdUserResponseDto returnObj = mapperService.getModelMapper().map(inDbUser, GetByIdUserResponseDto.class);
        return new SuccessDataResult<>(returnObj, GetByIdMessages.USER_LISTED);
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
    public boolean existById(int id) {
        return userRepository.existsById(id);
    }

}
