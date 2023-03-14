package com.example.grocery.service.abstracts;

import java.util.List;

import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.api.requests.employee.CreateEmployeeRequest;
import com.example.grocery.api.requests.employee.DeleteEmployeeRequest;
import com.example.grocery.api.requests.employee.UpdateEmployeeRequest;
import com.example.grocery.api.responses.employee.GetAllEmployeeResponse;
import com.example.grocery.api.responses.employee.GetByIdEmployeeResponse;

public interface EmployeeService {

    Result add(CreateEmployeeRequest createEmployeeRequest);

    Result delete(DeleteEmployeeRequest deleteEmployeeRequest);

    Result update(UpdateEmployeeRequest updateEmployeeRequest, Long id);

    DataResult<List<GetAllEmployeeResponse>> getAll();

    DataResult<GetByIdEmployeeResponse> getById(Long id);

    DataResult<List<GetAllEmployeeResponse>> getListBySorting(String sortBy);

    DataResult<List<GetAllEmployeeResponse>> getListByPagination(int pageNo, int pageSize);

    DataResult<List<GetAllEmployeeResponse>> getListByPaginationAndSorting(int pageNo, int pageSize, String sortBy);
}
