package com.example.grocery.business.concretes;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.grocery.business.abstracts.EmployeeService;
import com.example.grocery.business.abstracts.UserService;
import com.example.grocery.core.utilities.business.BusinessRules;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.modelMapper.ModelMapperService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.core.utilities.results.SuccessDataResult;
import com.example.grocery.core.utilities.results.SuccessResult;
import com.example.grocery.dataAccess.abstracts.EmployeeRepository;
import com.example.grocery.entity.concretes.Employee;
import com.example.grocery.webApi.requests.employee.CreateEmployeeRequest;
import com.example.grocery.webApi.requests.employee.DeleteEmployeeRequest;
import com.example.grocery.webApi.requests.employee.UpdateEmployeeRequest;
import com.example.grocery.webApi.responses.employee.GetAllEmployeeResponse;
import com.example.grocery.webApi.responses.employee.GetByIdEmployeeResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmployeeManager implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ModelMapperService modelMapperService;
    @Autowired
    private UserService userService;

    @Override
    public Result add(CreateEmployeeRequest createEmployeeRequest) {
        // asgari ücretin altında salary olamaz
        Result rules = BusinessRules.run(isExistEmail(createEmployeeRequest.getEmail()),
                isExistNationalId(createEmployeeRequest.getNationalIdentity()),
                isPermissibleAge(createEmployeeRequest.getYearOfBirth()),
                isValidPassword(createEmployeeRequest.getPassword(), createEmployeeRequest.getFirstName(),
                        createEmployeeRequest.getLastName(), createEmployeeRequest.getYearOfBirth()));

        Employee employee = modelMapperService.getModelMapper().map(createEmployeeRequest, Employee.class);
        employeeRepository.save(employee);
        log.info("added employee: {} {} logged to file!", createEmployeeRequest.getFirstName(),
                createEmployeeRequest.getLastName());
        return new SuccessResult("Employee saved in DB");
    }

    @Override
    public Result delete(DeleteEmployeeRequest deleteEmployeeRequest) {

        Result rules = BusinessRules.run(isExistId(deleteEmployeeRequest.getId()));

        Employee employee = modelMapperService.getModelMapper().map(deleteEmployeeRequest, Employee.class);
        Employee employeeForLog = employeeRepository.findById(deleteEmployeeRequest.getId())
                .orElseThrow(() -> new BusinessException("Id not found!"));
        log.info("deleted employee: {} {} logged to file!", employeeForLog.getFirstName(),
                employeeForLog.getLastName());
        employeeRepository.delete(employee);
        return new SuccessResult("Employee deleted from DB");
    }

    @Override
    public Result update(UpdateEmployeeRequest updateEmployeeRequest, int id) {

        Result rules = BusinessRules.run(isExistEmail(updateEmployeeRequest.getEmail()),
                isExistNationalId(updateEmployeeRequest.getNationalIdentity()),
                isPermissibleAge(updateEmployeeRequest.getYearOfBirth()),
                isValidPassword(updateEmployeeRequest.getPassword(), updateEmployeeRequest.getFirstName(),
                        updateEmployeeRequest.getLastName(), updateEmployeeRequest.getYearOfBirth()),
                isExistId(id));

        Employee inDbEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Id not found!"));

        Employee employee = modelMapperService.getModelMapper().map(updateEmployeeRequest, Employee.class);
        employee.setId(inDbEmployee.getId());
        employeeRepository.save(employee);
        log.info("modified employee: {} {} logged to file!", updateEmployeeRequest.getFirstName(),
                updateEmployeeRequest.getLastName());
        return new SuccessResult("Employee has been modified.");
    }

    @Override
    public DataResult<List<GetAllEmployeeResponse>> getAll() {
        List<Employee> employeeList = employeeRepository.findAll();
        List<GetAllEmployeeResponse> returnList = employeeList.stream()
                .map(e -> modelMapperService.getModelMapper().map(e, GetAllEmployeeResponse.class)).toList();
        return new SuccessDataResult<List<GetAllEmployeeResponse>>(returnList, "Employees listed");
    }

    @Override
    public DataResult<GetByIdEmployeeResponse> getById(int id) {
        Employee inDbEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Id not found!"));
        GetByIdEmployeeResponse returnObj = modelMapperService.getModelMapper().map(inDbEmployee,
                GetByIdEmployeeResponse.class);
        // returnObj.setId(inDbEmployee.getId());
        return new SuccessDataResult<GetByIdEmployeeResponse>(returnObj, "Employee listed by chosen id");
    }

    private Result isExistId(int id) {
        if (!userService.existById(id)) {
            throw new BusinessException("Id is not found on DB!");
        }
        return new SuccessResult();
    }

    private Result isExistEmail(String email) {
        if (userService.existByEmail(email)) {
            throw new BusinessException("Email address can not be repeat!");
        }
        return new SuccessResult();
    }

    private Result isExistNationalId(String nationalId) {
        if (employeeRepository.existsByNationalIdentity(nationalId)) {
            throw new BusinessException("National Identity can not be repeated!");
        }
        return new SuccessResult();
    }

    private Result isPermissibleAge(LocalDate birthYear) {
        if (LocalDate.now().getYear() - birthYear.getYear() < 18) {
            throw new BusinessException("Employer must be older than 18!");// bunu geliştir yaş 60tan büyükler olmasın
                                                                           // ve ayrıca gün ve ay kısmını doğru ayarla
        }
        return new SuccessResult();
    }

    private Result isValidPassword(String password, String firstName, String lastName, LocalDate birthDay) {
        if (password.contains(firstName)
                || password.contains(lastName)
                || password.contains(String.valueOf(birthDay.getYear()))) {
            throw new BusinessException("Password can not include firstname, lastname or year of birth");
        }
        return new SuccessResult();
    }

}
