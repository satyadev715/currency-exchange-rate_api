package com.currencyexchangeratetracker.dao;

import com.currencyexchangeratetracker.entity.RequestHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RequestHistoryRepository extends JpaRepository<RequestHistory,Integer> {

    Optional<RequestHistory> findAllByRequestDateAndSourceCurrencyAndTargetCurrency(LocalDate request_date, String srcCurrency, String targetCurrency);

    List<RequestHistory> findAllByRequestDate(LocalDate requestDate);

    @Query(value="SELECT * FROM REQUEST_HISTORY WHERE EXTRACT(year FROM REQUEST_HISTORY.request_date) = ?1 AND EXTRACT (month FROM REQUEST_HISTORY.request_date) = ?2", nativeQuery = true)
    List<RequestHistory> findAllMonth(Integer year, Integer month);
}