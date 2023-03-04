package com.example.grocery.core.utilities.mapper;

import org.modelmapper.ModelMapper;

public interface MapperService {

    ModelMapper forResponse();

    ModelMapper forRequest();
}
