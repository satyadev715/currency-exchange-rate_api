DROP TABLE IF EXISTS REQUEST_HISTORY;

CREATE TYPE TREND AS ENUM ('ascending','descending','constant','undefined');
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
insert into REQUEST_HISTORY(request_date,
source_currency,
target_currency,
exchange_rate,
avg_exchange_rate,
exchange_rate_trend
) values(to_date('2020-09-11', 'YYYY-MM-DD'),'EUR','INR',87.2431,86.89875,'undefined');

insert into REQUEST_HISTORY(request_date,
source_currency,
target_currency,
exchange_rate,
avg_exchange_rate,
exchange_rate_trend
) values(to_date('2020-08-28', 'YYYY-MM-DD'),'EUR','INR',87.2125,87.713,'descending');

