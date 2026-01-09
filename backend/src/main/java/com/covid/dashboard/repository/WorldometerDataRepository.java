package com.covid.dashboard.repository;

import com.covid.dashboard.model.WorldometerData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorldometerDataRepository extends JpaRepository<WorldometerData, String> {
}
