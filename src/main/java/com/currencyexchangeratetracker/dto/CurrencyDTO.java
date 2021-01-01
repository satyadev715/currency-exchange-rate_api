package com.currencyexchangeratetracker.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "rates",
        "base",
        "date"
})
public class CurrencyDTO {
    private static Logger log = LoggerFactory.getLogger(CurrencyDTO.class);
    @JsonProperty("rates")
    private Map<String,Double> rates;
    @JsonProperty("base")
    private String baseCurrency;
    @JsonProperty("date")
    private LocalDate requestDate;

    private Set<String> currencyCodes;

    public CurrencyDTO() {
    }

    public CurrencyDTO(Map<String, Double> rates, String baseCurrency, LocalDate requestDate) {
        this.rates = rates;
        this.baseCurrency = baseCurrency;
        this.requestDate = requestDate;
    }
    public Set<String> getCurrencyCodes()
    {
        if(!getRates().isEmpty() && !getRates().keySet().isEmpty()) {
            currencyCodes = new HashSet<>();
            currencyCodes.addAll(getRates().keySet());
            currencyCodes.add(baseCurrency);
        }
        return currencyCodes;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }

    public String getBaseCurrency() {
        log.info("baseCurrency: "+baseCurrency);
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        log.info("baseCurrency: "+baseCurrency);
        this.baseCurrency = baseCurrency;
    }

    public LocalDate getRequestDate() {
        log.info("requestDate: "+requestDate);
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        log.info("requestDate: "+requestDate);
        this.requestDate = requestDate;
    }
}
