package com.example.grocery.business.concretes;

import java.util.ArrayList;
import java.util.List;
import com.example.grocery.business.rules.PhotoBusinessRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.grocery.business.abstracts.PhotoService;
import com.example.grocery.business.constants.Messages.CreateMessages;
import com.example.grocery.business.constants.Messages.DeleteMessages;
import com.example.grocery.business.constants.Messages.ErrorMessages;
import com.example.grocery.business.constants.Messages.GetByIdMessages;
import com.example.grocery.business.constants.Messages.GetByUrlMessages;
import com.example.grocery.business.constants.Messages.GetListMessages;
import com.example.grocery.business.constants.Messages.UpdateMessages;
import com.example.grocery.business.constants.Messages.LogMessages.LogInfoMessages;
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
import com.example.grocery.webApi.responses.image.GetByUrlImageResponse;

import jakarta.transaction.Transactional;
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
    @Autowired
    private PhotoBusinessRules photoBusinessRules;

    @Override
    @Transactional
    public DataResult<Object> upload(MultipartFile file) {
        photoBusinessRules.isFormatValid(file);

        var result = imageService.save(file);
        Image image = mapperService.forRequest().map(result.getData(), Image.class);
        imageRepository.save(image);
        log.info(LogInfoMessages.SAVED_IMAGE_URL, image.getUrl());
        return new SuccessDataResult<>(image.getUrl(), CreateMessages.IMAGE_UPLOADED_AND_ADDED);
    }

    @Override
    @Transactional
    public Result delete(String imageUrl) {
        imageService.delete(imageUrl);
        Image image = imageRepository.findByUrl(imageUrl)
                .orElseThrow(() -> new BusinessException(ErrorMessages.IMAGE_URL_NOT_FOUND));
        log.info(LogInfoMessages.DELETED_IMAGE_URL, image.getUrl());
        imageRepository.delete(image);
        return new SuccessResult(DeleteMessages.IMAGE_DELETED);
    }

    @Override
    @Transactional
    public DataResult<Object> update(Long id, MultipartFile file) {
        photoBusinessRules.isFormatValid(file);

        Image inDbImage = imageRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));

        delete(inDbImage.getUrl());

        var result = imageService.save(file);
        Image image = mapperService.forRequest().map(result.getData(), Image.class);
        image.setId(inDbImage.getId());
        imageRepository.save(image);
        log.info(LogInfoMessages.UPDATED_IMAGE_URL, image.getUrl());
        return new SuccessDataResult<>(image.getUrl(), UpdateMessages.IMAGE_UPDATED_AND_ADDED);
    }

    @Override
    public DataResult<List<GetAllImageResponse>> getAll() {
        List<Image> images = imageRepository.findAll();
        List<GetAllImageResponse> responseList = images.stream()
                .map(i -> mapperService.forResponse().map(i, GetAllImageResponse.class)).toList();
        return new SuccessDataResult<>(responseList, GetListMessages.IMAGES_LISTED);
    }

    @Override
    public DataResult<GetByIdImageResponse> getById(Long id) {
        Image image = imageRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
        GetByIdImageResponse response = mapperService.forResponse().map(image, GetByIdImageResponse.class);
        return new SuccessDataResult<>(response, GetByIdMessages.IMAGE_LISTED);
    }

    @Override
    public DataResult<GetByUrlImageResponse> getByUrl(String imageUrl) {
        Image image = imageRepository.findByUrl(imageUrl)
                .orElseThrow(() -> new BusinessException(ErrorMessages.IMAGE_URL_NOT_FOUND));
        GetByUrlImageResponse response = mapperService.forResponse().map(image, GetByUrlImageResponse.class);
        return new SuccessDataResult<>(response, GetByUrlMessages.IMAGE_LISTED);
    }

    @Override
    public DataResult<List<GetAllImageResponse>> getListBySorting(String sortBy) {
        photoBusinessRules.isValidSortParameter(sortBy);

        List<Image> images = imageRepository.findAll(Sort.by(Sort.Direction.ASC, sortBy));
        List<GetAllImageResponse> responseList = images.stream()
                .map(i -> mapperService.forResponse().map(i, GetAllImageResponse.class)).toList();
        return new SuccessDataResult<>(responseList, GetListMessages.IMAGES_SORTED + sortBy);
    }

    @Override
    public DataResult<List<GetAllImageResponse>> getListByPagination(int pageNo, int pageSize) {
        photoBusinessRules.isPageNumberValid(pageNo);
        photoBusinessRules.isPageSizeValid(pageSize);

        List<Image> images = imageRepository.findAll(PageRequest.of(pageNo, pageSize)).toList();
        List<GetAllImageResponse> responseList = images.stream()
                .map(i -> mapperService.forResponse().map(i, GetAllImageResponse.class)).toList();
        return new SuccessDataResult<>(responseList, GetListMessages.IMAGES_PAGINATED);
    }

    @Override
    public DataResult<List<GetAllImageResponse>> getListByPaginationAndSorting(int pageNo, int pageSize,
            String sortBy) {
        photoBusinessRules.isPageNumberValid(pageNo);
        photoBusinessRules.isPageSizeValid(pageSize);
        photoBusinessRules.isValidSortParameter(sortBy);

        List<Image> images = imageRepository.findAll(PageRequest.of(pageNo, pageSize).withSort(Sort.by(sortBy)))
                .toList();
        List<GetAllImageResponse> responseList = images.stream()
                .map(i -> mapperService.forResponse().map(i, GetAllImageResponse.class)).toList();
        return new SuccessDataResult<>(responseList, GetListMessages.IMAGES_PAGINATED_AND_SORTED + sortBy);
    }

    // bağımlılığın kontrol altına alınması adına tasarlandı
    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorMessages.IMAGE_ID_NOT_FOUND));
    }

    @Override
    public List<Image> getImagesByIds(Long[] imageIds) {
        List<Image> resultList = new ArrayList<>();
        if (imageIds == null) {
            return resultList;
        }
        for (Long forEachId : imageIds) {
            Image findImageById = imageRepository.findById(forEachId)
                    .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
            resultList.add(findImageById);
        }
        return resultList;
    }

    @Override
    public List<Image> getImagesByUrls(String[] imageUrls) {
        List<Image> resultList = new ArrayList<>();
        String x = imageUrls.toString();
        if (imageUrls == null || x.contains("")) {
            return resultList;
        }
        for (String forEachUrl : imageUrls) {
            Image findImageByUrl = imageRepository.findByUrl(forEachUrl)
                    .orElseThrow(() -> new BusinessException(ErrorMessages.IMAGE_URL_NOT_FOUND));
            resultList.add(findImageByUrl);
        }
        return resultList;
    }

}
