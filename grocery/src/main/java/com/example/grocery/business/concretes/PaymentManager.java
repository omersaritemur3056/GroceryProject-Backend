package com.example.grocery.business.concretes;

import com.example.grocery.business.abstracts.PaymentService;
import com.example.grocery.business.constants.Messages.*;
import com.example.grocery.business.constants.Messages.LogMessages.LogInfoMessages;
import com.example.grocery.business.constants.Messages.LogMessages.LogWarnMessages;
import com.example.grocery.core.utilities.business.BusinessRules;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.mapper.MapperService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.core.utilities.results.SuccessDataResult;
import com.example.grocery.core.utilities.results.SuccessResult;
import com.example.grocery.core.validation.debitCardValidation.DebitCardValidationService;
import com.example.grocery.dataAccess.abstracts.PaymentRepository;
import com.example.grocery.entity.concretes.Payment;
import com.example.grocery.webApi.requests.payment.CreatePaymentRequest;
import com.example.grocery.webApi.requests.payment.DeletePaymentRequest;
import com.example.grocery.webApi.requests.payment.UpdatePaymentRequest;
import com.example.grocery.webApi.responses.payment.GetAllPaymentResponse;
import com.example.grocery.webApi.responses.payment.GetByIdPaymentResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PaymentManager implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private MapperService mapperService;
    @Autowired
    private DebitCardValidationService debitCardValidationService;

    @Override
    @Transactional
    public Result add(CreatePaymentRequest createPaymentRequest) {
        Result rules = BusinessRules
                .run(isValidCard(createPaymentRequest.getCardNumber(), createPaymentRequest.getFullName(),
                        createPaymentRequest.getCardExpirationYear(), createPaymentRequest.getCardExpirationMonth(),
                        createPaymentRequest.getCardCvv()));
        if (!rules.isSuccess())
            return rules;

        Payment payment = mapperService.getModelMapper().map(createPaymentRequest, Payment.class);
        payment.setFullName(createPaymentRequest.getFullName().toUpperCase());
        paymentRepository.save(payment);
        log.info(LogInfoMessages.PAYMENT_CREATED, createPaymentRequest.getCardNumber(),
                createPaymentRequest.getFullName(), createPaymentRequest.getCardExpirationYear(),
                createPaymentRequest.getCardExpirationMonth(), createPaymentRequest.getCardCvv());
        return new SuccessResult(CreateMessages.PAYMENT_CREATED);
    }

    @Override
    @Transactional
    public Result delete(DeletePaymentRequest deletePaymentRequest) {

        Result rules = BusinessRules.run(isExistId(deletePaymentRequest.getId()));
        if (!rules.isSuccess())
            return rules;

        Payment payment = mapperService.getModelMapper().map(deletePaymentRequest, Payment.class);
        log.info(LogInfoMessages.PAYMENT_DELETED, deletePaymentRequest.getId());
        paymentRepository.delete(payment);
        return new SuccessResult(DeleteMessages.PAYMENT_DELETED);
    }

    @Override
    @Transactional
    public Result update(UpdatePaymentRequest updatePaymentRequest, Long id) {
        Payment inDbPayment = paymentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));

        Result rules = BusinessRules.run(isValidCard(updatePaymentRequest.getCardNumber(),
                updatePaymentRequest.getFullName(), updatePaymentRequest.getCardExpirationYear(),
                updatePaymentRequest.getCardExpirationMonth(), updatePaymentRequest.getCardCvv()), isExistId(id));
        if (!rules.isSuccess())
            return rules;

        Payment payment = mapperService.getModelMapper().map(updatePaymentRequest, Payment.class);
        payment.setId(inDbPayment.getId());
        payment.setFullName(updatePaymentRequest.getFullName().toUpperCase());
        paymentRepository.save(payment);
        log.info(LogInfoMessages.PAYMENT_UPDATED, id, updatePaymentRequest.getCardNumber(),
                updatePaymentRequest.getFullName(), updatePaymentRequest.getCardExpirationYear(),
                updatePaymentRequest.getCardExpirationMonth(), updatePaymentRequest.getCardCvv());
        return new SuccessResult(UpdateMessages.PAYMENT_UPDATED);
    }

    @Override
    public DataResult<List<GetAllPaymentResponse>> getAll() {
        List<Payment> payments = paymentRepository.findAll();
        List<GetAllPaymentResponse> returnList = payments.stream()
                .map(c -> mapperService.getModelMapper().map(c, GetAllPaymentResponse.class)).toList();

        return new SuccessDataResult<>(returnList, GetListMessages.PAYMENTS_LISTED);
    }

    @Override
    public DataResult<GetByIdPaymentResponse> getById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
        GetByIdPaymentResponse getByIdPaymentResponse = mapperService.getModelMapper().map(payment,
                GetByIdPaymentResponse.class);
        return new SuccessDataResult<>(getByIdPaymentResponse, GetByIdMessages.PAYMENT_LISTED);
    }

    @Override
    public DataResult<List<GetAllPaymentResponse>> getListBySorting(String sortBy) {
        isValidSortParameter(sortBy);

        List<Payment> payments = paymentRepository.findAll(Sort.by(Sort.Direction.ASC, sortBy));
        List<GetAllPaymentResponse> returnList = payments.stream()
                .map(c -> mapperService.getModelMapper().map(c, GetAllPaymentResponse.class)).toList();

        return new SuccessDataResult<>(returnList, GetListMessages.PAYMENTS_SORTED + sortBy);
    }

    @Override
    public DataResult<List<GetAllPaymentResponse>> getListByPagination(int pageNo, int pageSize) {
        isPageNumberValid(pageNo);
        isPageSizeValid(pageSize);

        List<Payment> payments = paymentRepository.findAll(PageRequest.of(pageNo, pageSize)).toList();
        List<GetAllPaymentResponse> returnList = payments.stream()
                .map(c -> mapperService.getModelMapper().map(c, GetAllPaymentResponse.class)).toList();

        return new SuccessDataResult<>(returnList, GetListMessages.PAYMENTS_PAGINATED);
    }

    @Override
    public DataResult<List<GetAllPaymentResponse>> getListByPaginationAndSorting(int pageNo, int pageSize,
            String sortBy) {
        isPageNumberValid(pageNo);
        isPageSizeValid(pageSize);
        isValidSortParameter(sortBy);

        List<Payment> payments = paymentRepository.findAll(PageRequest.of(pageNo, pageSize).withSort(Sort.by(sortBy)))
                .toList();
        List<GetAllPaymentResponse> returnList = payments.stream()
                .map(c -> mapperService.getModelMapper().map(c, GetAllPaymentResponse.class)).toList();

        return new SuccessDataResult<>(returnList, GetListMessages.PAYMENTS_PAGINATED_AND_SORTED + sortBy);
    }

    // Bağımlılığın kontrol altına alınması için tasarlandı
    @Override
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.PAYMENT_ID_NOT_FOUND));
    }

    private Result isValidCard(String cardNumber, String fullName, int cardExpirationYear, int cardExpirationMonth,
            String cardCvv) {
        if (!debitCardValidationService
                .checkIfRealDebitCard(cardNumber, fullName, cardExpirationYear, cardExpirationMonth, cardCvv)
                .isSuccess()) {
            log.warn(LogWarnMessages.DEBIT_CARD_NOT_VALID, cardNumber, fullName, cardExpirationYear,
                    cardExpirationMonth, cardCvv);
            throw new BusinessException(ErrorMessages.DEBIT_CARD_NOT_VALID);
        }
        return new SuccessResult();
    }

    private Result isExistId(Long id) {
        if (!paymentRepository.existsById(id)) {
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
        Payment checkField = new Payment();
        if (!checkField.toString().contains(sortBy)) {
            log.warn(LogWarnMessages.SORT_PARAMETER_NOT_VALID);
            throw new BusinessException(ErrorMessages.SORT_PARAMETER_NOT_VALID);
        }
    }

}
