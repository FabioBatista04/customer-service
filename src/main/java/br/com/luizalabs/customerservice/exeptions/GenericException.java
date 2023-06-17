package br.com.luizalabs.customerservice.exeptions;

import br.com.luizalabs.customerservice.impl.model.ErrorEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
@Getter
@Setter
public class GenericException extends RuntimeException{
    private final ErrorEnum errorEnum;
    private final Map<String, String> fields;

    public GenericException(ErrorEnum errorEnum, String message, Map<String, String> fields) {
        super(message);
        this.errorEnum = errorEnum;
        this.fields = fields;
    }

    public GenericException(ErrorEnum errorEnum, String message) {
        super(message);
        this.errorEnum = errorEnum;
        this.fields = Map.of();
    }
}
