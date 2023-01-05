package com.example.grocery.business.concretes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.grocery.business.abstracts.ProducerService;
import com.example.grocery.core.utilities.business.BusinessRules;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.mapper.MapperService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.core.utilities.results.SuccessDataResult;
import com.example.grocery.core.utilities.results.SuccessResult;
import com.example.grocery.dataAccess.abstracts.ProducerRepository;
import com.example.grocery.entity.concretes.Producer;
import com.example.grocery.webApi.requests.producer.CreateProducerRequest;
import com.example.grocery.webApi.requests.producer.DeleteProducerRequest;
import com.example.grocery.webApi.requests.producer.UpdateProducerRequest;
import com.example.grocery.webApi.responses.producer.GetAllProducerResponse;
import com.example.grocery.webApi.responses.producer.GetByIdProducerResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProducerManager implements ProducerService {

    @Autowired
    private ProducerRepository producerRepository;
    @Autowired
    private MapperService mapperService;

    @Override
    public Result add(CreateProducerRequest createProducerRequest) {

        Result rules = BusinessRules.run(isExistName(createProducerRequest.getName()));

        Producer producer = mapperService.getModelMapper().map(createProducerRequest, Producer.class);
        producerRepository.save(producer);
        log.info("succeed producer : {} logged to file!", createProducerRequest.getName());
        return new SuccessResult("Producer added");
    }

    @Override
    public Result update(UpdateProducerRequest updateProducerRequest, int id) {

        Result rules = BusinessRules.run(isExistId(id), isExistName(updateProducerRequest.getName()));

        var inDbProducer = producerRepository.findById(id).orElseThrow(() -> new BusinessException("Id not found!"));
        Producer producer = mapperService.getModelMapper().map(updateProducerRequest, Producer.class);
        producer.setId(inDbProducer.getId());
        producerRepository.save(producer);
        log.info("modified producer : {} logged to file!", updateProducerRequest.getName());
        return new SuccessResult("Producer has been modified");
    }

    @Override
    public Result delete(DeleteProducerRequest deleteProducerRequest) {

        Result rules = BusinessRules.run(isExistId(deleteProducerRequest.getId()));

        Producer producer = mapperService.getModelMapper().map(deleteProducerRequest, Producer.class);
        log.info("removed producer: {} logged to file!", getProducerById(deleteProducerRequest.getId()).getName());
        producerRepository.delete(producer);
        return new SuccessResult("Producer removed form DB");
    }

    @Override
    public DataResult<List<GetAllProducerResponse>> getAll() {
        List<Producer> producers = producerRepository.findAll();
        List<GetAllProducerResponse> returnList = producers.stream()
                .map(p -> mapperService.getModelMapper().map(p, GetAllProducerResponse.class)).toList();
        return new SuccessDataResult<>(returnList, "Producers listed");
    }

    @Override
    public DataResult<GetByIdProducerResponse> getById(int id) {
        Producer producer = producerRepository.findById(id).orElseThrow(() -> new BusinessException("Id not found!"));
        GetByIdProducerResponse getByIdProducerResponse = mapperService.getModelMapper().map(producer,
                GetByIdProducerResponse.class);
        return new SuccessDataResult<>(getByIdProducerResponse, "Producer is found by id");
    }

    // ProductManager sınıfımızda bağımlılığı kontrol altına alma adına kullanılmak
    // üzere tasarlandı.
    public Producer getProducerById(int id) {
        return producerRepository.findById(id).orElseThrow(() -> new BusinessException("Producer id not found!"));
    }

    private Result isExistId(int id) {
        if (!producerRepository.existsById(id)) {
            throw new BusinessException("Id was not found on DB!");
        }
        return new SuccessResult();
    }

    private Result isExistName(String name) {
        if (producerRepository.existsByName(name)) {
            throw new BusinessException("Producer name can not be repeated!");
        }
        return new SuccessResult();
    }

}
