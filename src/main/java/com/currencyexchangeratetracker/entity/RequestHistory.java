package com.currencyexchangeratetracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@ToString
public class RequestHistory implements Serializable {

    @JsonProperty("Request date")
    private LocalDate requestDate;
    @JsonProperty("Exchange rate")
    private Double exchangeRate;
    @JsonProperty("Avg exchange rate")
    private Double avgExchangeRate;
    @JsonProperty("Exchange rate trend")
    private String exchangeRateTrend;
    @JsonProperty("Base currency")
    private String sourceCurrency;
    @JsonProperty("Target currency")
    private String targetCurrency;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer request_id;

    public RequestHistory(LocalDate requestDate, Double exchangeRate, Double avgExchangeRate,
                          String exchangeRateTrend, String sourceCurrency, String targetCurrency) {
        this.requestDate = requestDate;
        this.exchangeRate = exchangeRate;
        this.avgExchangeRate = avgExchangeRate;
        this.exchangeRateTrend = exchangeRateTrend;
        this.sourceCurrency = sourceCurrency;
        this.targetCurrency = targetCurrency;
    }

    @JsonIgnore
    public Integer getRequest_id() {
        return request_id;
    }
}
