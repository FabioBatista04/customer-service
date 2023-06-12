package br.com.luizalabs.customerservice.exeptions;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class NotFoundException extends RuntimeException {

    private Map<String, String> fields;

    public NotFoundException(String message, Map<String, String> fields) {
        super(message);
        this.fields = fields;
    }


}
