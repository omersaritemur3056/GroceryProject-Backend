package com.example.grocery.business.abstracts;

import java.util.List;

import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.entity.concretes.Producer;
import com.example.grocery.webApi.requests.producer.CreateProducerRequest;
import com.example.grocery.webApi.requests.producer.DeleteProducerRequest;
import com.example.grocery.webApi.requests.producer.UpdateProducerRequest;
import com.example.grocery.webApi.responses.producer.GetAllProducerResponse;
import com.example.grocery.webApi.responses.producer.GetByIdProducerResponse;

public interface ProducerService {

    Result add(CreateProducerRequest createProducerRequest);

    Result update(UpdateProducerRequest updateProducerRequest, Long id);

    Result delete(DeleteProducerRequest deleteProducerRequest);

    DataResult<List<GetAllProducerResponse>> getAll();

    DataResult<GetByIdProducerResponse> getById(Long id);

    DataResult<List<GetAllProducerResponse>> getListBySorting(String sortBy);

    DataResult<List<GetAllProducerResponse>> getListByPagination(int pageNo, int pageSize);

    DataResult<List<GetAllProducerResponse>> getListByPaginationAndSorting(int pageNo, int pageSize, String sortBy);

    default Producer getProducerById(Long id) {
        return null;
    }
}
