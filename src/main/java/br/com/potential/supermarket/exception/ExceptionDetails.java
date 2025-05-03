package br.com.potential.supermarket.exception;

import lombok.Data;

@Data
public class ExceptionDetails {

    private Integer status;
    private String message;

}
