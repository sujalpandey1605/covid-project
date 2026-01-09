package com.covid.dashboard.repository;

import com.covid.dashboard.model.DayWise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface DayWiseRepository extends JpaRepository<DayWise, String> {
}
