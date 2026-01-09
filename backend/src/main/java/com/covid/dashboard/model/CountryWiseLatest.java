package com.covid.dashboard.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "country_wise_latest")
public class CountryWiseLatest {
    @Id
    @Column(name = "Country/Region")
    private String countryRegion;

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

    @Column(name = "confirmed last week")
    private Long confirmedLastWeek;

    @Column(name = "1 week change")
    private Long oneWeekChange;

    @Column(name = "1 week % increase")
    private Double oneWeekPercentIncrease;

    @Column(name = "WHO Region")
    private String whoRegion;
}
