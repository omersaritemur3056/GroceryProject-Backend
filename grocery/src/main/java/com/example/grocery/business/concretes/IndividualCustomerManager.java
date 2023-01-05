package com.example.grocery.business.concretes;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.grocery.business.abstracts.IndividualCustomerService;
import com.example.grocery.business.abstracts.UserService;
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

                Result rules = BusinessRules.run(isExistEmail(createIndividualCustomerRequest.getEmail()),
                                isExistNationalId(createIndividualCustomerRequest.getNationalIdentity()),
                                isValidPassword(createIndividualCustomerRequest.getPassword(),
                                                createIndividualCustomerRequest.getFirstName(),
                                                createIndividualCustomerRequest.getLastName()));

                IndividualCustomer individualCustomer = mapperService.getModelMapper().map(
                                createIndividualCustomerRequest,
                                IndividualCustomer.class);
                individualCustomerRepository.save(individualCustomer);
                log.info("added individual customer: {} {} logged to file!",
                                createIndividualCustomerRequest.getFirstName(),
                                createIndividualCustomerRequest.getLastName());
                return new SuccessResult("Individual customer saved in DB");
        }

        @Override
        public Result delete(DeleteIndividualCustomerRequest deleteIndividualCustomerRequest) {

                Result rules = BusinessRules.run(isExistId(deleteIndividualCustomerRequest.getId()));

                IndividualCustomer individualCustomer = mapperService.getModelMapper().map(
                                deleteIndividualCustomerRequest,
                                IndividualCustomer.class);
                IndividualCustomer logForIndividual = individualCustomerRepository
                                .findById(deleteIndividualCustomerRequest.getId())
                                .orElseThrow(() -> new BusinessException("Id not found!"));
                log.info("deleted individual customer: {} {} logged to file!", logForIndividual.getFirstName(),
                                logForIndividual.getLastName());
                individualCustomerRepository.delete(individualCustomer);
                return new SuccessResult("Individual customer removed from DB");
        }

        @Override
        public Result update(UpdateIndividualCustomerRequest updateIndividualCustomerRequest, int id) {

                Result rules = BusinessRules.run(isExistEmail(updateIndividualCustomerRequest.getEmail()),
                                isExistNationalId(updateIndividualCustomerRequest.getNationalIdentity()),
                                isValidPassword(updateIndividualCustomerRequest.getPassword(),
                                                updateIndividualCustomerRequest.getFirstName(),
                                                updateIndividualCustomerRequest.getLastName()));

                IndividualCustomer inDbIndividualCustomer = individualCustomerRepository.findById(id)
                                .orElseThrow(() -> new BusinessException("Id not found!"));

                IndividualCustomer individualCustomer = mapperService.getModelMapper().map(
                                updateIndividualCustomerRequest,
                                IndividualCustomer.class);
                individualCustomer.setId(inDbIndividualCustomer.getId());
                individualCustomerRepository.save(individualCustomer);
                log.info("modified individual customer: {} {} logged to file!",
                                updateIndividualCustomerRequest.getFirstName(),
                                updateIndividualCustomerRequest.getLastName());
                return new SuccessResult("Individual customer has been modified");
        }

        @Override
        public DataResult<List<GetAllIndividualCustomerResponse>> getAll() {
                List<IndividualCustomer> individualCustomers = individualCustomerRepository.findAll();
                List<GetAllIndividualCustomerResponse> returnList = individualCustomers.stream()
                                .map(ic -> mapperService.getModelMapper().map(ic,
                                                GetAllIndividualCustomerResponse.class))
                                .toList();
                return new SuccessDataResult<List<GetAllIndividualCustomerResponse>>(returnList,
                                "Individual customers listed");
        }

        @Override
        public DataResult<GetByIdIndividualCustomerResponse> getById(int id) {
                IndividualCustomer inDbIndividualCustomer = individualCustomerRepository.findById(id)
                                .orElseThrow(() -> new BusinessException("Id not found!"));
                GetByIdIndividualCustomerResponse returnObj = mapperService.getModelMapper().map(
                                inDbIndividualCustomer,
                                GetByIdIndividualCustomerResponse.class);
                return new SuccessDataResult<GetByIdIndividualCustomerResponse>(returnObj,
                                "Individual customer listed by chosen id");
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
                if (individualCustomerRepository.existsByNationalIdentity(nationalId)) {
                        throw new BusinessException("National Identity can not be repeated!");
                }
                return new SuccessResult();
        }

        private Result isValidPassword(String password, String firstName, String lastName) {
                if (password.contains(firstName)
                                || password.contains(lastName)) {
                        throw new BusinessException("Password can not include firstname or lastname");
                }
                return new SuccessResult();
        }
}
