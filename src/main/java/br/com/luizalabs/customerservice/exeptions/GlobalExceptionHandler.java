package br.com.luizalabs.customerservice.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.MethodNotAllowedException;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionResponse handleException(MethodArgumentNotValidException ex, ServerHttpRequest request) {
        HashMap<String, String> fields = new HashMap<>();
        ex.getFieldErrors().forEach(field ->
                fields.put(field.toString(),
                        Objects.requireNonNullElse(field.getRejectedValue(), "").toString()));
        return ExceptionResponse.builder()
                .status(404)
                .error("Not Found")
                .message(ex.getMessage())
                .fields(fields)
                .path(request.getPath().value())
                .timestamp(OffsetDateTime.now())
                .build();
    }

    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ExceptionResponse handleException(BadRequestException ex, ServerHttpRequest request) {
        return ExceptionResponse.builder()
                .status(400)
                .error("Bad request")
                .message(ex.getMessage())
                .fields(ex.getFields())
                .path(request.getPath().value())
                .timestamp(OffsetDateTime.now())
                .build();
    }

    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(WebExchangeBindException.class)
    public ExceptionResponse handleException(WebExchangeBindException ex, ServerHttpRequest request) {
        HashMap<String, String> fields = new HashMap<>();
        ex.getFieldErrors().forEach(field ->
                fields.put(field.toString(),
                        Objects.requireNonNullElse(field.getRejectedValue(), "").toString()));

        return ExceptionResponse.builder()
                .status(ex.getStatusCode().value())
                .error("Bad Request")
                .message(ex.getMessage())
                .fields(fields)
                .path(request.getPath().value())
                .timestamp(OffsetDateTime.now())
                .build();
    }

    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodNotAllowedException.class)
    public ExceptionResponse handleException(MethodNotAllowedException ex, ServerHttpRequest request) {
        HashMap<String, String> fields = new HashMap<>();
        return ExceptionResponse.builder()
                .status(405)
                .error("Bad Request")
                .message(ex.getMessage())
                .fields(fields)
                .path(request.getPath().value())
                .timestamp(OffsetDateTime.now())
                .build();
    }
    @ResponseBody
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ExceptionResponse handleException(NotFoundException ex, ServerHttpRequest request) {
        HashMap<String, String> fields = new HashMap<>();
        return ExceptionResponse.builder()
                .status(404)
                .error("Not Found")
                .message(ex.getMessage())
                .fields(fields)
                .path(request.getPath().value())
                .timestamp(OffsetDateTime.now())
                .build();
    }

    @ResponseBody
    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ExceptionResponse handleException(ServerHttpRequest request) {
        return ExceptionResponse.builder()
                .status(401)
                .error("unauthorized")
                .path(request.getPath().value())
                .timestamp(OffsetDateTime.now())
                .build();
    }
}
