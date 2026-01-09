package com.covid.dashboard.repository;

import com.covid.dashboard.model.CovidCleanComplete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CovidCleanCompleteRepository extends JpaRepository<CovidCleanComplete, Long> {
    List<CovidCleanComplete> findByCountryRegion(String countryRegion);
}
