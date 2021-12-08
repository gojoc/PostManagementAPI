package ro.deloittedigital.samekh.postmanagement.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ro.deloittedigital.samekh.postmanagement.exception.EmptyFileException;
import ro.deloittedigital.samekh.postmanagement.exception.FileNotImageException;
import ro.deloittedigital.samekh.postmanagement.exception.InvalidImageException;
import ro.deloittedigital.samekh.postmanagement.exception.PostNotFoundException;
import ro.deloittedigital.samekh.postmanagement.exception.ServiceUnavailableException;
import ro.deloittedigital.samekh.postmanagement.exception.UploadFailedException;

import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerController {
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<List<String>> handleHttpMessageNotReadableException() {
        List<String> errors = List.of("The request body is not valid.");
        log.info("[ExceptionHandlerController] HTTP message not readable: {}", errors);
        return ResponseEntity.badRequest().body(errors);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<List<String>> handleConstraintViolationException(ConstraintViolationException exception) {
        List<String> errors = new ArrayList<>();
        exception.getConstraintViolations().forEach(constraintViolation -> {
            String field = null;
            for (Path.Node node : constraintViolation.getPropertyPath()) {
                field = node.getName();
            }
            errors.add(String.format("The %s: '%s' %s.", field, constraintViolation.getInvalidValue(),
                    constraintViolation.getMessage()));
        });
        log.info("[ExceptionHandlerController] constraint violations: {}", errors);
        return ResponseEntity.badRequest().body(errors);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = EmptyFileException.class)
    public ResponseEntity<List<String>> handleEmptyFileException(EmptyFileException exception) {
        List<String> errors = List.of(exception.getMessage());
        log.info("[ExceptionHandlerController] empty file: {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = FileNotImageException.class)
    public ResponseEntity<List<String>> handleFileNotImageException(FileNotImageException exception) {
        List<String> errors = List.of(exception.getMessage());
        log.info("[ExceptionHandlerController] file not image: {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = InvalidImageException.class)
    public ResponseEntity<List<String>> handleInvalidImageException(InvalidImageException exception) {
        List<String> errors = List.of(exception.getMessage());
        log.info("[ExceptionHandlerController] invalid image: {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = PostNotFoundException.class)
    public ResponseEntity<List<String>> handlePostNotFoundException(PostNotFoundException exception) {
        List<String> errors = List.of(exception.getMessage());
        log.info("[ExceptionHandlerController] post not found: {}", errors);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = UploadFailedException.class)
    public ResponseEntity<List<String>> handleUploadFailedException(UploadFailedException exception) {
        List<String> errors = List.of(exception.getMessage());
        log.info("[ExceptionHandlerController] upload failed: {}", errors);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
    }

    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(value = ServiceUnavailableException.class)
    public ResponseEntity<List<String>> handleServiceUnavailableException(ServiceUnavailableException exception) {
        List<String> errors = List.of(exception.getMessage());
        log.info("[ExceptionHandlerController] service unavailable: {}", errors);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errors);
    }
}
