package com.currencyexchangeratetracker.util;

import com.currencyexchangeratetracker.dto.ExchangeRateDTO;
import com.currencyexchangeratetracker.dto.RequestHistoryDTO;
import com.currencyexchangeratetracker.dto.Trend;
import com.currencyexchangeratetracker.entity.RequestHistory;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

public class ExchangeRateUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(ExchangeRateUtil.class);
    public static RequestHistory prepareRequestHistory(RequestHistory requestHistory, RequestHistoryDTO requestHistoryDTO){
        if(requestHistory !=null && requestHistoryDTO !=null) {
            requestHistory.setRequestDate(requestHistoryDTO.getRequestDate());
            requestHistory.setExchangeRate(requestHistoryDTO.getExchangeRate());
            requestHistory.setAvgExchangeRate(requestHistoryDTO.getAvgOfFiveDays());
            requestHistory.setExchangeRateTrend(requestHistoryDTO.getExchangeRateTrend());
        }
        LOGGER.debug("requestHistory : "+requestHistory.toString());
        return requestHistory;
    }
    public static RequestHistoryDTO prepareRequestHistoryDTO(ExchangeRateDTO exchangeRateDTO, LocalDate requestDate) {
        RequestHistoryDTO requestHistoryDTO=null;
        ArrayList<Double> exchangeRatesList= exchangeRateDTO.getExchangeRateList();
        if(!exchangeRatesList.isEmpty()) {
            requestHistoryDTO= new RequestHistoryDTO();
            requestHistoryDTO.setExchangeRateTrend(getExchangeRateTrend(exchangeRatesList));
            requestHistoryDTO.setAvgOfFiveDays(getAverageExchangeRate(exchangeRatesList));
            //request date need to adjust as exchangeRateDTO doesn't have data for weekends
            requestHistoryDTO.setRequestDate(adjustRequestDate(requestDate));

            Map<String, Double> map = exchangeRateDTO.getRates().get(requestDate);
            if(!map.entrySet().isEmpty()) {
                for (Map.Entry<String, Double> entry : map.entrySet()) {
                    if (entry.getValue() != null) {
                        requestHistoryDTO.setExchangeRate(entry.getValue());
                    }
                }
            }
        }

        return requestHistoryDTO;
    }

    /**
     * The exchange rate trend is determined using following definition:
     * descending:   when the exchange rates in the last five   days   are   in   strictly descending order,
     * ascending: when the exchange rates in the last five days are in strictly ascending order
     * constant: when the exchange rates in the last five days are  the same
     * undefined: in other cases.
     *
     * @param exchangeRatesList
     * @return
     */
    public static String getExchangeRateTrend(ArrayList<Double> exchangeRatesList){
        //when the exchange rates in the last five   days   are   in   strictly descending order
        if (Ordering.<Double>natural().isStrictlyOrdered(exchangeRatesList))
            return Trend.ascending.toString();

        //when the exchange rates in the last five days are in strictly ascending order
        if (Ordering.<Double>natural().isStrictlyOrdered(Lists.reverse(exchangeRatesList)))
            return Trend.descending.toString();

        //when the exchange rates in the last five days are  the same
        if (exchangeRatesList.isEmpty() || exchangeRatesList.stream().allMatch(exchangeRatesList.get(0)::equals))
            return Trend.constant.toString();

        return Trend.undefined.toString();
    }

    /**
     * the  average  of  the  five  days  before  the  requested  date  (excluding  Saturday  and Sunday )
     * @param exchangeRatesList
     * @return
     */
    public static Double getAverageExchangeRate(ArrayList<Double> exchangeRatesList){
        return exchangeRatesList.stream().mapToDouble(val -> val).average().orElse(0.0);
    }

    /**\
     * request date needs to adjust based on the WEEKEND
     * @param requestDate
     * @return
     */
    public static LocalDate adjustRequestDate(LocalDate requestDate){
        if(requestDate.getDayOfWeek()== DayOfWeek.SUNDAY){
            return requestDate.minusDays(2);
        }else if(requestDate.getDayOfWeek()== DayOfWeek.SATURDAY){
            return requestDate.minusDays(1);
        }
        LOGGER.debug("request date adjusted to "+requestDate);
        return requestDate;
    }

    public static RequestHistoryDTO getRequestHistoryDTO(RequestHistory requestHistory){
        RequestHistoryDTO requestHistoryDTO = new RequestHistoryDTO();
        if(requestHistory !=null) {
            requestHistoryDTO.setRequestDate(requestHistory.getRequestDate());
            requestHistoryDTO.setExchangeRate((requestHistory.getExchangeRate()));
            requestHistoryDTO.setAvgOfFiveDays(requestHistory.getAvgExchangeRate());
            requestHistoryDTO.setExchangeRateTrend(requestHistory.getExchangeRateTrend());
        }
        LOGGER.debug("requestHistoryDTO : "+requestHistoryDTO.toString());
        return requestHistoryDTO;
    }

    public static RequestHistory getRequestHistory(RequestHistoryDTO requestHistoryDTO,
                                            String baseCurrency, String targetCurrency){
        RequestHistory requestHistory=new RequestHistory();
        if(requestHistoryDTO !=null) {
            requestHistory = ExchangeRateUtil.prepareRequestHistory(requestHistory, requestHistoryDTO);
            requestHistory.setSourceCurrency(baseCurrency);
            requestHistory.setTargetCurrency(targetCurrency);
            requestHistory.setRequestDate(requestHistoryDTO.getRequestDate());
        }
        LOGGER.debug("requestHistory : "+requestHistory.toString());
        return requestHistory;
    }
}
