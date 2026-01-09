package com.covid.dashboard.repository;

import com.covid.dashboard.model.CountryWiseLatest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryWiseLatestRepository extends JpaRepository<CountryWiseLatest, String> {
}
