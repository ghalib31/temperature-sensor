package com.qardio.assessment.temperaturesensor.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApi;
import com.influxdb.query.FluxTable;
import com.qardio.assessment.temperaturesensor.common.Frequency;
import com.qardio.assessment.temperaturesensor.configuration.InfluxDbProperties;
import com.qardio.assessment.temperaturesensor.model.TemperatureSensorData;

@ExtendWith(MockitoExtension.class)
class TemperatureSensorServiceImplTest {

  @Mock
  private InfluxDBClient influxDBClient;
  @Mock
  private WriteApi writeApi;
  @Mock
  private QueryApi queryApi;
  @Mock
  private InfluxDbProperties influxDbProperties;
  @InjectMocks
  private TemperatureSensorServiceImpl temperatureSensorService;

  @BeforeEach
  void setUp() {

  }

  @Test
  void writeData() {
    when(influxDBClient.getWriteApi()).thenReturn(writeApi);
    final List<Map<String, String>> temperatureSensorRawDataList = new ArrayList<>();
    final Map map = new HashMap();
    map.put("time", "1630863959000");
    map.put("temperature", "13.5");
    temperatureSensorRawDataList.add(map);
    temperatureSensorService.writeData(temperatureSensorRawDataList);
    verify(writeApi, times(2)).listenEvents(any(), any());
  }

  @Test
  void generateAggregatedData() {
    when(influxDbProperties.getBucket()).thenReturn("bucket");
    when(influxDbProperties.getOrg()).thenReturn("org");
    when(influxDBClient.getQueryApi()).thenReturn(queryApi);
    when(queryApi.query(anyString(), anyString())).thenReturn(Arrays.asList(new FluxTable()));
    final ResponseEntity<List<TemperatureSensorData>> responseEntity = temperatureSensorService
        .generateAggregatedData(Frequency.DAILY, 0L, 1630863959000L);
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
  }
}