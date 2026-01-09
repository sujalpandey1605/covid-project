package com.covid.dashboard.controller;

import com.covid.dashboard.model.*;
import com.covid.dashboard.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/manage")
public class DataManagementController {

    @Autowired
    private CountryWiseLatestRepository countryRepo;
    @Autowired
    private DayWiseRepository dayWiseRepo;
    @Autowired
    private FullGroupedRepository fullGroupedRepo;
    @Autowired
    private WorldometerDataRepository worldometerRepo;
    @Autowired
    private CovidCleanCompleteRepository covidCleanRepo;
    @Autowired
    private UserRepository userRepo;

    @GetMapping("/{type}")
    public ResponseEntity<?> getAll(@PathVariable String type, 
                                    @RequestParam(defaultValue = "0") int page, 
                                    @RequestParam(defaultValue = "50") int size) {
        try {
            org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
            switch (type.toLowerCase()) {
                case "country-wise": return ResponseEntity.ok(countryRepo.findAll()); 
                case "day-wise": return ResponseEntity.ok(dayWiseRepo.findAll());
                case "full-grouped": return ResponseEntity.ok(fullGroupedRepo.findAll(pageable).getContent());
                case "worldometer": return ResponseEntity.ok(worldometerRepo.findAll());
                case "clean-complete": return ResponseEntity.ok(covidCleanRepo.findAll(pageable).getContent()); 
                case "users": return ResponseEntity.ok(userRepo.findAll());
                default: return ResponseEntity.badRequest().body("Unknown dataset type: " + type);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error fetching data: " + e.getMessage());
        }
    }

    @DeleteMapping("/{type}/{id}")
    public ResponseEntity<?> delete(@PathVariable String type, @PathVariable String id) {
        try {
            switch (type.toLowerCase()) {
                case "country-wise":
                    countryRepo.deleteById(id);
                    break;
                case "day-wise":
                    dayWiseRepo.deleteById(id);
                    break;
                case "full-grouped":
                    fullGroupedRepo.deleteById(Long.parseLong(id));
                    break;
                case "worldometer":
                    worldometerRepo.deleteById(id);
                    break;
                case "clean-complete":
                    covidCleanRepo.deleteById(Long.parseLong(id));
                    break;
                case "users":
                    userRepo.deleteById(Long.parseLong(id));
                    break;
                default:
                    return ResponseEntity.badRequest().body("Unknown dataset type");
            }
            return ResponseEntity.ok("Deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting: " + e.getMessage());
        }
    }

    @PostMapping("/{type}")
    public ResponseEntity<?> create(@PathVariable String type, @RequestBody Map<String, Object> payload) {
        return ResponseEntity.status(501).body("Create not fully implemented yet");
    }

    @PutMapping("/{type}/{id}")
    public ResponseEntity<?> update(@PathVariable String type, @PathVariable String id, @RequestBody Map<String, Object> payload) {
        try {
            switch (type.toLowerCase()) {
                case "country-wise": return updateCountryWise(id, payload);
                case "day-wise": return updateDayWise(id, payload);
                case "full-grouped": return updateFullGrouped(Long.parseLong(id), payload);
                case "worldometer": return updateWorldometer(id, payload);
                case "clean-complete": return updateCovidClean(Long.parseLong(id), payload);
                case "users": return updateUser(Long.parseLong(id), payload);
                default: return ResponseEntity.badRequest().body("Unknown type");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Update failed: " + e.getMessage());
        }
    }

    private ResponseEntity<?> updateCountryWise(String id, Map<String, Object> payload) {
        Optional<CountryWiseLatest> opt = countryRepo.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        CountryWiseLatest entity = opt.get();
        if (payload.containsKey("confirmed")) entity.setConfirmed(Long.valueOf(payload.get("confirmed").toString()));
        if (payload.containsKey("deaths")) entity.setDeaths(Long.valueOf(payload.get("deaths").toString()));
        if (payload.containsKey("recovered")) entity.setRecovered(Long.valueOf(payload.get("recovered").toString()));
        if (payload.containsKey("active")) entity.setActive(Long.valueOf(payload.get("active").toString()));
        countryRepo.save(entity);
        return ResponseEntity.ok(entity);
    }

    private ResponseEntity<?> updateDayWise(String id, Map<String, Object> payload) {
        Optional<DayWise> opt = dayWiseRepo.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        DayWise entity = opt.get();
        if (payload.containsKey("confirmed")) entity.setConfirmed(Long.valueOf(payload.get("confirmed").toString()));
        if (payload.containsKey("deaths")) entity.setDeaths(Long.valueOf(payload.get("deaths").toString()));
        if (payload.containsKey("recovered")) entity.setRecovered(Long.valueOf(payload.get("recovered").toString()));
        if (payload.containsKey("active")) entity.setActive(Long.valueOf(payload.get("active").toString()));
        dayWiseRepo.save(entity);
        return ResponseEntity.ok(entity);
    }

    private ResponseEntity<?> updateFullGrouped(Long id, Map<String, Object> payload) {
        Optional<FullGrouped> opt = fullGroupedRepo.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        FullGrouped entity = opt.get();
        if (payload.containsKey("confirmed")) entity.setConfirmed(Long.valueOf(payload.get("confirmed").toString()));
        if (payload.containsKey("deaths")) entity.setDeaths(Long.valueOf(payload.get("deaths").toString()));
        if (payload.containsKey("recovered")) entity.setRecovered(Long.valueOf(payload.get("recovered").toString()));
        if (payload.containsKey("active")) entity.setActive(Long.valueOf(payload.get("active").toString()));
        fullGroupedRepo.save(entity);
        return ResponseEntity.ok(entity);
    }

    private ResponseEntity<?> updateWorldometer(String id, Map<String, Object> payload) {
        Optional<WorldometerData> opt = worldometerRepo.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        WorldometerData entity = opt.get();
        if (payload.containsKey("totalCases")) entity.setTotalCases(Long.valueOf(payload.get("totalCases").toString()));
        if (payload.containsKey("totalDeaths")) entity.setTotalDeaths(Long.valueOf(payload.get("totalDeaths").toString()));
        if (payload.containsKey("totalRecovered")) entity.setTotalRecovered(Long.valueOf(payload.get("totalRecovered").toString()));
        if (payload.containsKey("activeCases")) entity.setActiveCases(Long.valueOf(payload.get("activeCases").toString()));
        worldometerRepo.save(entity);
        return ResponseEntity.ok(entity);
    }

    private ResponseEntity<?> updateCovidClean(Long id, Map<String, Object> payload) {
        Optional<CovidCleanComplete> opt = covidCleanRepo.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        CovidCleanComplete entity = opt.get();
        if (payload.containsKey("confirmed")) entity.setConfirmed(Long.valueOf(payload.get("confirmed").toString()));
        if (payload.containsKey("deaths")) entity.setDeaths(Long.valueOf(payload.get("deaths").toString()));
        if (payload.containsKey("recovered")) entity.setRecovered(Long.valueOf(payload.get("recovered").toString()));
        if (payload.containsKey("active")) entity.setActive(Long.valueOf(payload.get("active").toString()));
        covidCleanRepo.save(entity);
        return ResponseEntity.ok(entity);
    }

    private ResponseEntity<?> updateUser(Long id, Map<String, Object> payload) {
        Optional<User> opt = userRepo.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        User entity = opt.get();
        if (payload.containsKey("name")) entity.setName(payload.get("name").toString());
        if (payload.containsKey("email")) entity.setEmail(payload.get("email").toString());
        userRepo.save(entity);
        return ResponseEntity.ok(entity);
    }
}
