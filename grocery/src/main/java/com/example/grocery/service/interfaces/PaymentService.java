package com.example.grocery.service.abstracts;

import java.util.List;

import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.entity.concretes.Payment;
import com.example.grocery.api.requests.payment.CreatePaymentRequest;
import com.example.grocery.api.requests.payment.DeletePaymentRequest;
import com.example.grocery.api.requests.payment.UpdatePaymentRequest;
import com.example.grocery.api.responses.payment.GetAllPaymentResponse;
import com.example.grocery.api.responses.payment.GetByIdPaymentResponse;

public interface PaymentService {

    Result add(CreatePaymentRequest createPaymentRequest);

    Result delete(DeletePaymentRequest deletePaymentRequest);

    Result update(UpdatePaymentRequest updatePaymentRequest, Long id);

    DataResult<List<GetAllPaymentResponse>> getAll();

    DataResult<GetByIdPaymentResponse> getById(Long id);

    DataResult<List<GetAllPaymentResponse>> getListBySorting(String sortBy);

    DataResult<List<GetAllPaymentResponse>> getListByPagination(int pageNo, int pageSize);

    DataResult<List<GetAllPaymentResponse>> getListByPaginationAndSorting(int pageNo, int pageSize, String sortBy);

    Payment getPaymentById(Long id);
}
