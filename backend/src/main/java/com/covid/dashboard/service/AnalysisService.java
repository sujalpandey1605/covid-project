package com.covid.dashboard.service;

import com.covid.dashboard.model.CountryWiseLatest;
import com.covid.dashboard.repository.CountryWiseLatestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AnalysisService {

    @Autowired
    private CountryWiseLatestRepository countryWiseLatestRepository;

    public List<Map<String, Object>> getAlerts() {
        List<Map<String, Object>> alerts = new ArrayList<>();
        List<CountryWiseLatest> allData = countryWiseLatestRepository.findAll();

        for (CountryWiseLatest data : allData) {
            // Logic: DANGER if New Deaths >= 3
            if (data.getNewDeaths() != null && data.getNewDeaths() >= 3) {
                 alerts.add(Map.of(
                    "level", "HIGH",
                    "country", data.getCountryRegion(),
                    "message", "DANGER: This place is dangerous, please don't go. (" + data.getNewDeaths() + " deaths today)"
                ));
            }
            // Logic: High Alert if New Cases > 10 (High transmission) but low deaths
            else if (data.getNewCases() != null && data.getNewCases() > 10) {
                alerts.add(Map.of(
                    "level", "HIGH",
                    "country", data.getCountryRegion(),
                    "message", "High transmission detected: " + data.getNewCases() + " new cases."
                ));
            } 
            // Logic: Specific User Rule: 100 cases, 1 death -> Very Dangerous
            else if (data.getConfirmed() != null && data.getConfirmed() >= 100 && 
                     data.getDeaths() != null && data.getDeaths() >= 1) {
                 alerts.add(Map.of(
                    "level", "HIGH",
                    "country", data.getCountryRegion(),
                    "message", "CRITICAL WARNING: High fatality rate detected (>= 1 death / 100 cases). DO NOT GO."
                ));
            } 
            // Logic: Warning if Active cases > 100000
            else if (data.getActive() != null && data.getActive() > 100000) {
                 alerts.add(Map.of(
                    "level", "WARNING",
                    "country", data.getCountryRegion(),
                    "message", "High active cases count: " + data.getActive()
                ));
            }
        }
        return alerts;
    }
}
