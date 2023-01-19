package com.example.grocery.business.concretes;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.grocery.business.abstracts.PaymentService;
import com.example.grocery.business.constants.Messages.CreateMessages;
import com.example.grocery.business.constants.Messages.DeleteMessages;
import com.example.grocery.business.constants.Messages.ErrorMessages;
import com.example.grocery.business.constants.Messages.GetByIdMessages;
import com.example.grocery.business.constants.Messages.GetListMessages;
import com.example.grocery.business.constants.Messages.UpdateMessages;
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

import lombok.extern.slf4j.Slf4j;

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
    public Result add(CreatePaymentRequest createPaymentRequest) {
        // Card number ve Cvv numaralarını formatlacak kodlar yazılacak...
        Result rules = BusinessRules
                .run(isValidCard(createPaymentRequest.getCardNumber(), createPaymentRequest.getFullName(),
                        createPaymentRequest.getCardExpirationYear(), createPaymentRequest.getCardExpirationMonth(),
                        createPaymentRequest.getCardCvv()));

        Payment payment = mapperService.getModelMapper().map(createPaymentRequest, Payment.class);
        payment.setFullName(createPaymentRequest.getFullName().toUpperCase());
        paymentRepository.save(payment);
        log.info("card information has been successfully saved to the database");
        return new SuccessResult(CreateMessages.PAYMENT_CREATED);
    }

    @Override
    public Result delete(DeletePaymentRequest deletePaymentRequest) {

        Result rules = BusinessRules.run(isExistId(deletePaymentRequest.getId()));

        Payment payment = mapperService.getModelMapper().map(deletePaymentRequest, Payment.class);
        paymentRepository.delete(payment);
        log.info("card information has been successfully deleted from DB");
        return new SuccessResult(DeleteMessages.PAYMENT_DELETED);
    }

    @Override
    public Result update(UpdatePaymentRequest updatePaymentRequest, Long id) {
        // Card number, Cvv numaralarını formatlacak kodlar yazılacak...
        Payment inDbPayment = paymentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));

        Result rules = BusinessRules.run(isValidCard(updatePaymentRequest.getCardNumber(),
                updatePaymentRequest.getFullName(), updatePaymentRequest.getCardExpirationYear(),
                updatePaymentRequest.getCardExpirationMonth(), updatePaymentRequest.getCardCvv()), isExistId(id));

        Payment payment = mapperService.getModelMapper().map(updatePaymentRequest, Payment.class);
        payment.setId(inDbPayment.getId());
        payment.setFullName(updatePaymentRequest.getFullName().toUpperCase());
        paymentRepository.save(payment);
        log.info("card information has been successfully updated");
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
            throw new BusinessException(ErrorMessages.DEBIT_CARD_NOT_VALID);
        }
        return new SuccessResult();
    }

    private Result isExistId(Long id) {
        if (!paymentRepository.existsById(id)) {
            log.warn("Payment id could not found!");
            throw new BusinessException(ErrorMessages.ID_NOT_FOUND);
        }
        return new SuccessResult();
    }

}
