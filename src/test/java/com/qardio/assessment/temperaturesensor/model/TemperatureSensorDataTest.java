package com.qardio.assessment.temperaturesensor.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TemperatureSensorDataTest {
  private static final Instant TIME = Instant.now();
  private static final Double TEMPERATURE = 10.11;

  @Test
  void getTime() {
    TemperatureSensorData temperatureSensorData = mockTemperatureSensorData();
    assertEquals(TIME, temperatureSensorData.getTime());
  }

  @Test
  void getTemperature() {
    TemperatureSensorData temperatureSensorData = mockTemperatureSensorData();
    assertEquals(TEMPERATURE, temperatureSensorData.getTemperature());
  }

  @Test
  void setTime() {
    TemperatureSensorData temperatureSensorData = mockTemperatureSensorData();
    temperatureSensorData.setTime(TIME.plus(Duration.ofHours(1)));
    assertEquals(TIME.plus(Duration.ofHours(1)), temperatureSensorData.getTime());
  }

  @Test
  void setTemperature() {
    TemperatureSensorData temperatureSensorData = mockTemperatureSensorData();
    temperatureSensorData.setTemperature(20.20);
    assertEquals(20.20, temperatureSensorData.getTemperature());
  }

  private TemperatureSensorData mockTemperatureSensorData(){
    return new TemperatureSensorData(TIME, TEMPERATURE);
  }
}