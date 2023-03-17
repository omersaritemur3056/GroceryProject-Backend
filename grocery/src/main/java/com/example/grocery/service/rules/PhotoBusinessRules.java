package com.example.grocery.service.rules;

import com.example.grocery.service.constants.Messages;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.model.concretes.Image;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

@Service
@Slf4j
public class PhotoBusinessRules {

    public void isFormatValid(MultipartFile file) {
        ArrayList<String> formats = new ArrayList<>();
        formats.add("png");
        formats.add("jpg");
        formats.add("jpeg");

        if (file.isEmpty() || Objects.isNull(file.getOriginalFilename())) {
            log.error(Messages.LogMessages.LogErrorMessages.FILE_IS_NULL);
            throw new BusinessException(Messages.ErrorMessages.FILE_IS_NULL);
        }

        String imageName = file.getOriginalFilename().toLowerCase(Locale.ENGLISH);

        for (String format : formats) {
            if (imageName.contains(format)) {
                return;
            }
        }
        log.warn(Messages.LogMessages.LogWarnMessages.UNSUPPORTED_FORMAT);
        throw new BusinessException(Messages.ErrorMessages.UNSUPPORTED_FORMAT);
    }

    public void isPageNumberValid(int pageNo) {
        if (pageNo < 0) {
            log.warn(Messages.LogMessages.LogWarnMessages.PAGE_NUMBER_NEGATIVE);
            throw new BusinessException(Messages.ErrorMessages.PAGE_NUMBER_NEGATIVE);
        }
    }

    public void isPageSizeValid(int pageSize) {
        if (pageSize < 1) {
            log.warn(Messages.LogMessages.LogWarnMessages.PAGE_SIZE_NEGATIVE);
            throw new BusinessException(Messages.ErrorMessages.PAGE_SIZE_NEGATIVE);
        }
    }

    public void isValidSortParameter(String sortBy) {
        Image checkField = new Image();
        if (!checkField.toString().contains(sortBy)) {
            log.warn(Messages.LogMessages.LogWarnMessages.SORT_PARAMETER_NOT_VALID);
            throw new BusinessException(Messages.ErrorMessages.SORT_PARAMETER_NOT_VALID);
        }
    }
}
