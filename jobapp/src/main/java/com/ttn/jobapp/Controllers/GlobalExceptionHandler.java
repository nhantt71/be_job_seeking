/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import java.io.IOException;
import org.apache.tika.exception.UnsupportedFormatException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *
 * @author PC
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnsupportedFormatException.class)
    public ResponseEntity<String> handleUnsupportedFormat(UnsupportedFormatException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIoException(IOException ex) {
        return ResponseEntity.status(502)
                .body("Storage service error: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.internalServerError()
                .body("Processing error: " + ex.getMessage());
    }
}
