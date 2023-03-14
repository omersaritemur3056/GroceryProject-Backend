package com.example.grocery.core.validation.mernisValidation;

import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.model.concretes.Employee;

public interface MernisValidationService {
    Result validate(Employee employee);
}
