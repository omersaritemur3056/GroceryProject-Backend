package com.example.grocery.outservices.mernisValidator;

import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.results.ErrorResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.core.utilities.results.SuccessResult;
import com.example.grocery.core.validation.mernisValidation.MernisValidationService;
import com.example.grocery.model.concretes.Employee;
import com.example.grocery.outservices.mernisValidator.mernis.FLAKPSPublicSoap;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class MernisValidationServiceImpl implements MernisValidationService {

    @Override
    public Result validate(Employee employee) {
        FLAKPSPublicSoap client = new FLAKPSPublicSoap();

        boolean result = false;
        try {
            result = client.TCKimlikNoDogrula(Long.parseLong(employee.getNationalIdentity()),
                    employee.getFirstName().toUpperCase(new Locale("tr", "TR")),
                    employee.getLastName().toUpperCase(new Locale("tr", "TR")),
                    employee.getBirthYear().getYear());
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        if(!result){
            return new ErrorResult();
        }
        return new SuccessResult();
    }
}
