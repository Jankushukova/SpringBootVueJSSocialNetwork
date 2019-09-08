package com.okta.springbootvue.SpringBootVueApplication.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class BaseController {

    protected ResponseEntity<?> buildResponse(Object data, HttpStatus httpStatus){
        return new ResponseEntity<>(data , httpStatus);
    }

    protected ResponseEntity<?> buildResponse(HttpStatus httpStatus){
        return new ResponseEntity<>(httpStatus);
    }


}
