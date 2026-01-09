package com.covid.dashboard.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Data
@Table(name = "day_wise")
public class DayWise {
    @Id
    @Column(name = "Date")
    private String date;

    @Column(name = "confirmed")
    private Long confirmed;

    @Column(name = "deaths")
    private Long deaths;

    @Column(name = "recovered")
    private Long recovered;

    @Column(name = "active")
    private Long active;

    @Column(name = "new cases")
    private Long newCases;

    @Column(name = "new deaths")
    private Long newDeaths;

    @Column(name = "new recovered")
    private Long newRecovered;

    @Column(name = "Deaths / 100 Cases")
    private Double deathsPer100Cases;

    @Column(name = "Recovered / 100 Cases")
    private Double recoveredPer100Cases;

    @Column(name = "Deaths / 100 Recovered")
    private Double deathsPer100Recovered;

    @Column(name = "No. of countries")
    private Integer noOfCountries;
}
