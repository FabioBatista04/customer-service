package br.com.luizalabs.customerservice.impl.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorEnum {
    NOT_FOUND(404,"Not Found"),
    BAD_REQUEST(400,"Bad Request"),
    UNAUTHORIZED(401,"Unauthorized"),
    INTERNAL_SERVER(500,"Internal Server Error");

    private final int status;
    private final String error;
}
