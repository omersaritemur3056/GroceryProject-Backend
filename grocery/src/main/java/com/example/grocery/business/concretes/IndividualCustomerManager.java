package com.example.grocery.business.concretes;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.grocery.business.abstracts.IndividualCustomerService;
import com.example.grocery.business.constants.Messages.CreateMessages;
import com.example.grocery.business.constants.Messages.DeleteMessages;
import com.example.grocery.business.constants.Messages.ErrorMessages;
import com.example.grocery.business.constants.Messages.GetByIdMessages;
import com.example.grocery.business.constants.Messages.GetListMessages;
import com.example.grocery.business.constants.Messages.UpdateMessages;
import com.example.grocery.core.security.services.UserService;
import com.example.grocery.core.utilities.business.BusinessRules;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.mapper.MapperService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.core.utilities.results.SuccessDataResult;
import com.example.grocery.core.utilities.results.SuccessResult;
import com.example.grocery.dataAccess.abstracts.IndividualCustomerRepository;
import com.example.grocery.entity.concretes.IndividualCustomer;
import com.example.grocery.webApi.requests.individualCustomer.CreateIndividualCustomerRequest;
import com.example.grocery.webApi.requests.individualCustomer.DeleteIndividualCustomerRequest;
import com.example.grocery.webApi.requests.individualCustomer.UpdateIndividualCustomerRequest;
import com.example.grocery.webApi.responses.individualCustomer.GetAllIndividualCustomerResponse;
import com.example.grocery.webApi.responses.individualCustomer.GetByIdIndividualCustomerResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IndividualCustomerManager implements IndividualCustomerService {

        @Autowired
        private IndividualCustomerRepository individualCustomerRepository;
        @Autowired
        private MapperService mapperService;
        @Autowired
        private UserService userService;

        @Override
        public Result add(CreateIndividualCustomerRequest createIndividualCustomerRequest) {

                Result rules = BusinessRules.run(
                                isExistNationalId(createIndividualCustomerRequest.getNationalIdentity()));

                IndividualCustomer individualCustomer = mapperService.getModelMapper().map(
                                createIndividualCustomerRequest,
                                IndividualCustomer.class);
                individualCustomer.setUser(userService.getUserById(createIndividualCustomerRequest.getUserId()));
                individualCustomerRepository.save(individualCustomer);
                log.info("added individual customer: {} {} logged to file!",
                                createIndividualCustomerRequest.getFirstName(),
                                createIndividualCustomerRequest.getLastName());
                return new SuccessResult(CreateMessages.INDIVIDUAL_CUSTOMER_CREATED);
        }

        @Override
        public Result delete(DeleteIndividualCustomerRequest deleteIndividualCustomerRequest) {

                Result rules = BusinessRules.run(isExistId(deleteIndividualCustomerRequest.getId()));

                IndividualCustomer individualCustomer = mapperService.getModelMapper().map(
                                deleteIndividualCustomerRequest,
                                IndividualCustomer.class);
                IndividualCustomer logForIndividual = individualCustomerRepository
                                .findById(deleteIndividualCustomerRequest.getId())
                                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
                log.info("deleted individual customer: {} {} logged to file!", logForIndividual.getFirstName(),
                                logForIndividual.getLastName());
                individualCustomerRepository.delete(individualCustomer);
                return new SuccessResult(DeleteMessages.INDIVIDUAL_CUSTOMER_DELETED);
        }

        @Override
        public Result update(UpdateIndividualCustomerRequest updateIndividualCustomerRequest, Long id) {

                Result rules = BusinessRules.run(
                                isExistNationalId(updateIndividualCustomerRequest.getNationalIdentity()));

                IndividualCustomer inDbIndividualCustomer = individualCustomerRepository.findById(id)
                                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));

                IndividualCustomer individualCustomer = mapperService.getModelMapper().map(
                                updateIndividualCustomerRequest,
                                IndividualCustomer.class);
                individualCustomer.setId(inDbIndividualCustomer.getId());
                individualCustomer.setUser(userService.getUserById(updateIndividualCustomerRequest.getUserId()));
                individualCustomerRepository.save(individualCustomer);
                log.info("modified individual customer: {} {} logged to file!",
                                updateIndividualCustomerRequest.getFirstName(),
                                updateIndividualCustomerRequest.getLastName());
                return new SuccessResult(UpdateMessages.INDIVIDUAL_CUSTOMER_MODIFIED);
        }

        @Override
        public DataResult<List<GetAllIndividualCustomerResponse>> getAll() {
                List<IndividualCustomer> individualCustomers = individualCustomerRepository.findAll();
                List<GetAllIndividualCustomerResponse> returnList = new ArrayList<>();
                for (var forEachCustomer : individualCustomers) {
                        GetAllIndividualCustomerResponse obj = mapperService.getModelMapper().map(forEachCustomer,
                                        GetAllIndividualCustomerResponse.class);
                        obj.setUserId(forEachCustomer.getUser().getId());
                        returnList.add(obj);
                }
                return new SuccessDataResult<>(returnList,
                                GetListMessages.INDIVIDUAL_CUSTOMERS_LISTED);
        }

        @Override
        public DataResult<GetByIdIndividualCustomerResponse> getById(Long id) {
                IndividualCustomer inDbIndividualCustomer = individualCustomerRepository.findById(id)
                                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
                GetByIdIndividualCustomerResponse returnObj = mapperService.getModelMapper().map(
                                inDbIndividualCustomer,
                                GetByIdIndividualCustomerResponse.class);
                returnObj.setUserId(inDbIndividualCustomer.getUser().getId());
                return new SuccessDataResult<>(returnObj,
                                GetByIdMessages.INDIVIDUAL_CUSTOMER_LISTED);
        }

        private Result isExistId(Long id) {
                if (!userService.existById(id)) {
                        throw new BusinessException(ErrorMessages.ID_NOT_FOUND);
                }
                return new SuccessResult();
        }

        private Result isExistNationalId(String nationalId) {
                if (individualCustomerRepository.existsByNationalIdentity(nationalId)) {
                        throw new BusinessException(ErrorMessages.NATIONAL_IDENTITY_REPEATED);
                }
                return new SuccessResult();
        }
}
