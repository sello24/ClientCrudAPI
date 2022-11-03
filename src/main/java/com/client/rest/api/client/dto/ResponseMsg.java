package com.client.rest.api.client.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMsg {

    private HttpStatus httpStatus;
    private String msg;
}
