package com.qardio.assessment.temperaturesensor.model;

import java.time.Instant;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The POJO Temperature sensor data.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Measurement(name = "temperature")
public class TemperatureSensorData {
  @Column(timestamp = true)
  private Instant time;
  @Column
  private Double temperature;
}