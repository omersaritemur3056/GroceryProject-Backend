package com.example.grocery.service.implement;

import java.util.List;

import com.example.grocery.service.constants.Messages;
import com.example.grocery.service.rules.ProducerBusinessRules;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.grocery.service.interfaces.ProducerService;
import com.example.grocery.core.utilities.business.BusinessRules;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.mapper.MapperService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.core.utilities.results.SuccessDataResult;
import com.example.grocery.core.utilities.results.SuccessResult;
import com.example.grocery.repository.ProducerRepository;
import com.example.grocery.model.concretes.Producer;
import com.example.grocery.api.requests.producer.CreateProducerRequest;
import com.example.grocery.api.requests.producer.DeleteProducerRequest;
import com.example.grocery.api.requests.producer.UpdateProducerRequest;
import com.example.grocery.api.responses.producer.GetAllProducerResponse;
import com.example.grocery.api.responses.producer.GetByIdProducerResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class ProducerServiceImpl implements ProducerService {

    private final ProducerRepository producerRepository;
    private final MapperService mapperService;
    private final ProducerBusinessRules producerBusinessRules;

    @Override
    public Result add(CreateProducerRequest createProducerRequest) {

        Result rules = BusinessRules.run(producerBusinessRules.isExistName(createProducerRequest.getName()));
        if (!rules.isSuccess())
            return rules;

        Producer producer = mapperService.forRequest().map(createProducerRequest, Producer.class);
        producerRepository.save(producer);
        log.info(Messages.LogMessages.LogInfoMessages.PRODUCER_ADDED, createProducerRequest.getName());
        return new SuccessResult(Messages.CreateMessages.PRODUCER_CREATED);
    }

    @Override
    public Result update(UpdateProducerRequest updateProducerRequest, Long id) {

        Result rules = BusinessRules.run(producerBusinessRules.isExistId(id),
                producerBusinessRules.isExistName(updateProducerRequest.getName()));
        if (!rules.isSuccess())
            return rules;

        var inDbProducer = producerRepository.findById(id)
                .orElseThrow(() -> new BusinessException(Messages.ErrorMessages.ID_NOT_FOUND));
        Producer producer = mapperService.forRequest().map(updateProducerRequest, Producer.class);
        producer.setId(inDbProducer.getId());
        producerRepository.save(producer);
        log.info(Messages.LogMessages.LogInfoMessages.PRODUCER_UPDATED, updateProducerRequest.getName());
        return new SuccessResult(Messages.UpdateMessages.PRODUCER_MODIFIED);
    }

    @Override
    public Result delete(DeleteProducerRequest deleteProducerRequest) {

        Result rules = BusinessRules.run(producerBusinessRules.isExistId(deleteProducerRequest.getId()));
        if (!rules.isSuccess())
            return rules;

        Producer producer = mapperService.forRequest().map(deleteProducerRequest, Producer.class);
        log.info(Messages.LogMessages.LogInfoMessages.PRODUCER_DELETED, getProducerById(deleteProducerRequest.getId()).getName());
        producerRepository.delete(producer);
        return new SuccessResult(Messages.DeleteMessages.PRODUCER_DELETED);
    }

    @Override
    public DataResult<List<GetAllProducerResponse>> getAll() {
        List<Producer> producers = producerRepository.findAll();
        List<GetAllProducerResponse> returnList = producers.stream()
                .map(p -> mapperService.forResponse().map(p, GetAllProducerResponse.class)).toList();
        return new SuccessDataResult<>(returnList, Messages.GetListMessages.PRODUCERS_LISTED);
    }

    @Override
    public DataResult<GetByIdProducerResponse> getById(Long id) {
        Producer producer = producerRepository.findById(id)
                .orElseThrow(() -> new BusinessException(Messages.ErrorMessages.ID_NOT_FOUND));
        GetByIdProducerResponse getByIdProducerResponse = mapperService.forResponse().map(producer,
                GetByIdProducerResponse.class);
        return new SuccessDataResult<>(getByIdProducerResponse, Messages.GetByIdMessages.PRODUCER_LISTED);
    }

    @Override
    public DataResult<List<GetAllProducerResponse>> getListBySorting(String sortBy) {
        producerBusinessRules.isValidSortParameter(sortBy);

        List<Producer> producers = producerRepository.findAll(Sort.by(Sort.Direction.ASC, sortBy));
        List<GetAllProducerResponse> returnList = producers.stream()
                .map(p -> mapperService.forResponse().map(p, GetAllProducerResponse.class)).toList();
        return new SuccessDataResult<>(returnList, Messages.GetListMessages.PRODUCERS_SORTED + sortBy);
    }

    @Override
    public DataResult<List<GetAllProducerResponse>> getListByPagination(int pageNo, int pageSize) {
        producerBusinessRules.isPageNumberValid(pageNo);
        producerBusinessRules.isPageSizeValid(pageSize);

        List<Producer> producers = producerRepository.findAll(PageRequest.of(pageNo, pageSize)).toList();
        List<GetAllProducerResponse> returnList = producers.stream()
                .map(p -> mapperService.forResponse().map(p, GetAllProducerResponse.class)).toList();
        return new SuccessDataResult<>(returnList, Messages.GetListMessages.PRODUCERS_PAGINATED);
    }

    @Override
    public DataResult<List<GetAllProducerResponse>> getListByPaginationAndSorting(int pageNo, int pageSize,
            String sortBy) {
        producerBusinessRules.isPageNumberValid(pageNo);
        producerBusinessRules.isPageSizeValid(pageSize);
        producerBusinessRules.isValidSortParameter(sortBy);

        List<Producer> producers = producerRepository
                .findAll(PageRequest.of(pageNo, pageSize).withSort(Sort.by(sortBy))).toList();
        List<GetAllProducerResponse> returnList = producers.stream()
                .map(p -> mapperService.forResponse().map(p, GetAllProducerResponse.class)).toList();
        return new SuccessDataResult<>(returnList, Messages.GetListMessages.PRODUCERS_PAGINATED_AND_SORTED + sortBy);
    }

    // ProductManager sınıfımızda bağımlılığı kontrol altına alma adına kullanılmak
    // üzere tasarlandı.
    @Override
    public Producer getProducerById(Long id) {
        return producerRepository.findById(id)
                .orElseThrow(() -> new BusinessException(Messages.ErrorMessages.PRODUCER_ID_NOT_FOUND));
    }

}
