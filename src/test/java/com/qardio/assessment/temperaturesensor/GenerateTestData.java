package com.qardio.assessment.temperaturesensor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class to generate test data.
 */
@Slf4j
public class GenerateTestData {

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   * @throws JsonProcessingException the json processing exception
   */
  public static void main(String[] args) throws JsonProcessingException {
    final GenerateTestData generateTestData = new GenerateTestData();
    final List<Map<String, String>> temperatureSensorData = generateTestData.generate();
    ObjectMapper mapper = new ObjectMapper();
    log.info(mapper.writeValueAsString(temperatureSensorData));
  }

  /**
   * Generate list.
   *
   * @return the list
   */
  public List<Map<String, String>> generate() {
    final List<Map<String, String>> temperatureSensorDataList = new ArrayList<>();
    long oldDate = System.currentTimeMillis() - (365 * 24 * 60 * 60 * 1000);
    for (int i = 0; i < 100; i++) {
      final Map map = new HashMap();
      oldDate = oldDate + (25 * 60 * 1000);
      map.put("time", oldDate);
      map.put("temperature", new Random().nextDouble() * 50);
      temperatureSensorDataList.add(map);
    }
    return temperatureSensorDataList;
  }
}
