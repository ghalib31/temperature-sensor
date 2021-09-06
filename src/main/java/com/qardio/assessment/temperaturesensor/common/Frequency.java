package com.qardio.assessment.temperaturesensor.common;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Allowed frequency to query from backend.
 */
@AllArgsConstructor
@Getter
public enum Frequency {
  /**
   * Hourly frequency.
   */
  HOURLY("1h"),
  /**
   * Daily frequency.
   */
  DAILY("1d");

  private final String frequencyType;
}
