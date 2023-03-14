package com.example.grocery.service.implement;

import com.example.grocery.service.constants.Messages;
import com.example.grocery.service.interfaces.PaymentService;
import com.example.grocery.service.rules.PaymentBusinessRules;
import com.example.grocery.core.utilities.business.BusinessRules;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.mapper.MapperService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.core.utilities.results.SuccessDataResult;
import com.example.grocery.core.utilities.results.SuccessResult;
import com.example.grocery.repository.PaymentRepository;
import com.example.grocery.model.concretes.Payment;
import com.example.grocery.api.requests.payment.CreatePaymentRequest;
import com.example.grocery.api.requests.payment.DeletePaymentRequest;
import com.example.grocery.api.requests.payment.UpdatePaymentRequest;
import com.example.grocery.api.responses.payment.GetAllPaymentResponse;
import com.example.grocery.api.responses.payment.GetByIdPaymentResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

        private PaymentRepository paymentRepository;
        private MapperService mapperService;
        private PaymentBusinessRules paymentBusinessRules;

        @Override
        @Transactional
        public Result add(CreatePaymentRequest createPaymentRequest) {
                Result rules = BusinessRules
                                .run(paymentBusinessRules.isValidCard(createPaymentRequest.getCardNumber(),
                                                createPaymentRequest.getFullName(),
                                                createPaymentRequest.getCardExpirationYear(),
                                                createPaymentRequest.getCardExpirationMonth(),
                                                createPaymentRequest.getCardCvv()));
                if (!rules.isSuccess())
                        return rules;

                Payment payment = mapperService.forRequest().map(createPaymentRequest, Payment.class);
                payment.setFullName(createPaymentRequest.getFullName().toUpperCase());
                paymentRepository.save(payment);
                log.info(Messages.LogMessages.LogInfoMessages.PAYMENT_CREATED, createPaymentRequest.getCardNumber(),
                                createPaymentRequest.getFullName(), createPaymentRequest.getCardExpirationYear(),
                                createPaymentRequest.getCardExpirationMonth(), createPaymentRequest.getCardCvv());
                return new SuccessResult(Messages.CreateMessages.PAYMENT_CREATED);
        }

        @Override
        @Transactional
        public Result delete(DeletePaymentRequest deletePaymentRequest) {

                Result rules = BusinessRules.run(paymentBusinessRules.isExistId(deletePaymentRequest.getId()));
                if (!rules.isSuccess())
                        return rules;

                Payment payment = mapperService.forRequest().map(deletePaymentRequest, Payment.class);
                log.info(Messages.LogMessages.LogInfoMessages.PAYMENT_DELETED, deletePaymentRequest.getId());
                paymentRepository.delete(payment);
                return new SuccessResult(Messages.DeleteMessages.PAYMENT_DELETED);
        }

        @Override
        @Transactional
        public Result update(UpdatePaymentRequest updatePaymentRequest, Long id) {
                Payment inDbPayment = paymentRepository.findById(id)
                                .orElseThrow(() -> new BusinessException(Messages.ErrorMessages.ID_NOT_FOUND));

                Result rules = BusinessRules.run(paymentBusinessRules.isValidCard(updatePaymentRequest.getCardNumber(),
                                updatePaymentRequest.getFullName(), updatePaymentRequest.getCardExpirationYear(),
                                updatePaymentRequest.getCardExpirationMonth(), updatePaymentRequest.getCardCvv()),
                                paymentBusinessRules.isExistId(id));
                if (!rules.isSuccess())
                        return rules;

                Payment payment = mapperService.forRequest().map(updatePaymentRequest, Payment.class);
                payment.setId(inDbPayment.getId());
                payment.setFullName(updatePaymentRequest.getFullName().toUpperCase());
                paymentRepository.save(payment);
                log.info(Messages.LogMessages.LogInfoMessages.PAYMENT_UPDATED, id, updatePaymentRequest.getCardNumber(),
                                updatePaymentRequest.getFullName(), updatePaymentRequest.getCardExpirationYear(),
                                updatePaymentRequest.getCardExpirationMonth(), updatePaymentRequest.getCardCvv());
                return new SuccessResult(Messages.UpdateMessages.PAYMENT_UPDATED);
        }

        @Override
        public DataResult<List<GetAllPaymentResponse>> getAll() {
                List<Payment> payments = paymentRepository.findAll();
                List<GetAllPaymentResponse> returnList = payments.stream()
                                .map(c -> mapperService.forResponse().map(c, GetAllPaymentResponse.class)).toList();

                return new SuccessDataResult<>(returnList, Messages.GetListMessages.PAYMENTS_LISTED);
        }

        @Override
        public DataResult<GetByIdPaymentResponse> getById(Long id) {
                Payment payment = paymentRepository.findById(id)
                                .orElseThrow(() -> new BusinessException(Messages.ErrorMessages.ID_NOT_FOUND));
                GetByIdPaymentResponse getByIdPaymentResponse = mapperService.forResponse().map(payment,
                                GetByIdPaymentResponse.class);
                return new SuccessDataResult<>(getByIdPaymentResponse, Messages.GetByIdMessages.PAYMENT_LISTED);
        }

        @Override
        public DataResult<List<GetAllPaymentResponse>> getListBySorting(String sortBy) {
                paymentBusinessRules.isValidSortParameter(sortBy);

                List<Payment> payments = paymentRepository.findAll(Sort.by(Sort.Direction.ASC, sortBy));
                List<GetAllPaymentResponse> returnList = payments.stream()
                                .map(c -> mapperService.forResponse().map(c, GetAllPaymentResponse.class)).toList();

                return new SuccessDataResult<>(returnList, Messages.GetListMessages.PAYMENTS_SORTED + sortBy);
        }

        @Override
        public DataResult<List<GetAllPaymentResponse>> getListByPagination(int pageNo, int pageSize) {
                paymentBusinessRules.isPageNumberValid(pageNo);
                paymentBusinessRules.isPageSizeValid(pageSize);

                List<Payment> payments = paymentRepository.findAll(PageRequest.of(pageNo, pageSize)).toList();
                List<GetAllPaymentResponse> returnList = payments.stream()
                                .map(c -> mapperService.forResponse().map(c, GetAllPaymentResponse.class)).toList();

                return new SuccessDataResult<>(returnList, Messages.GetListMessages.PAYMENTS_PAGINATED);
        }

        @Override
        public DataResult<List<GetAllPaymentResponse>> getListByPaginationAndSorting(int pageNo, int pageSize,
                        String sortBy) {
                paymentBusinessRules.isPageNumberValid(pageNo);
                paymentBusinessRules.isPageSizeValid(pageSize);
                paymentBusinessRules.isValidSortParameter(sortBy);

                List<Payment> payments = paymentRepository
                                .findAll(PageRequest.of(pageNo, pageSize).withSort(Sort.by(sortBy)))
                                .toList();
                List<GetAllPaymentResponse> returnList = payments.stream()
                                .map(c -> mapperService.forResponse().map(c, GetAllPaymentResponse.class)).toList();

                return new SuccessDataResult<>(returnList, Messages.GetListMessages.PAYMENTS_PAGINATED_AND_SORTED + sortBy);
        }

        // Bağımlılığın kontrol altına alınması için tasarlandı
        @Override
        public Payment getPaymentById(Long id) {
                return paymentRepository.findById(id)
                                .orElseThrow(() -> new BusinessException(Messages.ErrorMessages.PAYMENT_ID_NOT_FOUND));
        }

}
