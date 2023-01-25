package com.example.grocery.business.concretes;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.grocery.business.abstracts.PhotoService;
import com.example.grocery.business.constants.Messages.CreateMessages;
import com.example.grocery.business.constants.Messages.DeleteMessages;
import com.example.grocery.business.constants.Messages.ErrorMessages;
import com.example.grocery.business.constants.Messages.GetByIdMessages;
import com.example.grocery.business.constants.Messages.GetListMessages;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.image.ImageService;
import com.example.grocery.core.utilities.mapper.MapperService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.core.utilities.results.SuccessDataResult;
import com.example.grocery.core.utilities.results.SuccessResult;
import com.example.grocery.dataAccess.abstracts.ImageRepository;
import com.example.grocery.entity.concretes.Image;
import com.example.grocery.webApi.responses.image.GetAllImageResponse;
import com.example.grocery.webApi.responses.image.GetByIdImageResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PhotoManager implements PhotoService {

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private MapperService mapperService;

    @Override
    public DataResult<?> upload(MultipartFile file) {
        // resim formatı için iş kuralı getirilebilir...
        var result = imageService.save(file);
        Image image = mapperService.getModelMapper().map(result.getData(), Image.class);
        imageRepository.save(image);
        log.info("saved image url: {}", image.getUrl());
        return new SuccessDataResult<>(image.getUrl(), CreateMessages.IMAGE_UPLOADED_AND_ADDED);
    }

    @Override
    public Result delete(String imageUrl) {
        imageService.delete(imageUrl);
        Image image = imageRepository.findByUrl(imageUrl)
                .orElseThrow(() -> new BusinessException(ErrorMessages.IMAGE_URL_NOT_FOUND));
        log.info("delete image url: {}", image.getUrl());
        imageRepository.delete(image);
        return new SuccessResult(DeleteMessages.IMAGE_DELETED);
    }

    @Override
    public DataResult<List<GetAllImageResponse>> getAll() {
        List<Image> images = imageRepository.findAll();
        List<GetAllImageResponse> responseList = images.stream()
                .map(i -> mapperService.getModelMapper().map(i, GetAllImageResponse.class)).toList();
        return new SuccessDataResult<>(responseList, GetListMessages.IMAGES_LISTED);
    }

    @Override
    public DataResult<GetByIdImageResponse> getById(Long id) {
        Image image = imageRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
        GetByIdImageResponse response = mapperService.getModelMapper().map(image, GetByIdImageResponse.class);
        return new SuccessDataResult<>(response, GetByIdMessages.IMAGE_LISTED);
    }

    // bağımlılığın kontrol altına alınması adına tasarlandı
    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorMessages.IMAGE_ID_NOT_FOUND));
    }

    @Override
    public List<Image> getImagesByIds(Long[] imageId) {
        List<Image> resultList = new ArrayList<>();
        for (Long forEachId : imageId) {
            Image findImageById = imageRepository.findById(forEachId)
                    .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
            resultList.add(findImageById);
        }
        return resultList;
    }

}
