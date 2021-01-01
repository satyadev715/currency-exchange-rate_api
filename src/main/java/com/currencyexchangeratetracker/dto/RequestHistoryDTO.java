package com.currencyexchangeratetracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RequestHistoryDTO {//implements Serializable {

    @JsonProperty("Request date")
    private LocalDate requestDate;
    @JsonProperty("Exchange date")
    private Double exchangeRate;
    @JsonProperty("Average rate")
    private Double avgOfFiveDays;
    @JsonProperty("Exchange rate trend")
    private String exchangeRateTrend;

    /*public RequestHistoryDTO() {
    }

    public RequestHistoryDTO(LocalDate requestDate, Double exchangeRate,
                             Double avgOfFiveDays, String exchangeRateTrend) {
        this.requestDate = requestDate;
        this.exchangeRate = exchangeRate;
        this.avgOfFiveDays = avgOfFiveDays;
        this.exchangeRateTrend = exchangeRateTrend;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Double getAvgOfFiveDays() {
        return avgOfFiveDays;
    }

    public void setAvgOfFiveDays(Double avgOfFiveDays) {
        this.avgOfFiveDays = avgOfFiveDays;
    }

    public String getExchangeRateTrend() {
        return exchangeRateTrend;
    }

    public void setExchangeRateTrend(String exchangeRateTrend) {
        this.exchangeRateTrend = exchangeRateTrend;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    @Override
    public String toString() {
        return "RequestHistoryDTO{" +
                "requestDate=" + requestDate +
                ", exchangeRate=" + exchangeRate +
                ", avgOfFiveDays=" + avgOfFiveDays +
                ", exchangeRateTrend='" + exchangeRateTrend + '\'' +
                '}';
    }*/
}
