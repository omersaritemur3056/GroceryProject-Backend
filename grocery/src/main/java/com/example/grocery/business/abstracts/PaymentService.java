package com.example.grocery.business.abstracts;

import java.util.List;

import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.entity.concretes.Payment;
import com.example.grocery.webApi.requests.payment.CreatePaymentRequest;
import com.example.grocery.webApi.requests.payment.DeletePaymentRequest;
import com.example.grocery.webApi.requests.payment.UpdatePaymentRequest;
import com.example.grocery.webApi.responses.payment.GetAllPaymentResponse;
import com.example.grocery.webApi.responses.payment.GetByIdPaymentResponse;

public interface PaymentService {

    Result add(CreatePaymentRequest createPaymentRequest);

    Result delete(DeletePaymentRequest deletePaymentRequest);

    Result update(UpdatePaymentRequest updatePaymentRequest, Long id);

    DataResult<List<GetAllPaymentResponse>> getAll();

    DataResult<GetByIdPaymentResponse> getById(Long id);

    Payment getPaymentById(Long id);
}
