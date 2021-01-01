package com.currencyexchangeratetracker.service;

import com.currencyexchangeratetracker.dto.CurrencyDTO;
import com.currencyexchangeratetracker.dto.ExchangeRateDTO;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;


@Service
public class RestClientService {
    private static Logger LOGGER = LoggerFactory.getLogger(RestClientService.class);
    private final RestTemplate restTemplate;


    @Autowired
    public RestClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     *
     * @param requestDate
     * @param baseCurrency
     * @param targetCurrency
     * @return
     */
    @HystrixCommand(fallbackMethod = "getFallbackExchangeRateInfo")
    public ExchangeRateDTO getExchangeRateDTO(LocalDate requestDate, String baseCurrency, String targetCurrency) {
        LOGGER.debug("RestClientService: getExchangeRateDTO");
        ExchangeRateDTO exchangeRateDTO=new ExchangeRateDTO();
        LocalDate startDate = requestDate.minusDays(7);
        try {
            URI url = URI.create("https://api.exchangeratesapi.io/history?start_at="
                    + startDate + "&end_at=" + requestDate + "&symbols=" + targetCurrency + "&base=" + baseCurrency);

            LOGGER.info("requesting exchange rate api with URL:"+url);
            ResponseEntity<ExchangeRateDTO> exchangeRateDTOResponse =
                    restTemplate.getForEntity(url, ExchangeRateDTO.class);

            if (exchangeRateDTOResponse.hasBody()) {
                exchangeRateDTO = exchangeRateDTOResponse.getBody();
            }else{
                LOGGER.debug("exchangeRateDTOResponse :no data available");
            }
        }catch(RestClientException ex){
            LOGGER.error("RestClientException :"+ex.getMessage()+":"+ex.getCause());
        }
        return exchangeRateDTO;
    }

    /**
     * Fallback method getExchangeRateDTO
     * It will execute if there is any issue while calling external exchange rate api
     * @param requestDate
     * @param baseCurrency
     * @param targetCurrency
     * @return
     */
    public ExchangeRateDTO getFallbackExchangeRateInfo(LocalDate requestDate,
                                                         String baseCurrency, String targetCurrency){
        ExchangeRateDTO exchangeRateDTO =new ExchangeRateDTO();
        exchangeRateDTO.setBase("");
        exchangeRateDTO.setEnd_at("");
        exchangeRateDTO.setExchangeRateList(new ArrayList<>());
        exchangeRateDTO.setStart_at("");
        exchangeRateDTO.setRates(new HashMap<>());
        return exchangeRateDTO;
    }

    /**
     * get latest exchange rate currency codes from external exchange rate api
     * to make sure that API handles with updated currency codes
     * @return
     */
    public CurrencyDTO getLatestCurrencyCodes(){
        CurrencyDTO currencyDTO=new CurrencyDTO();
        try {
              URI url = URI.create("https://api.exchangeratesapi.io/latest?");

              ResponseEntity<CurrencyDTO> currencyDTOResponse =
                    restTemplate.getForEntity(url, CurrencyDTO.class);

            if (currencyDTOResponse.hasBody()) {
               currencyDTO = currencyDTOResponse.getBody();
            }else{
                 LOGGER.debug("currencyDTOResponse : no data available");
             }
        } catch (RestClientException ex) {
            LOGGER.error("RestClientException :" + ex.getMessage() + ":" + ex.getCause());
        }
        return currencyDTO;
    }

}
