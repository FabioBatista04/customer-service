package br.com.luizalabs.customerservice.exeptions;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
@Getter
@Setter
public class GenericException extends RuntimeException{
    private int status;
    private HashMap<String, String> fields;

    public GenericException(int status, String message, HashMap<String, String> fields) {
        super(message);
        this.status = status;
        this.fields = fields;
    }
}
