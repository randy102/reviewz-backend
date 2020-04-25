package com.backend;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class Error {
    static public ResponseStatusException NotFoundError(String object){
        String message = object + " not found";
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }

    static public ResponseStatusException DuplicatedError(String object){
        String message = object + " existed";
        return new ResponseStatusException(HttpStatus.CONFLICT, message);
    }

    static public ResponseStatusException NoPermissionError(){
        String message = "No permission";
        return new ResponseStatusException(HttpStatus.FORBIDDEN, message);
    }

    static public ResponseStatusException FormError(String object){
        String message = "Invalid field: " + object.toLowerCase();
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }
}
