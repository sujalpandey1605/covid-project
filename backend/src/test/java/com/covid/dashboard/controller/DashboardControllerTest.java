package com.covid.dashboard.controller;

import com.covid.dashboard.repository.CountryWiseLatestRepository;
import com.covid.dashboard.repository.CovidCleanCompleteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class DashboardControllerTest {
}