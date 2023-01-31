package com.example.grocery.business.concretes;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.grocery.business.abstracts.CorporateCustomerService;
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
import com.example.grocery.dataAccess.abstracts.CorporateCustomerRepository;
import com.example.grocery.entity.concretes.CorporateCustomer;
import com.example.grocery.webApi.requests.corporateCustomer.CreateCorporateCustomerRequest;
import com.example.grocery.webApi.requests.corporateCustomer.DeleteCorporateCustomerRequest;
import com.example.grocery.webApi.requests.corporateCustomer.UpdateCorporateCustomerRequest;
import com.example.grocery.webApi.responses.corporateCustomer.GetAllCorporateCustomerResponse;
import com.example.grocery.webApi.responses.corporateCustomer.GetByIdCorporateCustomerResponse;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CorporateCustomerManager implements CorporateCustomerService {

        @Autowired
        private CorporateCustomerRepository corporateCustomerRepository;
        @Autowired
        private MapperService mapperService;
        @Autowired
        private UserService userService;
        @Autowired
        private PhotoService photoService;

        @Override
        @Transactional
        public Result add(CreateCorporateCustomerRequest createCorporateCustomerRequest) {

                Result rules = BusinessRules.run(isExistTaxNumber(createCorporateCustomerRequest.getTaxNumber()));
                if (!rules.isSuccess())
                        return rules;

                CorporateCustomer corporateCustomer = mapperService.getModelMapper().map(
                                createCorporateCustomerRequest,
                                CorporateCustomer.class);
                corporateCustomer.setUser(userService.getUserById(createCorporateCustomerRequest.getUserId()));
                corporateCustomer.setImage(photoService.getImageById(createCorporateCustomerRequest.getImageId()));
                corporateCustomerRepository.save(corporateCustomer);
                log.info(LogInfoMessages.CORPORATE_CUSTOMER_ADDED,
                                createCorporateCustomerRequest.getCompanyName());
                return new SuccessResult(CreateMessages.CORPORATE_CUSTOMER_CREATED);
        }

        @Override
        @Transactional
        public Result delete(DeleteCorporateCustomerRequest deleteCorporateCustomerRequest) {

                Result rules = BusinessRules.run(isExistId(deleteCorporateCustomerRequest.getId()));
                if (!rules.isSuccess())
                        return rules;

                CorporateCustomer corporateCustomer = mapperService.getModelMapper().map(
                                deleteCorporateCustomerRequest,
                                CorporateCustomer.class);
                CorporateCustomer logForCorporate = corporateCustomerRepository
                                .findById(deleteCorporateCustomerRequest.getId())
                                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
                log.info(LogInfoMessages.CORPORATE_CUSTOMER_DELETED, logForCorporate.getCompanyName());
                corporateCustomerRepository.delete(corporateCustomer);
                return new SuccessResult(DeleteMessages.CORPORATE_CUSTOMER_DELETED);
        }

        @Override
        @Transactional
        public Result update(UpdateCorporateCustomerRequest updateCorporateCustomerRequest, Long id) {

                Result rules = BusinessRules.run(isExistTaxNumber(updateCorporateCustomerRequest.getTaxNumber()));
                if (!rules.isSuccess())
                        return rules;

                CorporateCustomer inDbCorporateCustomer = corporateCustomerRepository.findById(id)
                                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));

                CorporateCustomer corporateCustomer = mapperService.getModelMapper()
                                .map(updateCorporateCustomerRequest, CorporateCustomer.class);
                corporateCustomer.setId(inDbCorporateCustomer.getId());
                corporateCustomer.setUser(userService.getUserById(updateCorporateCustomerRequest.getUserId()));
                corporateCustomer.setImage(photoService.getImageById(updateCorporateCustomerRequest.getImageId()));
                log.info(LogInfoMessages.CORPORATE_CUSTOMER_UPDATED,
                                updateCorporateCustomerRequest.getCompanyName());
                corporateCustomerRepository.save(corporateCustomer);
                return new SuccessResult(UpdateMessages.CORPORATE_CUSTOMER_MODIFIED);
        }

        @Override
        public DataResult<List<GetAllCorporateCustomerResponse>> getAll() {
                List<CorporateCustomer> corporateCustomers = corporateCustomerRepository.findAll();
                List<GetAllCorporateCustomerResponse> returnList = new ArrayList<>();
                for (CorporateCustomer forEachCustomer : corporateCustomers) {
                        GetAllCorporateCustomerResponse obj = mapperService.getModelMapper().map(forEachCustomer,
                                        GetAllCorporateCustomerResponse.class);
                        obj.setUserId(forEachCustomer.getUser().getId());
                        obj.setImageId(forEachCustomer.getImage().getId());
                        returnList.add(obj);
                }
                return new SuccessDataResult<>(returnList,
                                GetListMessages.CORPORATE_CUSTOMERS_LISTED);
        }

        @Override
        public DataResult<GetByIdCorporateCustomerResponse> getById(Long id) {
                CorporateCustomer inDbCorporateCustomer = corporateCustomerRepository.findById(id)
                                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
                GetByIdCorporateCustomerResponse returnObj = mapperService.getModelMapper()
                                .map(inDbCorporateCustomer, GetByIdCorporateCustomerResponse.class);
                returnObj.setUserId(inDbCorporateCustomer.getUser().getId());
                returnObj.setImageId(inDbCorporateCustomer.getImage().getId());
                return new SuccessDataResult<>(returnObj,
                                GetByIdMessages.CORPORATE_CUSTOMER_LISTED);
        }

        @Override
        public DataResult<List<GetAllCorporateCustomerResponse>> getListBySorting(String sortBy) {
                isValidSortParameter(sortBy);

                List<CorporateCustomer> corporateCustomers = corporateCustomerRepository
                                .findAll(Sort.by(Sort.Direction.ASC, sortBy));
                List<GetAllCorporateCustomerResponse> returnList = new ArrayList<>();
                for (CorporateCustomer forEachCustomer : corporateCustomers) {
                        GetAllCorporateCustomerResponse obj = mapperService.getModelMapper().map(forEachCustomer,
                                        GetAllCorporateCustomerResponse.class);
                        obj.setUserId(forEachCustomer.getUser().getId());
                        obj.setImageId(forEachCustomer.getImage().getId());
                        returnList.add(obj);
                }
                return new SuccessDataResult<>(returnList,
                                GetListMessages.CORPORATE_CUSTOMERS_SORTED + sortBy);
        }

        @Override
        public DataResult<List<GetAllCorporateCustomerResponse>> getListByPagination(int pageNo, int pageSize) {
                isPageNumberValid(pageNo);
                isPageSizeValid(pageSize);

                List<CorporateCustomer> corporateCustomers = corporateCustomerRepository
                                .findAll(PageRequest.of(pageNo, pageSize)).toList();
                List<GetAllCorporateCustomerResponse> returnList = new ArrayList<>();
                for (CorporateCustomer forEachCustomer : corporateCustomers) {
                        GetAllCorporateCustomerResponse obj = mapperService.getModelMapper().map(forEachCustomer,
                                        GetAllCorporateCustomerResponse.class);
                        obj.setUserId(forEachCustomer.getUser().getId());
                        obj.setImageId(forEachCustomer.getImage().getId());
                        returnList.add(obj);
                }
                return new SuccessDataResult<>(returnList,
                                GetListMessages.CORPORATE_CUSTOMERS_PAGINATED);
        }

        @Override
        public DataResult<List<GetAllCorporateCustomerResponse>> getListByPaginationAndSorting(int pageNo, int pageSize,
                        String sortBy) {
                isPageNumberValid(pageNo);
                isPageSizeValid(pageSize);
                isValidSortParameter(sortBy);

                List<CorporateCustomer> corporateCustomers = corporateCustomerRepository
                                .findAll(PageRequest.of(pageNo, pageSize).withSort(Sort.by(sortBy))).toList();
                List<GetAllCorporateCustomerResponse> returnList = new ArrayList<>();
                for (CorporateCustomer forEachCustomer : corporateCustomers) {
                        GetAllCorporateCustomerResponse obj = mapperService.getModelMapper().map(forEachCustomer,
                                        GetAllCorporateCustomerResponse.class);
                        obj.setUserId(forEachCustomer.getUser().getId());
                        obj.setImageId(forEachCustomer.getImage().getId());
                        returnList.add(obj);
                }
                return new SuccessDataResult<>(returnList,
                                GetListMessages.CORPORATE_CUSTOMERS_PAGINATED_AND_SORTED + sortBy);
        }

        private Result isExistTaxNumber(String taxNumber) {
                if (corporateCustomerRepository.existsByTaxNumber(taxNumber)) {
                        log.warn(LogWarnMessages.TAX_NUMBER_REPEATED, taxNumber);
                        throw new BusinessException(ErrorMessages.TAX_NUMBER_REPEATED);
                }
                return new SuccessResult();
        }

        private Result isExistId(Long id) {
                if (!userService.existById(id)) {
                        throw new BusinessException(ErrorMessages.ID_NOT_FOUND);
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
                CorporateCustomer checkField = new CorporateCustomer();
                if (!checkField.toString().contains(sortBy)) {
                        log.warn(LogWarnMessages.SORT_PARAMETER_NOT_VALID);
                        throw new BusinessException(ErrorMessages.SORT_PARAMETER_NOT_VALID);
                }
        }

}
