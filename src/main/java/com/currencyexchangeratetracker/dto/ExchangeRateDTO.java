package com.currencyexchangeratetracker.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "rates",
        "start_at",
        "base",
        "end_at"
})
public class ExchangeRateDTO implements Serializable {
    private static Logger log = LoggerFactory.getLogger(ExchangeRateDTO.class);
    @JsonProperty("rates")
    private Map<LocalDate,Map<String,Double>> rates;
    @JsonProperty("start_at")
    private String start_at;
    @JsonProperty("base")
    private String base;
    @JsonProperty("end_at")
    private String end_at;

    private ArrayList<Double> exchangeRateList;

    public ArrayList<Double> getExchangeRateList() {
        Collection<Map<String,Double>> mapCollection = getRates().values();
        exchangeRateList=new ArrayList<>();
        if(!mapCollection.isEmpty()) {
            for (Map<String, Double> map : mapCollection) {
               if(!map.isEmpty()) {
                   for (Map.Entry<String, Double> entry : map.entrySet()) {
                       if(entry.getValue()!=null) {
                           exchangeRateList.add(entry.getValue());
                       }
                   }
               }
            }
        }

        if(!exchangeRateList.isEmpty()){
            exchangeRateList.remove(exchangeRateList.size()-1);
            }

        return exchangeRateList;
    }

    public void setExchangeRateList(ArrayList<Double> exchangeRateList) {

        this.exchangeRateList = exchangeRateList;
    }

    public ExchangeRateDTO() {
    }

    public ExchangeRateDTO(Map<LocalDate, Map<String,Double>> rates, String start_at, String base, String end_at) {
        this.rates = rates;
        this.start_at = start_at;
        this.base = base;
        this.end_at = end_at;

    }

    public Map<LocalDate, Map<String,Double>> getRates() {
         return rates;
    }

    public void setRates(Map<LocalDate, Map<String,Double>> rates) {
        this.rates = rates.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue,(e1,e2) ->e1, LinkedHashMap::new));
    }
    public String getStart_at() {
        return start_at;
    }

    public void setStart_at(String start_at) {
        this.start_at = start_at;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getEnd_at() {
        return end_at;
    }

    public void setEnd_at(String end_at) {
        this.end_at = end_at;
    }

    @Override
    public String toString() {
        return "ExchangeRateDTO{" +
                "rates=" + rates +
                ", start_at='" + start_at + '\'' +
                ", base='" + base + '\'' +
                ", end_at='" + end_at + '\'' +
                ", exchangeRateList=" + exchangeRateList +
                '}';
    }
}
