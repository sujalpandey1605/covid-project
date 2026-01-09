package com.covid.dashboard.repository;

import com.covid.dashboard.model.FullGrouped;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FullGroupedRepository extends JpaRepository<FullGrouped, Long> {
    List<FullGrouped> findByCountryRegion(String countryRegion);
}
