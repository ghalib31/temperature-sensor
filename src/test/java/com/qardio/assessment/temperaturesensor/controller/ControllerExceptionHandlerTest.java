package com.qardio.assessment.temperaturesensor.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.influxdb.exceptions.InfluxException;

@ExtendWith(SpringExtension.class)
class ControllerExceptionHandlerTest {

  @InjectMocks
  private ControllerExceptionHandler controllerExceptionHandler;

  @Test
  void handleInfluxException() {
    String message = controllerExceptionHandler.handleInfluxException(new InfluxException("Connection issue"));
    assertEquals("Service unavailable", message);
  }
}