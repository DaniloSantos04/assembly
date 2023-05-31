package br.com.assembly.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomException extends Exception {
    private int httpStatus;

    public CustomException(String mensage, int httpStatus) {
        super(mensage);
        this.httpStatus = httpStatus;
    }
}
