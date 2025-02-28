package br.com.potential.inventory.exception;

import lombok.Data;
import lombok.Getter;

@Data
public class ExceptionDetails {

    private Integer status;
    private String message;

}
