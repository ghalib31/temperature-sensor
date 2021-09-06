package com.qardio.assessment.temperaturesensor.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.qardio.assessment.temperaturesensor.common.Frequency;
import com.qardio.assessment.temperaturesensor.model.TemperatureSensorData;

/**
 * The interface to send and retrieve temperature sensor data.
 */
public interface TemperatureSensorService {
  /**
   * Save temperature sensor data.
   *
   * @param temperatureSensorRawDataList the list of epoch time and temperature data
   * @return the list
   */
  List<ResponseEntity<String>> writeData(final List<Map<String, String>> temperatureSensorRawDataList);

  /**
   * Generate aggregate data response entity.
   *
   * @param frequency the frequency
   * @param startTime the start time
   * @param endTime   the end time
   * @return the response entity
   */
  ResponseEntity<List<TemperatureSensorData>> generateAggregatedData(final Frequency frequency, final long startTime, final long endTime);
}
