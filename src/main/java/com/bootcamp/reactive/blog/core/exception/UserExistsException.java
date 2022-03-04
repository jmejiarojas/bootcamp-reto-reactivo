package com.bootcamp.reactive.blog.core.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserExistsException extends RuntimeException {

    private HttpStatus status  = HttpStatus.BAD_REQUEST;
    private String message;

    public UserExistsException(String message){
        this.message = message;
    }


}