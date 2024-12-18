package co.org.ccb.constituciones.transversal.errores;


import co.org.ccb.constituciones.transversal.aplicacion.logs.RegistrarLogService;
import co.org.ccb.constituciones.transversal.errores.entrada.ConflictException;
import co.org.ccb.constituciones.transversal.errores.entrada.GeneralErrorResponse;
import co.org.ccb.constituciones.transversal.errores.entrada.UnauthorizedException;
import co.org.ccb.constituciones.transversal.infraestructura.entrada.LogRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;


@Log4j2
@RestControllerAdvice
public class ConstitucionesExceptionHandler {
    @Value("${spring.application.name}")
    private String componente;
    @Autowired
    RegistrarLogService logService;
    private static final ConcurrentHashMap<String, HttpStatus> STATUS_CODES =
            new ConcurrentHashMap<>();
    private final MultiValueMap<String, String> headers = new org.springframework.http.HttpHeaders();

    public ConstitucionesExceptionHandler() {
        STATUS_CODES.put(ConflictException.class.getSimpleName(), HttpStatus.CONFLICT);
        STATUS_CODES.put(NoResourceFoundException.class.getSimpleName(), HttpStatus.NOT_FOUND);
        STATUS_CODES.put(UnauthorizedException.class.getSimpleName(), HttpStatus.UNAUTHORIZED);
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
    }

    @ExceptionHandler(value = {ConflictException.class})
    public final ResponseEntity<GeneralErrorResponse> handleConflictExceptions(
            ConflictException ex, HttpServletRequest httpRequest) {
        log.error("handleConflictExceptions", ex);

        HttpStatus status =
                STATUS_CODES.getOrDefault(ex.getClass().getSimpleName(), HttpStatus.INTERNAL_SERVER_ERROR);
        var response =
                GeneralErrorResponse.builder()
                        .message(ex.getMessage())
                        .error(ex.getError())
                        .status(status.value())
                        .path(httpRequest.getRequestURI())
                        .build();
        logService.registrar(LogRequest.builder()
                        .mensaje("Excepcion [ConflictException]: " + ex.getMessage())
                        .componente(componente)
                        .servicio(httpRequest.getRequestURI())
                        .tipo("ERROR")
                .build());
        return new ResponseEntity<>(response, headers, status);
    }

    @ExceptionHandler(value = {UnauthorizedException.class})
    public final ResponseEntity<Object> handleUnauthorizedExceptions(
            Exception ex, HttpServletRequest httpRequest) {
        log.error("handleUnauthorizedExceptions", ex);

        HttpStatus status =
                STATUS_CODES.getOrDefault(ex.getClass().getSimpleName(), HttpStatus.UNAUTHORIZED);
        var response =
                GeneralErrorResponse.builder()
                        .error(ex.getMessage())
                        .message("Usuario no autorizado")
                        .status(status.value())
                        .path(httpRequest.getRequestURI())
                        .build();
        return new ResponseEntity<>(response, headers, status);
    }

    @ExceptionHandler(value = {Exception.class})
    public final ResponseEntity<Object> handleAllExceptions(
            Exception ex, HttpServletRequest httpRequest) {
        log.error("handleAllExceptions", ex);


        HttpStatus status =
                STATUS_CODES.getOrDefault(ex.getClass().getSimpleName(), HttpStatus.INTERNAL_SERVER_ERROR);
        var response =
                GeneralErrorResponse.builder()
                        .error(ex.getMessage())
                        .message("Internal server error")
                        .status(status.value())
                        .path(httpRequest.getRequestURI())
                        .build();
        logService.registrar(LogRequest.builder()
                .mensaje("Excepcion [ConflictException]: " + ex.getMessage())
                .componente(componente)
                .servicio(httpRequest.getRequestURI())
                .tipo("ERROR")
                .build());
        return new ResponseEntity<>(response, headers, status);
    }


    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public final ResponseEntity<Object> handleArgumentNotValidExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest httpRequest) {
        log.error("handleArgumentNotValidExceptions", ex);
        var errors = new ArrayList<String>();
        ex.getBindingResult().getFieldErrors().forEach(f -> errors.add(f.getField() + ": " + f.getDefaultMessage()));
        ex.getBindingResult().getGlobalErrors().forEach(g -> errors.add(g.getObjectName() + ": " + g.getDefaultMessage()));
        var response =
                GeneralErrorResponse.builder()
                        .error(errors.toString())
                        .message("Bad Request")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .path(httpRequest.getRequestURI())
                        .build();
        logService.registrar(LogRequest.builder()
                .mensaje("Excepcion [MethodArgumentNotValidException]: " + errors)
                .componente(componente)
                .servicio(httpRequest.getRequestURI())
                .tipo("ERROR")
                .build());
        return new ResponseEntity<>(response, headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    public final ResponseEntity<Object> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException exception, HttpServletRequest httpRequest) {
        var response =
                GeneralErrorResponse.builder()
                        .error(exception.getCause().getCause().getMessage())
                        .message("Bad Request")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .path(httpRequest.getRequestURI())
                        .build();
        logService.registrar(LogRequest.builder()
                .mensaje("Excepcion [MethodArgumentTypeMismatchException]: " + exception.getCause().getCause().getMessage())
                .componente(componente)
                .servicio(httpRequest.getRequestURI())
                .tipo("ERROR")
                .build());
        return new ResponseEntity<>(response, headers, HttpStatus.BAD_REQUEST);
    }
}
