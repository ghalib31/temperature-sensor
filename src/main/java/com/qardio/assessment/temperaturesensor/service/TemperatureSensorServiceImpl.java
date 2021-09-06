package com.qardio.assessment.temperaturesensor.service;

import static com.qardio.assessment.temperaturesensor.common.TemperatureSensorConstants.TEMPERATURE;
import static com.qardio.assessment.temperaturesensor.common.TemperatureSensorConstants.TIME;
import static com.qardio.assessment.temperaturesensor.common.TemperatureSensorConstants.VALUE;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.events.WriteErrorEvent;
import com.influxdb.client.write.events.WriteSuccessEvent;
import com.influxdb.query.FluxTable;
import com.qardio.assessment.temperaturesensor.common.Frequency;
import com.qardio.assessment.temperaturesensor.configuration.InfluxDbProperties;
import com.qardio.assessment.temperaturesensor.model.TemperatureSensorData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The implementation of TemperatureSensorService. This class saves and retrieves data from Influx database
 */
@Service
@AllArgsConstructor
@Slf4j
public class TemperatureSensorServiceImpl implements TemperatureSensorService {

  private final InfluxDBClient influxDBClient;
  private final InfluxDbProperties influxDbProperties;

  public List<ResponseEntity<String>> writeData(final List<Map<String, String>> temperatureSensorRawDataList) {
    final List<ResponseEntity<String>> responseEntities = new ArrayList<>();
    try (WriteApi writeApi = influxDBClient.getWriteApi()) {
      temperatureSensorRawDataList.stream().forEach
          (temperatureSensorRawData ->
          {
            writeApi.writeMeasurement(influxDbProperties.getBucket(), influxDbProperties.getOrg(),
                WritePrecision.MS, new TemperatureSensorData(
                    Instant.ofEpochMilli(Long.valueOf(temperatureSensorRawData.get(TIME))),
                    Double.valueOf(temperatureSensorRawData.get("temperature"))));
            log.debug("temperatureSensorRawData.getTemperature is {}", temperatureSensorRawData.get(TEMPERATURE));
            writeApi.listenEvents(WriteSuccessEvent.class, value -> responseEntities.add(ResponseEntity.ok("Successfully saved record")));
            writeApi.listenEvents(WriteErrorEvent.class, value -> responseEntities.add(ResponseEntity.ok("Failed to save record")));
          });
    }
    return responseEntities;
  }

  public ResponseEntity<List<TemperatureSensorData>> generateAggregatedData(final Frequency frequency, final long startTime, final long endTime) {
    // Query to influxdb to get mean based on given frequency
    final String queryString = "from(bucket: \"%s\")\n" +
        "    |> range(start: %s, stop: %s)\n" +
        "    |> aggregateWindow(every: %s, fn: mean, createEmpty: false)";

    final String query = String.format(queryString,
        influxDbProperties.getBucket(),
        Instant.ofEpochMilli(startTime), Instant.ofEpochMilli(endTime),
        frequency.getFrequencyType());
    log.debug("query: {}", query);

    final List<FluxTable> tables = influxDBClient.getQueryApi().query(query, influxDbProperties.getOrg());
    final List<TemperatureSensorData> response = new ArrayList<>();
    tables.stream().forEach(
        fluxTable -> fluxTable.getRecords().stream().forEach(
            fluxRecord -> response.add(
                new TemperatureSensorData(fluxRecord.getTime(), (Double) fluxRecord.getValueByKey(VALUE)))));
    return ResponseEntity.ok(response);
  }
}
