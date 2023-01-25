package com.example.grocery.outservices.cloudinary;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.grocery.core.utilities.image.ImageModel;
import com.example.grocery.core.utilities.image.ImageService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.ErrorDataResult;
import com.example.grocery.core.utilities.results.ErrorResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.core.utilities.results.SuccessDataResult;
import com.example.grocery.core.utilities.results.SuccessResult;

@Service
public class CloudinaryImageAdapter implements ImageService {

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public DataResult<Object> save(MultipartFile file) {
        Map<?, ?> result;

        try {
            result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        } catch (IOException e) {
            return new ErrorDataResult<>(e.getMessage());
        }

        ImageModel image = ImageModel.builder()
                .publicId(String.valueOf(result.get("public_id")))
                .url(String.valueOf(result.get("url")))
                .bytes(Integer.parseInt(String.valueOf(result.get("bytes"))))
                .format(String.valueOf(result.get("format")))
                .height(Integer.parseInt(String.valueOf(result.get("height"))))
                .width(Integer.parseInt(String.valueOf(result.get("width"))))
                .build();

        return new SuccessDataResult<>(image);
    }

    @Override
    public Result delete(String imageUrl) {
        try {
            cloudinary.uploader().destroy(CloudinaryImageHelper.getImagePublicIdFromUrl(imageUrl),
                    ObjectUtils.emptyMap());
        } catch (IOException e) {
            return new ErrorResult(e.getMessage());
        }

        return new SuccessResult();
    }
}
