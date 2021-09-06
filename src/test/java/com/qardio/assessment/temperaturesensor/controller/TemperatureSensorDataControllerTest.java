package com.qardio.assessment.temperaturesensor.controller;

import static com.qardio.assessment.temperaturesensor.common.TemperatureSensorConstants.PAGE_AGGREGATED_DATA;
import static com.qardio.assessment.temperaturesensor.common.TemperatureSensorConstants.PAGE_SEND_DATA;
import static com.qardio.assessment.temperaturesensor.common.TemperatureSensorConstants.PAGE_TEMPERATURE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import com.qardio.assessment.temperaturesensor.common.Frequency;
import com.qardio.assessment.temperaturesensor.service.TemperatureSensorService;

@ExtendWith(MockitoExtension.class)
class TemperatureSensorDataControllerTest {

  private WebTestClient webTestClient;
  @Mock
  private TemperatureSensorService temperatureSensorService;
  @InjectMocks
  private TemperatureSensorDataController controller;

  @BeforeEach
  void setUp() {
    webTestClient = WebTestClient.bindToController(controller).build();
    controller = new TemperatureSensorDataController(temperatureSensorService);
  }

  @Test
  void sendData_without_input_should_respond_bad_request() {
    webTestClient.post()
        .uri(PAGE_TEMPERATURE+PAGE_SEND_DATA)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void sendData_ok() {
    List<Map<String, String>> temperatureSensorDataList = new ArrayList<>();
    Map map = new HashMap();
    map.put("time", System.currentTimeMillis());
    map.put("temperature", new Random().nextDouble() * 50);
    temperatureSensorDataList.add(map);

    webTestClient.post()
        .uri(PAGE_TEMPERATURE+PAGE_SEND_DATA)
        .accept(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(temperatureSensorDataList))
        .exchange()
        .expectStatus().isOk();
  }

  @Test
  void generateAggregatedData_bad_request() {
    webTestClient.get()
        .uri(PAGE_TEMPERATURE+PAGE_AGGREGATED_DATA)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void generateAggregatedData_wrong_param() {
    webTestClient.get()
        .uri(PAGE_TEMPERATURE+PAGE_AGGREGATED_DATA+"?frequency=minute&startTime=1599293874000&endTime=1630829874000")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void generateAggregatedData_ok() {
    when(temperatureSensorService.generateAggregatedData(any(Frequency.class), anyLong(), anyLong())).thenReturn(ResponseEntity.ok().build());

    webTestClient.get()
        .uri(PAGE_TEMPERATURE+PAGE_AGGREGATED_DATA+"?frequency=daily&startTime=1599293874000&endTime=1630829874000")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk();
  }
}