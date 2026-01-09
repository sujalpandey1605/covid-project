package com.covid.dashboard.service;

import com.covid.dashboard.model.*;
import com.covid.dashboard.repository.*;
import jakarta.annotation.PostConstruct;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataLoaderService {

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

    @PostConstruct
    public void loadData() {
        try {
            loadCountryWiseLatest();
            loadDayWise();
            loadFullGrouped();
            loadWorldometerData();
            loadCovidCleanComplete();
        } catch (Exception e) {
            System.err.println("Error loading CSV data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadCountryWiseLatest() {
        if (countryRepo.count() > 0) return;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource("country_wise_latest.csv").getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<CountryWiseLatest> list = new ArrayList<>();
            for (CSVRecord record : csvParser) {
                CountryWiseLatest entity = new CountryWiseLatest();
                entity.setCountryRegion(record.get("Country/Region"));
                entity.setConfirmed(parseLong(record, "Confirmed"));
                entity.setDeaths(parseLong(record, "Deaths"));
                entity.setRecovered(parseLong(record, "Recovered"));
                entity.setActive(parseLong(record, "Active"));
                entity.setNewCases(parseLong(record, "New cases"));
                entity.setNewDeaths(parseLong(record, "New deaths"));
                entity.setNewRecovered(parseLong(record, "New recovered"));
                entity.setDeathsPer100Cases(parseDouble(record, "Deaths / 100 Cases"));
                entity.setRecoveredPer100Cases(parseDouble(record, "Recovered / 100 Cases"));
                entity.setDeathsPer100Recovered(parseDouble(record, "Deaths / 100 Recovered"));
                entity.setConfirmedLastWeek(parseLong(record, "Confirmed last week"));
                entity.setOneWeekChange(parseLong(record, "1 week change"));
                entity.setOneWeekPercentIncrease(parseDouble(record, "1 week % increase"));
                entity.setWhoRegion(record.get("WHO Region"));
                list.add(entity);
            }
            countryRepo.saveAll(list);
            System.out.println("Loaded " + list.size() + " records into CountryWiseLatest");
        } catch (Exception e) {
            System.out.println("Failed to load country_wise_latest.csv: " + e.getMessage());
        }
    }

    private void loadDayWise() {
        if (dayWiseRepo.count() > 0) return;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource("day_wise.csv").getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<DayWise> list = new ArrayList<>();
            for (CSVRecord record : csvParser) {
                DayWise entity = new DayWise();
                entity.setDate(record.get("Date"));
                entity.setConfirmed(parseLong(record, "Confirmed"));
                entity.setDeaths(parseLong(record, "Deaths"));
                entity.setRecovered(parseLong(record, "Recovered"));
                entity.setActive(parseLong(record, "Active"));
                entity.setNewCases(parseLong(record, "New cases"));
                entity.setNewDeaths(parseLong(record, "New deaths"));
                entity.setNewRecovered(parseLong(record, "New recovered"));
                entity.setDeathsPer100Cases(parseDouble(record, "Deaths / 100 Cases"));
                entity.setRecoveredPer100Cases(parseDouble(record, "Recovered / 100 Cases"));
                entity.setDeathsPer100Recovered(parseDouble(record, "Deaths / 100 Recovered"));
                entity.setNoOfCountries(Integer.parseInt(record.get("No. of countries")));
                list.add(entity);
            }
            dayWiseRepo.saveAll(list);
            System.out.println("Loaded " + list.size() + " records into DayWise");
        } catch (Exception e) {
            System.out.println("Failed to load day_wise.csv: " + e.getMessage());
        }
    }

    private void loadFullGrouped() {
        if (fullGroupedRepo.count() > 0) return;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource("full_grouped.csv").getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<FullGrouped> list = new ArrayList<>();
            for (CSVRecord record : csvParser) {
                FullGrouped entity = new FullGrouped();
                entity.setDate(record.get("Date"));
                entity.setCountryRegion(record.get("Country/Region"));
                entity.setConfirmed(parseLong(record, "Confirmed"));
                entity.setDeaths(parseLong(record, "Deaths"));
                entity.setRecovered(parseLong(record, "Recovered"));
                entity.setActive(parseLong(record, "Active"));
                entity.setNewCases(parseLong(record, "New cases"));
                entity.setNewDeaths(parseLong(record, "New deaths"));
                entity.setNewRecovered(parseLong(record, "New recovered"));
                entity.setWhoRegion(record.get("WHO Region"));
                list.add(entity);
            }
            fullGroupedRepo.saveAll(list);
            System.out.println("Loaded " + list.size() + " records into FullGrouped");
        } catch (Exception e) {
            System.out.println("Failed to load full_grouped.csv: " + e.getMessage());
        }
    }

    private void loadWorldometerData() {
        if (worldometerRepo.count() > 0) return;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource("worldometer_data.csv").getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<WorldometerData> list = new ArrayList<>();
            for (CSVRecord record : csvParser) {
                WorldometerData entity = new WorldometerData();
                entity.setCountryRegion(record.get("Country/Region"));
                entity.setContinent(record.get("Continent"));
                entity.setPopulation(parseLong(record, "Population"));
                entity.setTotalCases(parseLong(record, "TotalCases"));
                entity.setNewCases(parseLong(record, "NewCases"));
                entity.setTotalDeaths(parseLong(record, "TotalDeaths"));
                entity.setNewDeaths(parseLong(record, "NewDeaths"));
                entity.setTotalRecovered(parseLong(record, "TotalRecovered"));
                entity.setNewRecovered(parseLong(record, "NewRecovered"));
                entity.setActiveCases(parseLong(record, "ActiveCases"));
                entity.setSeriousCritical(parseLong(record, "Serious,Critical"));
                entity.setTotalCasesPer1M(parseDouble(record, "Tot Cases/1M pop"));
                entity.setDeathsPer1M(parseDouble(record, "Deaths/1M pop"));
                entity.setTotalTests(parseLong(record, "TotalTests"));
                entity.setTestsPer1M(parseDouble(record, "Tests/1M pop"));
                entity.setWhoRegion(record.get("WHO Region"));
                list.add(entity);
            }
            worldometerRepo.saveAll(list);
            System.out.println("Loaded " + list.size() + " records into WorldometerData");
        } catch (Exception e) {
            System.out.println("Failed to load worldometer_data.csv: " + e.getMessage());
        }
    }

    private void loadCovidCleanComplete() {
        if (covidCleanRepo.count() > 0) return;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource("covid_19_clean_complete.csv").getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<CovidCleanComplete> list = new ArrayList<>();
            for (CSVRecord record : csvParser) {
                CovidCleanComplete entity = new CovidCleanComplete();
                entity.setProvinceState(record.get("Province/State"));
                entity.setCountryRegion(record.get("Country/Region"));
                entity.setLat(parseDouble(record, "Lat"));
                entity.setLng(parseDouble(record, "Long"));
                entity.setDate(record.get("Date"));
                entity.setConfirmed(parseLong(record, "Confirmed"));
                entity.setDeaths(parseLong(record, "Deaths"));
                entity.setRecovered(parseLong(record, "Recovered"));
                entity.setActive(parseLong(record, "Active"));
                entity.setWhoRegion(record.get("WHO Region"));
                list.add(entity);
            }
            covidCleanRepo.saveAll(list);
            System.out.println("Loaded " + list.size() + " records into CovidCleanComplete");
        } catch (Exception e) {
            System.out.println("Failed to load covid_19_clean_complete.csv: " + e.getMessage());
        }
    }

    private Long parseLong(CSVRecord record, String header) {
        try {
            String val = record.get(header);
            if (val == null || val.trim().isEmpty()) return 0L;
            return Long.parseLong(val);
        } catch (Exception e) {
            return 0L;
        }
    }

    private Double parseDouble(CSVRecord record, String header) {
        try {
            String val = record.get(header);
            if (val == null || val.trim().isEmpty()) return 0.0;
            return Double.parseDouble(val);
        } catch (Exception e) {
            return 0.0;
        }
    }
}
