package com.example.grocery.core.utilities.image;

import org.springframework.web.multipart.MultipartFile;

import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;

public interface ImageService {

    DataResult<Object> save(MultipartFile file);

    Result delete(String imageUrl);

}
