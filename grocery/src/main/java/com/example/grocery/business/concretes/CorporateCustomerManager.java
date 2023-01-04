package com.example.grocery.business.concretes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.grocery.business.abstracts.CorporateCustomerService;
import com.example.grocery.business.abstracts.UserService;
import com.example.grocery.core.utilities.business.BusinessRules;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.modelMapper.ModelMapperService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.core.utilities.results.SuccessDataResult;
import com.example.grocery.core.utilities.results.SuccessResult;
import com.example.grocery.dataAccess.abstracts.CorporateCustomerRepository;
import com.example.grocery.entity.concretes.CorporateCustomer;
import com.example.grocery.webApi.requests.corporateCustomer.CreateCorporateCustomerRequest;
import com.example.grocery.webApi.requests.corporateCustomer.DeleteCorporateCustomerRequest;
import com.example.grocery.webApi.requests.corporateCustomer.UpdateCorporateCustomerRequest;
import com.example.grocery.webApi.responses.corporateCustomer.GetAllCorporateCustomerResponse;
import com.example.grocery.webApi.responses.corporateCustomer.GetByIdCorporateCustomerResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CorporateCustomerManager implements CorporateCustomerService {

        @Autowired
        private CorporateCustomerRepository corporateCustomerRepository;
        @Autowired
        private ModelMapperService modelMapperService;
        @Autowired
        private UserService userService;

        @Override
        public Result add(CreateCorporateCustomerRequest createCorporateCustomerRequest) {

                Result rules = BusinessRules.run(isExistEmail(createCorporateCustomerRequest.getEmail()),
                                isExistTaxNumber(createCorporateCustomerRequest.getTaxNumber()));

                CorporateCustomer corporateCustomer = modelMapperService.getModelMapper().map(
                                createCorporateCustomerRequest,
                                CorporateCustomer.class);
                corporateCustomerRepository.save(corporateCustomer);
                log.info("added corporate customer: {} logged to file!",
                                createCorporateCustomerRequest.getCompanyName());
                return new SuccessResult("Corporate customer saved in DB");
        }

        @Override
        public Result delete(DeleteCorporateCustomerRequest deleteCorporateCustomerRequest) {

                Result rules = BusinessRules.run(isExistId(deleteCorporateCustomerRequest.getId()));

                CorporateCustomer corporateCustomer = modelMapperService.getModelMapper().map(
                                deleteCorporateCustomerRequest,
                                CorporateCustomer.class);
                CorporateCustomer logForCorporate = corporateCustomerRepository
                                .findById(deleteCorporateCustomerRequest.getId())
                                .orElseThrow(() -> new BusinessException("Id not found!"));
                log.info("deleted corporate customer: {} logged to file!", logForCorporate.getCompanyName());
                corporateCustomerRepository.delete(corporateCustomer);
                return new SuccessResult("Corporate customer has been removed from DB");
        }

        @Override
        public Result update(UpdateCorporateCustomerRequest updateCorporateCustomerRequest, int id) {

                Result rules = BusinessRules.run(isExistEmail(updateCorporateCustomerRequest.getEmail()),
                                isExistTaxNumber(updateCorporateCustomerRequest.getTaxNumber()));

                CorporateCustomer inDbCorporateCustomer = corporateCustomerRepository.findById(id)
                                .orElseThrow(() -> new BusinessException("Id not found!"));

                CorporateCustomer corporateCustomer = modelMapperService.getModelMapper()
                                .map(updateCorporateCustomerRequest, CorporateCustomer.class);
                corporateCustomer.setId(inDbCorporateCustomer.getId());
                log.info("modified corporate customer: {} logged to file!",
                                updateCorporateCustomerRequest.getCompanyName());
                corporateCustomerRepository.save(corporateCustomer);
                return new SuccessResult("Corporate customer has been modified");
        }

        @Override
        public DataResult<List<GetAllCorporateCustomerResponse>> getAll() {
                List<CorporateCustomer> corporateCustomers = corporateCustomerRepository.findAll();
                List<GetAllCorporateCustomerResponse> returnList = corporateCustomers.stream()
                                .map(cc -> modelMapperService.getModelMapper().map(cc,
                                                GetAllCorporateCustomerResponse.class))
                                .toList();
                return new SuccessDataResult<List<GetAllCorporateCustomerResponse>>(returnList,
                                "Corporate customers listed");
        }

        @Override
        public DataResult<GetByIdCorporateCustomerResponse> getById(int id) {
                CorporateCustomer inDbCorporateCustomer = corporateCustomerRepository.findById(id)
                                .orElseThrow(() -> new BusinessException("Id not found!"));
                GetByIdCorporateCustomerResponse returnObj = modelMapperService.getModelMapper()
                                .map(inDbCorporateCustomer, GetByIdCorporateCustomerResponse.class);
                return new SuccessDataResult<GetByIdCorporateCustomerResponse>(returnObj,
                                "Corporate customer listed by chosen id");
        }

        private Result isExistEmail(String email) {
                if (userService.existByEmail(email)) {
                        throw new BusinessException("Email address can not be repeat!");
                }
                return new SuccessResult();
        }

        private Result isExistTaxNumber(String taxNumber) {
                if (corporateCustomerRepository.existsByTaxNumber(taxNumber)) {
                        throw new BusinessException("Tax number can not be repeat!");
                }
                return new SuccessResult();
        }

        private Result isExistId(int id) {
                if (!userService.existById(id)) {
                        throw new BusinessException("Id is not found on DB!");
                }
                return new SuccessResult();
        }
}
