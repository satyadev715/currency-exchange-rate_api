package com.currencyexchangeratetracker.service;

import com.currencyexchangeratetracker.dao.RequestHistoryRepository;
import com.currencyexchangeratetracker.entity.RequestHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "requestHistoryCache")
public class RequestHistoryService {
    private static Logger LOGGER = LoggerFactory.getLogger(RequestHistoryService.class);
    private RequestHistoryRepository requestHistoryRepository;

    @Autowired
    public RequestHistoryService(RequestHistoryRepository requestHistoryRepository) {
        this.requestHistoryRepository = requestHistoryRepository;
    }

    @Cacheable(value="requestHistoryCache")
    public Optional<RequestHistory> findRequest(LocalDate requestDate, String sourceCurrency, String targetCurrency){
        Optional<RequestHistory> requestHistory = requestHistoryRepository.
                findAllByRequestDateAndSourceCurrencyAndTargetCurrency(requestDate,sourceCurrency,targetCurrency);
        return requestHistory;
    }
    @Scheduled(fixedRate = 60000)
    @CacheEvict(value = "requestHistoryCache", allEntries = true)
    public void evictAllCacheValues() {
        LOGGER.info("requestHistoryCache is cleared");
    }

    @CachePut(value="requestHistoryCache")
   public Integer saveRequest(RequestHistory requestHistory){
            Integer requestId=-1;
           RequestHistory savedRequest = requestHistoryRepository.save(requestHistory);
           requestId = savedRequest.getRequest_id();
        LOGGER.info("Request saved successfully");
       return requestId;
   }
   public List<RequestHistory> getRequestHistoryByDate(LocalDate requestDate){
        LOGGER.info("getRequestHistoryByDate : "+requestDate);
        return requestHistoryRepository.findAllByRequestDate(requestDate);
   }
   public List<RequestHistory> getRequestHistoryByMonth(Integer year, Integer month){
       return requestHistoryRepository.findAllMonth(year,month);
   }
}
