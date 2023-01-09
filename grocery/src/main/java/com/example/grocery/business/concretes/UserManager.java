package com.example.grocery.business.concretes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.grocery.business.abstracts.UserService;
import com.example.grocery.business.constants.Messages;
import com.example.grocery.business.constants.Messages.ErrorMessages;
import com.example.grocery.business.constants.Messages.GetByIdMessages;
import com.example.grocery.business.constants.Messages.GetListMessages;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.mapper.MapperService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.SuccessDataResult;
import com.example.grocery.dataAccess.abstracts.UserRepository;
import com.example.grocery.entity.concretes.User;
import com.example.grocery.webApi.responses.user.GetAllUserResponse;
import com.example.grocery.webApi.responses.user.GetByIdUserResponse;

@Service
public class UserManager implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MapperService mapperService;

    @Override
    public DataResult<List<GetAllUserResponse>> getAll() {
        List<User> userList = userRepository.findAll();
        List<GetAllUserResponse> returnList = userList.stream()
                .map(u -> mapperService.getModelMapper().map(u, GetAllUserResponse.class)).toList();
        return new SuccessDataResult<>(returnList, GetListMessages.USERS_LISTED);
    }

    @Override
    public DataResult<GetByIdUserResponse> getById(int id) {
        User inDbUser = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
        GetByIdUserResponse returnObj = mapperService.getModelMapper().map(inDbUser, GetByIdUserResponse.class);
        return new SuccessDataResult<>(returnObj, GetByIdMessages.USER_LISTED);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
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
