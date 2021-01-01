package com.currencyexchangeratetracker.filter;

import com.currencyexchangeratetracker.service.CurrencyCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Service
public class ValidateRequestService {
    private static Logger log = LoggerFactory.getLogger(ValidateRequestService.class);

    private CurrencyCodeService currencyCodeService;

    @Autowired
    public ValidateRequestService(CurrencyCodeService currencyCodeService) {
        this.currencyCodeService = currencyCodeService;
    }

    public Boolean validateCurrency(String currency){
        //validate the currencies
        Optional<Set<String>> availableCurrencyCodeSet = Optional.ofNullable(currencyCodeService.getAvailableCurrencyCodes());
        if(availableCurrencyCodeSet.isPresent()) {
            if (!availableCurrencyCodeSet.get().contains(currency)) {
                log.error(currency + " Currency not supported by the API");
                return true;
            }
        }
        return false;
    }
    public Boolean validateRequestDate(LocalDate requestDate){
        if(requestDate.isBefore(LocalDate.parse("2000-01-01"))
                || requestDate.isAfter(LocalDate.now().minusDays(1))){
            return true;
        }
        return false;
    }

}
