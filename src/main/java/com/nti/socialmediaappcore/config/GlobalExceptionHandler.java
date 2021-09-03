package com.nti.socialmediaappcore.config;

import com.nti.socialmediaappcore.exception.*;

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
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex,
                                                          HttpServletRequest request) {
        return handleCustomException(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = DuplicateException.class)
    public ResponseEntity<Object> handleDuplicateException(DuplicateException ex,
                                                           HttpServletRequest request) {
        return handleCustomException(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = UserAlreadyRegistered.class)
    public ResponseEntity<Object> handleUserAlreadyRegistered(UserAlreadyRegistered ex,
                                                              HttpServletRequest request) {
        return handleCustomException(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = RoleNotFoundException.class)
    public ResponseEntity<Object> handleRoleNotFoundException(RoleNotFoundException ex,
                                                              HttpServletRequest request) {
        return handleCustomException(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = InvalidCredentialsException.class)
    public ResponseEntity<Object> handleInvalidCredentialsException(InvalidCredentialsException ex,
                                                                    HttpServletRequest request) {
        return handleCustomException(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = AuthException.class)
    public ResponseEntity<Object> handleAuthEntryPointJwtException(AuthException ex,
                                                                   HttpServletRequest request) {
        return handleCustomException(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = NoAccessException.class)
    public ResponseEntity<Object> handleNoAccessException(NoAccessException ex,
                                                          HttpServletRequest request) {
        return handleCustomException(ex, HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<Object> badRequestException(BadRequestException ex,
                                                          HttpServletRequest request) {
        return handleCustomException(ex, HttpStatus.BAD_REQUEST, request);
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

    private ResponseEntity<Object> handleCustomException(RuntimeException ex,
                                                         HttpStatus status,
                                                         HttpServletRequest request) {
        List<String> messages = List.of(ex.getMessage());
        Map<String, Object> body = new LinkedHashMap<>(createBody(status, messages));
        body.put("description", "uri=" + request.getRequestURI());
        return new ResponseEntity<>(body, status);
    }
}
