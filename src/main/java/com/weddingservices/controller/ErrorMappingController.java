package com.weddingservices.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
@RequestMapping("/error")
public class ErrorMappingController {
	private static final Logger logger = LoggerFactory.getLogger(ErrorMappingController.class);
	
	@RequestMapping("/404")
	@ExceptionHandler(NoHandlerFoundException.class)
	public String pageNotFound(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
		logger.warn("404 page!");
		return "error/404";
	}
	
	@RequestMapping("/405")
	public String methodNotAllowed(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
		logger.warn("405 page!");
		return "error/405";
	}
	
}
