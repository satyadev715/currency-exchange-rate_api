package com.currencyexchangeratetracker.service;

import com.currencyexchangeratetracker.dto.ExchangeRateDTO;
import com.currencyexchangeratetracker.dto.RequestHistoryDTO;
import com.currencyexchangeratetracker.util.ExchangeRateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@CacheConfig(cacheNames =   "exchangeRateCache")
public class ExchangeRateService {

    private static Logger LOGGER = LoggerFactory.getLogger(ExchangeRateService.class);

    private final RestClientService restClientService;

    @Autowired
    public ExchangeRateService(RestClientService restClientService) {
        this.restClientService = restClientService;
    }

    @Cacheable(value="exchangeRateCache")
    public RequestHistoryDTO getExchangeRateInfo(LocalDate requestDate,
                                                 String baseCurrency, String targetCurrency){
        RequestHistoryDTO requestHistoryDTO;

        ExchangeRateDTO exchangeRateDTO = restClientService.
                getExchangeRateDTO(requestDate,baseCurrency,targetCurrency);

        requestHistoryDTO = ExchangeRateUtil.prepareRequestHistoryDTO(exchangeRateDTO, requestDate);

        return requestHistoryDTO;
    }

    @Scheduled(fixedRate = 60000)
    @CacheEvict(value = "exchangeRateCache", allEntries = false)
    public void evictAllCacheValues() {
        LOGGER.info("exchangeRateCache is cleared");
    }

}
