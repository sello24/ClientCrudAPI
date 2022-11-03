package com.client.rest.api.client.service;

import com.client.rest.api.client.exception.ErrorStatusEnum;
import com.client.rest.api.client.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Slf4j
@Service
public class ValidationService {

    public void validateIdNumber(final String idNumber, boolean exists) {
        if (exists) {
            throw new ValidationException(ErrorStatusEnum.ID_NUMBER_ALREADY_EXIST);
        }
        if (StringUtils.isBlank(idNumber)) {
            throw new ValidationException(ErrorStatusEnum.ID_MUST_NOT_BE_NULL);
        }

        if (idNumber.length() != 13) {
            throw new ValidationException(ErrorStatusEnum.ID_NUMBER_LIMIT);
        }

        try {
            Long.parseLong(idNumber);
        } catch (NumberFormatException e) {
            throw new ValidationException(ErrorStatusEnum.ID_NUMBER_IS_NOT_A_NUMBER);
        }

        isValidDate(idNumber.substring(0, 6));

        int country = Integer.parseInt(idNumber.substring(10, 11));

        if (country != 0) {
            throw new ValidationException(ErrorStatusEnum.NOT_SA_CITIZEN);
        }
    }

    private void isValidDate(final String strDate) {
        DateFormat sdf = new SimpleDateFormat("yyMMdd");
        sdf.setLenient(false);
        try {
            sdf.parse(strDate);
        } catch (ParseException e) {
            throw new ValidationException(ErrorStatusEnum.INVALID_DATE);
        }
    }

    public void validateFirstName(final String firstName) {
        if (StringUtils.isBlank(firstName)) {
            throw new ValidationException(ErrorStatusEnum.FIRSTNAME_MUST_NOT_BE_NULL);
        }
    }

    public void validateLastName(final String lastName) {
        if (StringUtils.isBlank(lastName)) {
            throw new ValidationException(ErrorStatusEnum.LASTNAME_MUST_NOT_BE_NULL);
        }
    }

    public void checkMobileNumberDuplicate(boolean exists) {
        if (exists) {
            throw new ValidationException(ErrorStatusEnum.MOBILE_NUMBER_ALREADY_EXIST);
        }
    }
}
