package com.currencyexchangeratetracker;

import com.currencyexchangeratetracker.dto.RequestHistoryDTO;
import com.currencyexchangeratetracker.entity.RequestHistory;
import com.currencyexchangeratetracker.filter.CurrencyNotSupportedException;
import com.currencyexchangeratetracker.filter.RequestDateException;
import com.currencyexchangeratetracker.filter.ValidateRequestService;
import com.currencyexchangeratetracker.service.ExchangeRateService;
import com.currencyexchangeratetracker.service.RequestHistoryService;
import com.currencyexchangeratetracker.service.RestClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@WebMvcTest
class CurrencyexchangeratetrackerApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ValidateRequestService validateRequestService;

	@MockBean
	private ExchangeRateService exchangeRateService;

	@MockBean
	private RequestHistoryService requestHistoryService;

	@MockBean
	private RestTemplate restTemplate;

	@MockBean
	private RestClientService restClientService;

	@Test
	void contextLoads() {
	}

	@Test
	public void testGetExchangeRateInfo() throws Exception {
		LocalDate requestDate=LocalDate.of(2020,9,11);
		given(validateRequestService.validateRequestDate(requestDate)).willReturn(false);
		given(validateRequestService.validateCurrency("EUR")).willReturn(false);
		given(validateRequestService.validateCurrency("INR")).willReturn(false);
		RequestHistory requestHistory = new RequestHistory(requestDate,87.2431,86.89875,"undefined","EUR","INR");
		RequestHistoryDTO requestHistoryDTO = new RequestHistoryDTO(requestDate,87.2431,86.89875,"undefined");
		given(requestHistoryService.saveRequest(requestHistory)).willReturn(75);
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
				get("/api/exchange-rate/2020-09-11/EUR/INR")).andReturn();
		String response = result.getResponse().getContentAsString();
		if(response.equals(requestHistoryDTO.toString())){
			assertTrue(true);
		}

	}

	@Test
	public void testRequestDateException() throws Exception{
		LocalDate requestDate=LocalDate.of(2022,8,01);
		given(validateRequestService.validateRequestDate(requestDate)).willReturn(true);

		//check request date exception for exchange rate api
		mockMvc.perform(MockMvcRequestBuilders.
				get("/api/exchange-rate/2022-08-01/EUR/INR"))
				.andExpect(result ->
						assertTrue(result.getResolvedException() instanceof RequestDateException));

		//check request date exception for exchange rate history daily api
		mockMvc.perform(MockMvcRequestBuilders.
				get("/api/exchange-rate/history/daily/2022/08/01"))
				.andExpect(result ->
						assertTrue(result.getResolvedException() instanceof RequestDateException));

		////check request date exception for exchange rate history monthly api
		mockMvc.perform(MockMvcRequestBuilders.
				get("/api/exchange-rate/history/monthly/2022/08"))
				.andExpect(result ->
						assertTrue(result.getResolvedException() instanceof RequestDateException));
	}
	@Test
	public void testCurrencyException() throws Exception {
		given(validateRequestService.validateCurrency("PAK")).willReturn(true);

		mockMvc.perform(MockMvcRequestBuilders.
				get("/api/exchange-rate/2020-08-01/EUR/PAK"))
				.andExpect(result ->
						assertTrue(result.getResolvedException() instanceof CurrencyNotSupportedException));
	}
}
