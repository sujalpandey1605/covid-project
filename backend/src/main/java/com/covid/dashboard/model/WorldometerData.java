package com.covid.dashboard.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "worldometer_data")
public class WorldometerData {
    @Id
    @Column(name = "Country/Region")
    private String countryRegion;

    @Column(name = "Continent")
    private String continent;

    @Column(name = "Population")
    private Long population;

    @Column(name = "TotalCases")
    private Long totalCases;

    @Column(name = "NewCases")
    private Long newCases;

    @Column(name = "TotalDeaths")
    private Long totalDeaths;

    @Column(name = "NewDeaths")
    private Long newDeaths;

    @Column(name = "TotalRecovered")
    private Long totalRecovered;

    @Column(name = "NewRecovered")
    private Long newRecovered;

    @Column(name = "ActiveCases")
    private Long activeCases;

    @Column(name = "Serious,Critical")
    private Long seriousCritical;

    @Column(name = "Tot Cases/1M pop")
    private Double totalCasesPer1M;

    @Column(name = "Deaths/1M pop")
    private Double deathsPer1M;

    @Column(name = "TotalTests")
    private Long totalTests;

    @Column(name = "Tests/1M pop")
    private Double testsPer1M;

    @Column(name = "WHO Region")
    private String whoRegion;
}
