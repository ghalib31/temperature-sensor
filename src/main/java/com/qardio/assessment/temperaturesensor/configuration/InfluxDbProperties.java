package com.qardio.assessment.temperaturesensor.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;
import lombok.Setter;

/**
 * POJO for influxdb.properties. Contains various fields needed to access influxdb.
 */
@Configuration
@ConfigurationProperties(prefix = "influxdb")
@PropertySource("classpath:influxdb.properties")
@Getter
@Setter
public class InfluxDbProperties {
  private String url;
  private String username;
  private String password;
  private String database;
  private String bucket;
  private String org;
  private String token;
}
