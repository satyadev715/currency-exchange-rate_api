REST  service  to  display exchange  rates  for  our customers.
The service receives three parameters: 
two currencies 
and a date using  the URL path. 
The format of the URL path is /api/exchange-rate/{date}/{baseCurrency}/{targetCurrency}

The REST service returns: 
The exchange rate of the requested date, 
the  average  of  the  five  days  before  the  requested  date  (excluding  Saturday  and Sunday )  
and the exchange rate trend.

## Use case 1 
I have reviewed the Foreign exchange rates API and observed that 
below GET API call is giving the exchange rates for given period

https://api.exchangeratesapi.io/history?start_at=2020-09-04&end_at=2020-09-11&symbols=INR&base=EUR

I have implemented the first service with above GET API call
/api/exchange-rate/{date}/{baseCurrency}/{targetCurrency}

-----------------------------
to get the latest currency codes supported by Foreign exchange rates API
https://api.exchangeratesapi.io/latest

When application starts, I have implemented a rest client to invoke above GET API call 
and get the latest supported currency codes to validate the base and target currencies

after the validating the request 
we need to find the record with request date, base currency and target currency 
if its not exist then save the record in DB

All  successful  queries  should  be  persisted  in  the  DB.  The  customer  can  get historical information using two API’s, 
one for the daily information and other for the monthly information.

I have implemented below services to get the historical information
daily: /api/exchange-rate/history/daily/{yyyy}/{MM}/{dd}

monthly:  /api/exchange-rate/history/monthly/{yyyy}/{MM}

### Requests considered as invalid are:
* malformed JSON
* missing one or more fields
RequestValidationHandler will handle the above exceptions

* Only   dates   between   2000-01-01   and   yesterday   are   allowed.   
* Application will throw RequestDateException

* Only  the  currencies  supported  by https://exchangeratesapi.io/ can  be used.
  Application will throw CurrencyNotSupportedException
  
### The REQUEST_HISTORY table will contain:

create table REQUEST_HISTORY
     (
        request_id int(11) unsigned NOT NULL AUTO_INCREMENT,
        request_date  date not null,
    	source_currency VARCHAR(3) not null,
    	target_currency VARCHAR(3) not null,
    	exchange_rate decimal(10,5) not null,
    	avg_exchange_rate decimal(10,5) not null,
        exchange_rate_trend TREND,
    	constraint REQUEST_HISTORY_PK primary key(request_date,source_currency,target_currency),
    	constraint exchange_trend_val check exchange_rate_trend IN ('ascending','descending','constant','undefined')
     );
     
  CREATE TYPE TREND AS ENUM ('ascending','descending','constant','undefined');

Tools and technologies
------------------------
IntelliJ IDEA 2020.1.4
Java 11
Spring Boot 2.3.1
Spring Data JPA
H2 DB :jdbc:h2:mem:testdb
WebMvcTest for integration Testing
slf4j for logging