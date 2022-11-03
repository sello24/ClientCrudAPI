package com.client.rest.api.client.exception;

public class ValidationException extends RuntimeException{

    public ValidationException(ErrorStatusEnum errorStatusEnum){
        super(errorStatusEnum.status);
    }
}
