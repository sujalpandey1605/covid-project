package com.covid.dashboard.controller;

import com.covid.dashboard.model.*;
import com.covid.dashboard.repository.*;
import com.covid.dashboard.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private CountryWiseLatestRepository countryRepository;

    @Autowired
    private DayWiseRepository dayWiseRepository;

    @Autowired
    private AnalysisService analysisService;
    
    @Autowired
    private FullGroupedRepository fullGroupedRepository;

    @GetMapping("/summary")
    public List<CountryWiseLatest> getSummary() {
        return countryRepository.findAll();
    }

    @GetMapping("/trends")
    public List<DayWise> getTrends() {
        return dayWiseRepository.findAll();
    }
    
    @GetMapping("/history/{country}")
    public List<FullGrouped> getCountryHistory(@PathVariable String country) {
        return fullGroupedRepository.findByCountryRegion(country);
    }

    @GetMapping("/alerts")
    public List<Map<String, Object>> getAlerts() {
        return analysisService.getAlerts();
    }

    @PostMapping("/update")
    public org.springframework.http.ResponseEntity<?> updateCountryData(@RequestBody Map<String, Object> payload) {
        String country = (String) payload.get("country");
        // Convert to proper types safely (assuming frontend sends numbers)
        Long confirmed = payload.get("confirmed") != null ? Long.valueOf(payload.get("confirmed").toString()) : null;
        Long deaths = payload.get("deaths") != null ? Long.valueOf(payload.get("deaths").toString()) : null;
        Long recovered = payload.get("recovered") != null ? Long.valueOf(payload.get("recovered").toString()) : null;

        return countryRepository.findById(country)
            .map(existing -> {
                if (confirmed != null) existing.setConfirmed(confirmed);
                if (deaths != null) existing.setDeaths(deaths);
                if (recovered != null) existing.setRecovered(recovered);
                countryRepository.save(existing);
                return org.springframework.http.ResponseEntity.ok("Data updated successfully");
            })
            .orElse(org.springframework.http.ResponseEntity.notFound().build());
    }
}
