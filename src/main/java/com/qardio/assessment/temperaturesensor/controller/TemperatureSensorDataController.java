package com.qardio.assessment.temperaturesensor.controller;

import static com.qardio.assessment.temperaturesensor.common.TemperatureSensorConstants.PAGE_AGGREGATED_DATA;
import static com.qardio.assessment.temperaturesensor.common.TemperatureSensorConstants.PAGE_SEND_DATA;
import static com.qardio.assessment.temperaturesensor.common.TemperatureSensorConstants.PAGE_TEMPERATURE;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.qardio.assessment.temperaturesensor.common.Frequency;
import com.qardio.assessment.temperaturesensor.service.TemperatureSensorService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * This controller exposes api for
 * 1. sending temperature sensor data to backend database and
 * 2. fetching aggregate data on hourly or daily basis.
 */
@RestController
@AllArgsConstructor
@RequestMapping(PAGE_TEMPERATURE)
@Slf4j
public class TemperatureSensorDataController {

  private final TemperatureSensorService temperatureSensorService;

  /**
   * Get the status whether sent data is saved or not.
   *
   * @param temperatureSensorDataList the temperature sensor data list.
   *                                  Sample input data : [{"temperature":6.83,"time":1629359668793}]
   * @return the mono status
   */
  @PostMapping(PAGE_SEND_DATA)
  @ResponseBody
  public Mono<List<ResponseEntity<String>>> sendData(@RequestBody final List<Map<String, String>> temperatureSensorDataList) {
    return Mono.just(temperatureSensorService.writeData(temperatureSensorDataList));
  }

  /**
   * Generate aggregated data for temperature sensor.
   *
   * @param frequency the frequency (hourly, daily)
   * @param startTime the start time (epoch time in millisecond)
   * @param endTime   the end time (epoch time in millisecond)
   * @return the mono
   */
  @GetMapping(PAGE_AGGREGATED_DATA)
  @ResponseBody
  public Mono<ResponseEntity> generateAggregatedData(@RequestParam final String frequency,
                                                     @RequestParam final long startTime, @RequestParam final long endTime) {
    final String validationMessage = validateParameters(frequency, startTime, endTime);
    if (StringUtils.hasLength(validationMessage)) {
      log.info("validationMessage : {}", validationMessage);
      return Mono.just(ResponseEntity.badRequest().body(validationMessage));
    }
    return Mono.just(temperatureSensorService.generateAggregatedData(
        Frequency.valueOf(frequency.toUpperCase()), startTime, endTime));
  }

  // Validate input parameters
  private String validateParameters(final String frequency, final Long startTime, final Long endTime) {
    if (!StringUtils.hasLength(frequency)) {
      return "Missing frequency in request parameter";
    } else if (Arrays.stream(Frequency.values()).noneMatch(e -> e.name().equalsIgnoreCase(frequency))) {
      return "Frequency is not correct";
    } else if (startTime.compareTo(endTime) >= 0) {
      return "endTime should be greater that startTime";
    }
    return "";
  }
}
