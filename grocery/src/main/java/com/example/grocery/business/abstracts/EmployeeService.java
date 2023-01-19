package com.example.grocery.business.abstracts;

import java.util.List;

import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.webApi.requests.employee.CreateEmployeeRequest;
import com.example.grocery.webApi.requests.employee.DeleteEmployeeRequest;
import com.example.grocery.webApi.requests.employee.UpdateEmployeeRequest;
import com.example.grocery.webApi.responses.employee.GetAllEmployeeResponse;
import com.example.grocery.webApi.responses.employee.GetByIdEmployeeResponse;

public interface EmployeeService {

    Result add(CreateEmployeeRequest createEmployeeRequest);

    Result delete(DeleteEmployeeRequest deleteEmployeeRequest);

    Result update(UpdateEmployeeRequest updateEmployeeRequest, Long id);

    DataResult<List<GetAllEmployeeResponse>> getAll();

    DataResult<GetByIdEmployeeResponse> getById(Long id);
}
