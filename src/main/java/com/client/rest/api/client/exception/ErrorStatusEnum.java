package com.client.rest.api.client.exception;

public enum ErrorStatusEnum {
    INVALID_DATE("Date in ID is invalid"),
    ID_NUMBER_ALREADY_EXIST("ID number already exists"),
    ID_NUMBER_LIMIT("ID number exceeds required length"),
    ID_NUMBER_IS_NOT_A_NUMBER("ID number is not a number"),
    NOT_SA_CITIZEN("Not SA citizen"),
    ID_MUST_NOT_BE_NULL("ID number is required"),
    FIRSTNAME_MUST_NOT_BE_NULL("First name is required"),
    LASTNAME_MUST_NOT_BE_NULL("Last name is required"),
    MOBILE_NUMBER_ALREADY_EXIST("Mobile number already exists"),
    CLIENT_NOT_FOUND("Client not found");

    public String status;

    private ErrorStatusEnum(String status) {
        this.status = status;
    }
}
