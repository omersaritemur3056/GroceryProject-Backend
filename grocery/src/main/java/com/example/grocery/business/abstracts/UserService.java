package com.example.grocery.business.abstracts;

import java.util.List;

import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.entity.concretes.User;
import com.example.grocery.webApi.responses.user.GetAllUserResponse;
import com.example.grocery.webApi.responses.user.GetByIdUserResponse;

public interface UserService {

    DataResult<List<GetAllUserResponse>> getAll();

    DataResult<GetByIdUserResponse> getById(int id);

    User getByEmail(String email);

    boolean existByEmail(String email);

    boolean existById(int id);
}
