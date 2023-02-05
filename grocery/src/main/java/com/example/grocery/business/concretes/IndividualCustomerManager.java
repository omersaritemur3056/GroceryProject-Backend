package com.example.grocery.business.concretes;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.grocery.business.abstracts.IndividualCustomerService;
import com.example.grocery.business.abstracts.PhotoService;
import com.example.grocery.business.constants.Messages.CreateMessages;
import com.example.grocery.business.constants.Messages.DeleteMessages;
import com.example.grocery.business.constants.Messages.ErrorMessages;
import com.example.grocery.business.constants.Messages.GetByIdMessages;
import com.example.grocery.business.constants.Messages.GetListMessages;
import com.example.grocery.business.constants.Messages.UpdateMessages;
import com.example.grocery.business.constants.Messages.LogMessages.LogInfoMessages;
import com.example.grocery.business.constants.Messages.LogMessages.LogWarnMessages;
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

        @Override
        @Transactional
        public Result add(CreateIndividualCustomerRequest createIndividualCustomerRequest) {

                Result rules = BusinessRules.run(
                                isExistNationalId(createIndividualCustomerRequest.getNationalIdentity()),
                                isExistUserId(createIndividualCustomerRequest.getUserId()),
                                isExistImageId(createIndividualCustomerRequest.getImageId()));
                if (!rules.isSuccess())
                        return rules;

                IndividualCustomer individualCustomer = mapperService.getModelMapper().map(
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

                Result rules = BusinessRules.run(isExistId(deleteIndividualCustomerRequest.getId()));
                if (!rules.isSuccess())
                        return rules;

                IndividualCustomer individualCustomer = mapperService.getModelMapper().map(
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

                IndividualCustomer individualCustomer = mapperService.getModelMapper().map(
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
                        GetAllIndividualCustomerResponse obj = mapperService.getModelMapper().map(forEachCustomer,
                                        GetAllIndividualCustomerResponse.class);
                        obj.setUserId(forEachCustomer.getUser().getId());
                        obj.setImageId(forEachCustomer.getImage().getId());
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
                returnObj.setImageId(inDbIndividualCustomer.getImage().getId());
                return new SuccessDataResult<>(returnObj,
                                GetByIdMessages.INDIVIDUAL_CUSTOMER_LISTED);
        }

        @Override
        public DataResult<List<GetAllIndividualCustomerResponse>> getListBySorting(String sortBy) {
                isValidSortParameter(sortBy);

                List<IndividualCustomer> individualCustomers = individualCustomerRepository
                                .findAll(Sort.by(Sort.Direction.ASC, sortBy));
                List<GetAllIndividualCustomerResponse> returnList = new ArrayList<>();
                for (var forEachCustomer : individualCustomers) {
                        GetAllIndividualCustomerResponse obj = mapperService.getModelMapper().map(forEachCustomer,
                                        GetAllIndividualCustomerResponse.class);
                        obj.setUserId(forEachCustomer.getUser().getId());
                        obj.setImageId(forEachCustomer.getImage().getId());
                        returnList.add(obj);
                }
                return new SuccessDataResult<>(returnList,
                                GetListMessages.INDIVIDUAL_CUSTOMERS_SORTED + sortBy);
        }

        @Override
        public DataResult<List<GetAllIndividualCustomerResponse>> getListByPagination(int pageNo, int pageSize) {
                isPageNumberValid(pageNo);
                isPageSizeValid(pageSize);

                List<IndividualCustomer> individualCustomers = individualCustomerRepository
                                .findAll(PageRequest.of(pageNo, pageSize)).toList();
                List<GetAllIndividualCustomerResponse> returnList = new ArrayList<>();
                for (var forEachCustomer : individualCustomers) {
                        GetAllIndividualCustomerResponse obj = mapperService.getModelMapper().map(forEachCustomer,
                                        GetAllIndividualCustomerResponse.class);
                        obj.setUserId(forEachCustomer.getUser().getId());
                        obj.setImageId(forEachCustomer.getImage().getId());
                        returnList.add(obj);
                }
                return new SuccessDataResult<>(returnList,
                                GetListMessages.INDIVIDUAL_CUSTOMERS_PAGINATED);
        }

        @Override
        public DataResult<List<GetAllIndividualCustomerResponse>> getListByPaginationAndSorting(int pageNo,
                        int pageSize, String sortBy) {
                isPageNumberValid(pageNo);
                isPageSizeValid(pageSize);
                isValidSortParameter(sortBy);

                List<IndividualCustomer> individualCustomers = individualCustomerRepository
                                .findAll(PageRequest.of(pageNo, pageSize).withSort(Sort.by(sortBy))).toList();
                List<GetAllIndividualCustomerResponse> returnList = new ArrayList<>();
                for (var forEachCustomer : individualCustomers) {
                        GetAllIndividualCustomerResponse obj = mapperService.getModelMapper().map(forEachCustomer,
                                        GetAllIndividualCustomerResponse.class);
                        obj.setUserId(forEachCustomer.getUser().getId());
                        obj.setImageId(forEachCustomer.getImage().getId());
                        returnList.add(obj);
                }
                return new SuccessDataResult<>(returnList,
                                GetListMessages.INDIVIDUAL_CUSTOMERS_PAGINATED_AND_SORTED + sortBy);
        }

        private Result isExistId(Long id) {
                if (!userService.existById(id)) {
                        throw new BusinessException(ErrorMessages.ID_NOT_FOUND);
                }
                return new SuccessResult();
        }

        private Result isExistNationalId(String nationalId) {
                if (individualCustomerRepository.existsByNationalIdentity(nationalId)) {
                        log.warn(LogWarnMessages.NATIONAL_IDENTITY_REPEATED, nationalId);
                        throw new BusinessException(ErrorMessages.NATIONAL_IDENTITY_REPEATED);
                }
                return new SuccessResult();
        }

        private Result isExistUserId(Long userId) {
                if (individualCustomerRepository.existsByUser_Id(userId)) {
                        log.warn(LogWarnMessages.USER_ID_REPEATED, userId);
                        throw new BusinessException(ErrorMessages.USER_ID_REPEATED);
                }
                return new SuccessResult();
        }

        private Result isExistImageId(Long imageId) {
                if (individualCustomerRepository.existsByImage_Id(imageId)) {
                        log.warn(LogWarnMessages.IMAGE_ID_REPEATED, imageId);
                        throw new BusinessException(ErrorMessages.IMAGE_ID_REPEATED);
                }
                return new SuccessResult();
        }

        private void isPageNumberValid(int pageNo) {
                if (pageNo < 0) {
                        log.warn(LogWarnMessages.PAGE_NUMBER_NEGATIVE);
                        throw new BusinessException(ErrorMessages.PAGE_NUMBER_NEGATIVE);
                }
        }

        private void isPageSizeValid(int pageSize) {
                if (pageSize < 1) {
                        log.warn(LogWarnMessages.PAGE_SIZE_NEGATIVE);
                        throw new BusinessException(ErrorMessages.PAGE_SIZE_NEGATIVE);
                }
        }

        private void isValidSortParameter(String sortBy) {
                IndividualCustomer checkField = new IndividualCustomer();
                if (!checkField.toString().contains(sortBy)) {
                        log.warn(LogWarnMessages.SORT_PARAMETER_NOT_VALID);
                        throw new BusinessException(ErrorMessages.SORT_PARAMETER_NOT_VALID);
                }
        }

}
