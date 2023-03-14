package com.example.grocery.service.abstracts;

import java.util.List;

import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.entity.concretes.Supplier;
import com.example.grocery.api.requests.supplier.CreateSupplierRequest;
import com.example.grocery.api.requests.supplier.DeleteSupplierRequest;
import com.example.grocery.api.requests.supplier.UpdateSupplierRequest;
import com.example.grocery.api.responses.supplier.GetAllSupplierResponse;
import com.example.grocery.api.responses.supplier.GetByIdSupplierResponse;

public interface SupplierService {

    Result add(CreateSupplierRequest createSupplierRequest);

    Result update(UpdateSupplierRequest updateSupplierRequest, Long id);

    Result delete(DeleteSupplierRequest deleteSupplierRequest);

    DataResult<List<GetAllSupplierResponse>> getAll();

    DataResult<GetByIdSupplierResponse> getById(Long id);

    DataResult<List<GetAllSupplierResponse>> getListBySorting(String sortBy);

    DataResult<List<GetAllSupplierResponse>> getListByPagination(int pageNo, int pageSize);

    DataResult<List<GetAllSupplierResponse>> getListByPaginationAndSorting(int pageNo, int pageSize, String sortBy);

    default Supplier getSupplierById(Long id) {
        return null;
    }
}
