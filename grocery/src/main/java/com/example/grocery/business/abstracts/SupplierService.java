package com.example.grocery.business.abstracts;

import java.util.List;

import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.entity.concretes.Supplier;
import com.example.grocery.webApi.requests.supplier.CreateSupplierRequest;
import com.example.grocery.webApi.requests.supplier.DeleteSupplierRequest;
import com.example.grocery.webApi.requests.supplier.UpdateSupplierRequest;
import com.example.grocery.webApi.responses.supplier.GetAllSupplierResponse;
import com.example.grocery.webApi.responses.supplier.GetByIdSupplierResponse;

public interface SupplierService {

    Result add(CreateSupplierRequest createSupplierRequest);

    Result update(UpdateSupplierRequest updateSupplierRequest, Long id);

    Result delete(DeleteSupplierRequest deleteSupplierRequest);

    DataResult<List<GetAllSupplierResponse>> getAll();

    DataResult<GetByIdSupplierResponse> getById(Long id);

    default Supplier getSupplierById(Long id) {
        return null;
    }
}
