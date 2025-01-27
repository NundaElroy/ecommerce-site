package com.ecommerce.nunda.controller;


import com.ecommerce.nunda.customexceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice(basePackages = "com.ecommerce.nunda.controller")
public class ViewExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleException(IllegalArgumentException ex) {
        ModelAndView modelAndView = new ModelAndView("error/error");
        modelAndView.addObject("message", ex.getMessage());
        modelAndView.addObject("status", HttpStatus.BAD_REQUEST.value());
        return modelAndView;
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView handleException(UserNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("error/error");
        modelAndView.addObject("message", ex.getMessage());
        modelAndView.addObject("status", HttpStatus.NOT_FOUND.value());
        return modelAndView;
    }
}
