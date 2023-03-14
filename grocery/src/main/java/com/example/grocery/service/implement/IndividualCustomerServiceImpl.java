package com.example.grocery.service.concretes;

import java.util.ArrayList;
import java.util.List;

import com.example.grocery.service.rules.IndividualCustomerBusinessRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.grocery.service.abstracts.IndividualCustomerService;
import com.example.grocery.service.abstracts.PhotoService;
import com.example.grocery.service.constants.Messages.CreateMessages;
import com.example.grocery.service.constants.Messages.DeleteMessages;
import com.example.grocery.service.constants.Messages.ErrorMessages;
import com.example.grocery.service.constants.Messages.GetByIdMessages;
import com.example.grocery.service.constants.Messages.GetListMessages;
import com.example.grocery.service.constants.Messages.UpdateMessages;
import com.example.grocery.service.constants.Messages.LogMessages.LogInfoMessages;
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
import com.example.grocery.api.requests.individualCustomer.CreateIndividualCustomerRequest;
import com.example.grocery.api.requests.individualCustomer.DeleteIndividualCustomerRequest;
import com.example.grocery.api.requests.individualCustomer.UpdateIndividualCustomerRequest;
import com.example.grocery.api.responses.individualCustomer.GetAllIndividualCustomerResponse;
import com.example.grocery.api.responses.individualCustomer.GetByIdIndividualCustomerResponse;

import jakarta.transaction.Transactional;
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
        @Autowired
        private PhotoService photoService;
        @Autowired
        private IndividualCustomerBusinessRules individualCustomerBusinessRules;

        @Override
        @Transactional
        public Result add(CreateIndividualCustomerRequest createIndividualCustomerRequest) {

                Result rules = BusinessRules.run(
                                individualCustomerBusinessRules.isExistNationalId(
                                                createIndividualCustomerRequest.getNationalIdentity()),
                                individualCustomerBusinessRules
                                                .isExistUserId(createIndividualCustomerRequest.getUserId()),
                                individualCustomerBusinessRules
                                                .isExistImageId(createIndividualCustomerRequest.getImageId()));
                if (!rules.isSuccess())
                        return rules;

                IndividualCustomer individualCustomer = mapperService.forRequest().map(
                                createIndividualCustomerRequest,
                                IndividualCustomer.class);
                individualCustomer.setUser(userService.getUserById(createIndividualCustomerRequest.getUserId()));
                individualCustomer.setImage(photoService.getImageById(createIndividualCustomerRequest.getImageId()));
                individualCustomerRepository.save(individualCustomer);
                log.info(LogInfoMessages.INDIVIDUAL_CUSTOMER_ADDED,
                                createIndividualCustomerRequest.getFirstName(),
                                createIndividualCustomerRequest.getLastName());
                return new SuccessResult(CreateMessages.INDIVIDUAL_CUSTOMER_CREATED);
        }

        @Override
        @Transactional
        public Result delete(DeleteIndividualCustomerRequest deleteIndividualCustomerRequest) {

                Result rules = BusinessRules.run(
                                individualCustomerBusinessRules.isExistId(deleteIndividualCustomerRequest.getId()));
                if (!rules.isSuccess())
                        return rules;

                IndividualCustomer individualCustomer = mapperService.forRequest().map(
                                deleteIndividualCustomerRequest,
                                IndividualCustomer.class);
                IndividualCustomer logForIndividual = individualCustomerRepository
                                .findById(deleteIndividualCustomerRequest.getId())
                                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
                log.info(LogInfoMessages.INDIVIDUAL_CUSTOMER_DELETED, logForIndividual.getFirstName(),
                                logForIndividual.getLastName());
                individualCustomerRepository.delete(individualCustomer);
                return new SuccessResult(DeleteMessages.INDIVIDUAL_CUSTOMER_DELETED);
        }

        @Override
        @Transactional
        public Result update(UpdateIndividualCustomerRequest updateIndividualCustomerRequest, Long id) {
                IndividualCustomer inDbIndividualCustomer = individualCustomerRepository.findById(id)
                                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));

                IndividualCustomer individualCustomer = mapperService.forRequest().map(
                                updateIndividualCustomerRequest,
                                IndividualCustomer.class);
                individualCustomer.setId(inDbIndividualCustomer.getId());
                individualCustomer.setUser(userService.getUserById(updateIndividualCustomerRequest.getUserId()));
                individualCustomer.setImage(photoService.getImageById(updateIndividualCustomerRequest.getImageId()));
                individualCustomerRepository.save(individualCustomer);
                log.info(LogInfoMessages.INDIVIDUAL_CUSTOMER_UPDATED,
                                updateIndividualCustomerRequest.getFirstName(),
                                updateIndividualCustomerRequest.getLastName());
                return new SuccessResult(UpdateMessages.INDIVIDUAL_CUSTOMER_MODIFIED);
        }

        @Override
        public DataResult<List<GetAllIndividualCustomerResponse>> getAll() {
                List<IndividualCustomer> individualCustomers = individualCustomerRepository.findAll();
                List<GetAllIndividualCustomerResponse> returnList = new ArrayList<>();
                for (var forEachCustomer : individualCustomers) {
                        GetAllIndividualCustomerResponse obj = mapperService.forResponse().map(forEachCustomer,
                                        GetAllIndividualCustomerResponse.class);
                        returnList.add(obj);
                }
                return new SuccessDataResult<>(returnList,
                                GetListMessages.INDIVIDUAL_CUSTOMERS_LISTED);
        }

        @Override
        public DataResult<GetByIdIndividualCustomerResponse> getById(Long id) {
                IndividualCustomer inDbIndividualCustomer = individualCustomerRepository.findById(id)
                                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
                GetByIdIndividualCustomerResponse returnObj = mapperService.forResponse().map(
                                inDbIndividualCustomer,
                                GetByIdIndividualCustomerResponse.class);
                return new SuccessDataResult<>(returnObj,
                                GetByIdMessages.INDIVIDUAL_CUSTOMER_LISTED);
        }

        @Override
        public DataResult<List<GetAllIndividualCustomerResponse>> getListBySorting(String sortBy) {
                individualCustomerBusinessRules.isValidSortParameter(sortBy);

                List<IndividualCustomer> individualCustomers = individualCustomerRepository
                                .findAll(Sort.by(Sort.Direction.ASC, sortBy));
                List<GetAllIndividualCustomerResponse> returnList = new ArrayList<>();
                for (var forEachCustomer : individualCustomers) {
                        GetAllIndividualCustomerResponse obj = mapperService.forResponse().map(forEachCustomer,
                                        GetAllIndividualCustomerResponse.class);
                        returnList.add(obj);
                }
                return new SuccessDataResult<>(returnList,
                                GetListMessages.INDIVIDUAL_CUSTOMERS_SORTED + sortBy);
        }

        @Override
        public DataResult<List<GetAllIndividualCustomerResponse>> getListByPagination(int pageNo, int pageSize) {
                individualCustomerBusinessRules.isPageNumberValid(pageNo);
                individualCustomerBusinessRules.isPageSizeValid(pageSize);

                List<IndividualCustomer> individualCustomers = individualCustomerRepository
                                .findAll(PageRequest.of(pageNo, pageSize)).toList();
                List<GetAllIndividualCustomerResponse> returnList = new ArrayList<>();
                for (var forEachCustomer : individualCustomers) {
                        GetAllIndividualCustomerResponse obj = mapperService.forResponse().map(forEachCustomer,
                                        GetAllIndividualCustomerResponse.class);
                        returnList.add(obj);
                }
                return new SuccessDataResult<>(returnList,
                                GetListMessages.INDIVIDUAL_CUSTOMERS_PAGINATED);
        }

        @Override
        public DataResult<List<GetAllIndividualCustomerResponse>> getListByPaginationAndSorting(int pageNo,
                        int pageSize, String sortBy) {
                individualCustomerBusinessRules.isPageNumberValid(pageNo);
                individualCustomerBusinessRules.isPageSizeValid(pageSize);
                individualCustomerBusinessRules.isValidSortParameter(sortBy);

                List<IndividualCustomer> individualCustomers = individualCustomerRepository
                                .findAll(PageRequest.of(pageNo, pageSize).withSort(Sort.by(sortBy))).toList();
                List<GetAllIndividualCustomerResponse> returnList = new ArrayList<>();
                for (var forEachCustomer : individualCustomers) {
                        GetAllIndividualCustomerResponse obj = mapperService.forResponse().map(forEachCustomer,
                                        GetAllIndividualCustomerResponse.class);
                        returnList.add(obj);
                }
                return new SuccessDataResult<>(returnList,
                                GetListMessages.INDIVIDUAL_CUSTOMERS_PAGINATED_AND_SORTED + sortBy);
        }

}
