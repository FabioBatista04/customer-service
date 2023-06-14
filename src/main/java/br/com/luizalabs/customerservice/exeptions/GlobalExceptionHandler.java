package br.com.luizalabs.customerservice.exeptions;

import br.com.luizalabs.customerservice.impl.model.ErrorEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.MethodNotAllowedException;
import reactor.netty.internal.util.MapUtils;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static br.com.luizalabs.customerservice.impl.model.ErrorEnum.INTERNAL_SERVER;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler({GenericException.class, Exception.class})
    public ResponseEntity<ExceptionResponse> handleException(Exception ex, ServerHttpRequest request) {

        if( ex instanceof GenericException gEx)
            return ResponseEntity.status(gEx.getErrorEnum().getStatus()).body( buildException(
                    gEx.getErrorEnum(), gEx.getMessage(), gEx.getFields(), request.getPath().value()));

        return ResponseEntity.status(500).body(
                buildException(INTERNAL_SERVER, ex.getCause().getMessage(),null,request.getPath().value()));
    }
    private ExceptionResponse buildException (ErrorEnum error, String message, Map<String,String> fields, String path
    ){
        return ExceptionResponse.builder()
                .status(error.getStatus())
                .error(error.getError())
                .message(message)
                .fields(fields)
                .path(path)
                .timestamp(OffsetDateTime.now())
                .build();
    }

}
