package com.qardio.assessment.temperaturesensor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.influxdb.exceptions.InfluxException;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller exception handler.
 */
@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

  /**
   * Handle influxdb exception.
   *
   * @param exception the exception
   * @return the string
   */
  @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
  @ExceptionHandler(InfluxException.class)
  @ResponseBody
  public String handleInfluxException(InfluxException exception) {
    log.error("InfluxException: " + exception.getMessage());
    return "Service unavailable";
  }
}
