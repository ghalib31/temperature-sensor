package com.qardio.assessment.temperaturesensor.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Configuration to connect to influx database.
 */
@Configuration
@AllArgsConstructor
@Slf4j
public class InfluxDatabaseConfiguration {
  private final InfluxDbProperties influxDbProperties;

  /**
   * Influx db client influx db client.
   *
   * @return the influx db client
   */
  @Bean
  public InfluxDBClient influxDBClient() {
    return InfluxDBClientFactory.create(influxDbProperties.getUrl(), influxDbProperties.getToken().toCharArray());
  }

}