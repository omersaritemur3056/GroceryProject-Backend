package com.example.grocery.service.implement;

import java.util.ArrayList;
import java.util.List;

import com.example.grocery.service.interfaces.PhotoService;
import com.example.grocery.service.rules.CorporateCustomerBusinessRules;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.grocery.service.interfaces.CorporateCustomerService;
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
import com.example.grocery.repository.CorporateCustomerRepository;
import com.example.grocery.model.concretes.CorporateCustomer;
import com.example.grocery.api.requests.corporateCustomer.CreateCorporateCustomerRequest;
import com.example.grocery.api.requests.corporateCustomer.DeleteCorporateCustomerRequest;
import com.example.grocery.api.requests.corporateCustomer.UpdateCorporateCustomerRequest;
import com.example.grocery.api.responses.corporateCustomer.GetAllCorporateCustomerResponse;
import com.example.grocery.api.responses.corporateCustomer.GetByIdCorporateCustomerResponse;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class CorporateCustomerServiceImpl implements CorporateCustomerService {

        private final CorporateCustomerRepository corporateCustomerRepository;
        private final MapperService mapperService;
        private final UserService userService;
        private final PhotoService photoService;
        private final CorporateCustomerBusinessRules corporateCustomerBusinessRules;

        @Override
        @Transactional
        public Result add(CreateCorporateCustomerRequest createCorporateCustomerRequest) {
                Result rules = BusinessRules.run(
                                corporateCustomerBusinessRules
                                                .isExistTaxNumber(createCorporateCustomerRequest.getTaxNumber()),
                                corporateCustomerBusinessRules
                                                .isExistImageId(createCorporateCustomerRequest.getImageId()),
                                corporateCustomerBusinessRules
                                                .isExistUserId(createCorporateCustomerRequest.getUserId()));
                if (!rules.isSuccess())
                        return rules;

                CorporateCustomer corporateCustomer = mapperService.forRequest().map(
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
                Result rules = BusinessRules
                                .run(corporateCustomerBusinessRules.isExistId(deleteCorporateCustomerRequest.getId()));
                if (!rules.isSuccess())
                        return rules;

                CorporateCustomer corporateCustomer = mapperService.forRequest().map(
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
                CorporateCustomer inDbCorporateCustomer = corporateCustomerRepository.findById(id)
                                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));

                CorporateCustomer corporateCustomer = mapperService.forRequest()
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
                        GetAllCorporateCustomerResponse obj = mapperService.forResponse().map(forEachCustomer,
                                        GetAllCorporateCustomerResponse.class);
                        returnList.add(obj);
                }
                return new SuccessDataResult<>(returnList,
                                GetListMessages.CORPORATE_CUSTOMERS_LISTED);
        }

        @Override
        public DataResult<GetByIdCorporateCustomerResponse> getById(Long id) {
                CorporateCustomer inDbCorporateCustomer = corporateCustomerRepository.findById(id)
                                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
                GetByIdCorporateCustomerResponse returnObj = mapperService.forResponse()
                                .map(inDbCorporateCustomer, GetByIdCorporateCustomerResponse.class);
                return new SuccessDataResult<>(returnObj,
                                GetByIdMessages.CORPORATE_CUSTOMER_LISTED);
        }

        @Override
        public DataResult<List<GetAllCorporateCustomerResponse>> getListBySorting(String sortBy) {
                corporateCustomerBusinessRules.isValidSortParameter(sortBy);

                List<CorporateCustomer> corporateCustomers = corporateCustomerRepository
                                .findAll(Sort.by(Sort.Direction.ASC, sortBy));
                List<GetAllCorporateCustomerResponse> returnList = new ArrayList<>();
                for (CorporateCustomer forEachCustomer : corporateCustomers) {
                        GetAllCorporateCustomerResponse obj = mapperService.forResponse().map(forEachCustomer,
                                        GetAllCorporateCustomerResponse.class);
                        returnList.add(obj);
                }
                return new SuccessDataResult<>(returnList,
                                GetListMessages.CORPORATE_CUSTOMERS_SORTED + sortBy);
        }

        @Override
        public DataResult<List<GetAllCorporateCustomerResponse>> getListByPagination(int pageNo, int pageSize) {
                corporateCustomerBusinessRules.isPageNumberValid(pageNo);
                corporateCustomerBusinessRules.isPageSizeValid(pageSize);

                List<CorporateCustomer> corporateCustomers = corporateCustomerRepository
                                .findAll(PageRequest.of(pageNo, pageSize)).toList();
                List<GetAllCorporateCustomerResponse> returnList = new ArrayList<>();
                for (CorporateCustomer forEachCustomer : corporateCustomers) {
                        GetAllCorporateCustomerResponse obj = mapperService.forResponse().map(forEachCustomer,
                                        GetAllCorporateCustomerResponse.class);
                        returnList.add(obj);
                }
                return new SuccessDataResult<>(returnList,
                                GetListMessages.CORPORATE_CUSTOMERS_PAGINATED);
        }

        @Override
        public DataResult<List<GetAllCorporateCustomerResponse>> getListByPaginationAndSorting(int pageNo, int pageSize,
                        String sortBy) {
                corporateCustomerBusinessRules.isPageNumberValid(pageNo);
                corporateCustomerBusinessRules.isPageSizeValid(pageSize);
                corporateCustomerBusinessRules.isValidSortParameter(sortBy);

                List<CorporateCustomer> corporateCustomers = corporateCustomerRepository
                                .findAll(PageRequest.of(pageNo, pageSize).withSort(Sort.by(sortBy))).toList();
                List<GetAllCorporateCustomerResponse> returnList = new ArrayList<>();
                for (CorporateCustomer forEachCustomer : corporateCustomers) {
                        GetAllCorporateCustomerResponse obj = mapperService.forResponse().map(forEachCustomer,
                                        GetAllCorporateCustomerResponse.class);
                        returnList.add(obj);
                }
                return new SuccessDataResult<>(returnList,
                                GetListMessages.CORPORATE_CUSTOMERS_PAGINATED_AND_SORTED + sortBy);
        }

}
