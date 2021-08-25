package com.nti.socialmediaappcore.config;

import com.nti.socialmediaappcore.exception.DuplicateException;
import com.nti.socialmediaappcore.exception.ExistingUserException;
import com.nti.socialmediaappcore.exception.NotFoundException;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        List<String> messages = List.of(ex.getMessage());
        Map<String, Object> body = new LinkedHashMap<>(createBody(status, messages));
        body.put("description", "uri=" + request.getRequestURI());

        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(value = DuplicateException.class)
    public ResponseEntity<Object> handleDuplicateException(DuplicateException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        List<String> messages = List.of(ex.getMessage());
        Map<String, Object> body = new LinkedHashMap<>(createBody(status, messages));
        body.put("description", "uri=" + request.getRequestURI());

        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(value = ExistingUserException.class)
    public ResponseEntity<Object> handleExistingUserException(ExistingUserException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        List<String> messages = List.of(ex.getMessage());
        Map<String, Object> body = new LinkedHashMap<>(createBody(status, messages));
        body.put("description", "uri=" + request.getRequestURI());

        return new ResponseEntity<>(body, status);
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatus status,
            @NonNull WebRequest request) {

        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        Map<String, Object> body = new LinkedHashMap<>(createBody(status, errors));
        body.put("description", request.getDescription(false));

        return new ResponseEntity<>(body, headers, status);
    }

    private Map<String, Object> createBody(HttpStatus status, List<String> errors) {
        return Map.of("timestamp", Timestamp.valueOf(LocalDateTime.now()),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", errors);
    }
}
