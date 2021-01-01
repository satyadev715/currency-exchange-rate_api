package com.currencyexchangeratetracker.service;

import com.currencyexchangeratetracker.dto.CurrencyDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Service
public class CurrencyCodeService {
    private static Logger log = LoggerFactory.getLogger(CurrencyCodeService.class);

   private RestClientService restClientService;
    private Set<String> currencyCodes;

    @Autowired
    public CurrencyCodeService(RestClientService restClientService) {
        this.restClientService = restClientService;
    }

    @PostConstruct
    public void getLatestCurrencyCodes() {
                CurrencyDTO currencyDTO = null;
                currencyCodes = new HashSet<>();
                currencyDTO = restClientService.getLatestCurrencyCodes();
                currencyCodes = currencyDTO.getCurrencyCodes();
    }
    public Set<String> getAvailableCurrencyCodes(){
        return currencyCodes;
    }
}
