package com.currencyexchangeratetracker.controller;


import com.currencyexchangeratetracker.dto.RequestHistoryDTO;
import com.currencyexchangeratetracker.entity.RequestHistory;
import com.currencyexchangeratetracker.filter.CurrencyNotSupportedException;
import com.currencyexchangeratetracker.filter.RequestDateException;
import com.currencyexchangeratetracker.filter.ValidateRequestService;
import com.currencyexchangeratetracker.service.ExchangeRateService;
import com.currencyexchangeratetracker.service.RequestHistoryService;
import com.currencyexchangeratetracker.util.ExchangeRateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Api(value="ExchangeRateController")
@RestController
@RequestMapping("v1/api/exchange-rate")
public class ExchangeRateController {
    private static Logger log = LoggerFactory.getLogger(ExchangeRateController.class);

    private ExchangeRateService exchangeRateService;
    private ValidateRequestService validateRequestService;
    private RequestHistoryService requestHistoryService;

    @Autowired
    public ExchangeRateController(ExchangeRateService exchangeRateService,
                                  ValidateRequestService validateRequestService,
                                  RequestHistoryService requestHistoryService) {
        this.exchangeRateService = exchangeRateService;
        this.validateRequestService = validateRequestService;
        this.requestHistoryService = requestHistoryService;
    }

    /***
     * REST  service  to  display exchange  rates  for  our customers.
     * The service receives three parameters:
     * two currencies
     * and a date using  the URL path.
     * The format of the URL path is /api/exchange-rate/{date}/{baseCurrency}/{targetCurrency}
     *
     * The REST service returns: RequestHistoryDTO
     * The exchange rate of the requested date,
     * the  average  of  the  five  days  before  the  requested  date  (excluding  Saturday  and Sunday )
     * and the exchange rate trend.
     *
     * @param requestDate
     * @param baseCurrency
     * @param targetCurrency
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @ApiOperation(httpMethod = "GET", value = "To display exchange  rate info",
            response = String.class, responseContainer = "RequestHistoryDTO")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Exchange rates for given currency codes are not found"),
            @ApiResponse(code = 500, message = "Exchange rates for given currency codes could not be fetched")
    })
    @GetMapping(value = "/{date}/{baseCurrency}/{targetCurrency}")//, produces = MediaType.APPLICATION_JSON_VALUE)S
    public RequestHistoryDTO getExchangeRateInfo(@PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate requestDate,
                                                 @PathVariable("baseCurrency") String baseCurrency,
                                                 @PathVariable("targetCurrency") String targetCurrency) throws IOException, InterruptedException, ExecutionException {

        RequestHistoryDTO requestHistoryDTO = new RequestHistoryDTO();
        //validate the request
        if(validateRequestService.validateRequestDate(requestDate)){
            throw new RequestDateException("Only dates between 2000-01-01 and yesterday are allowed");
        }
        if(validateRequestService.validateCurrency(baseCurrency))
            throw new CurrencyNotSupportedException(baseCurrency + " Currency not supported by the API");
        if(validateRequestService.validateCurrency(targetCurrency))
            throw new CurrencyNotSupportedException(targetCurrency + " Currency not supported by the API");

        //If request date is on weekends, need to adjust the date to friday
        requestDate = ExchangeRateUtil.adjustRequestDate(requestDate);

        //to check if a record exists in DB with same request date and currencies
        Optional<RequestHistory> requestHistoryOptional = requestHistoryService.
                                                           findRequest(requestDate,baseCurrency,targetCurrency);
        if(requestHistoryOptional.isPresent()){
              log.info("same request exists in DB");
              RequestHistory requestHistory = requestHistoryOptional.get();
            requestHistoryDTO = ExchangeRateUtil.getRequestHistoryDTO(requestHistory);
        }else {
             requestHistoryDTO = exchangeRateService
                    .getExchangeRateInfo(requestDate, baseCurrency, targetCurrency);

            //save the request in DB
            RequestHistory requestHistory = ExchangeRateUtil.
                    getRequestHistory(requestHistoryDTO,baseCurrency,targetCurrency);
            Integer requestId =  requestHistoryService.saveRequest(requestHistory);
        }

        return requestHistoryDTO;
    }

    /**
     * The  customer  can  get historical information using below API,
     * for the monthly information.
     * daily: /api/exchange-rate/history/daily/{yyyy}/{MM}/{dd}
     * @param year
     * @param month
     * @param day
     * @return
     */
    @GetMapping(value="/history/daily/{yyyy}/{MM}/{dd}", produces=MediaType.APPLICATION_JSON_VALUE)
    public List<RequestHistory> getRequestHistoryByDate(@PathVariable("yyyy") Integer year,
                                                        @PathVariable("MM") Integer month,
                                                        @PathVariable("dd") Integer day){

        LocalDate requestDate=LocalDate.of(year,month,day);
        if(validateRequestService.validateRequestDate(requestDate)){
            throw new RequestDateException("Only dates between 2000-01-01 and yesterday are allowed");
        }
         //If request date is on weekends, need to adjust the date to friday
        requestDate = ExchangeRateUtil.adjustRequestDate(requestDate);
        List<RequestHistory> requestHistories = requestHistoryService.getRequestHistoryByDate(requestDate);
        return requestHistories;
    }

    /**
     * The  customer  can  get historical information using below API,
     * for the monthly information
     * monthly:  /api/exchange-rate/history/monthly/{yyyy}/{MM}
     * @param year
     * @param month
     * @return
     */
    @GetMapping(value="/history/monthly/{yyyy}/{MM}", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<RequestHistory> getRequestHistoryByMonth(@PathVariable("yyyy") Integer year,
                                                                    @PathVariable("MM") Integer month){
        LocalDate requestDate=LocalDate.of(year,month,01);
        if(validateRequestService.validateRequestDate(requestDate)){
            throw new RequestDateException("Only months between 2000-01 and current are allowed");
        }
        List<RequestHistory> requestHistories = requestHistoryService.getRequestHistoryByMonth(year,month);
        return requestHistories;
        }
}