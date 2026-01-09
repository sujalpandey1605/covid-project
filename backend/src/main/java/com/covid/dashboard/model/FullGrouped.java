package com.covid.dashboard.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Data
@Table(name = "full_grouped")
public class FullGrouped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Date")
    private String date;

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

    @Column(name = "WHO Region")
    private String whoRegion;
}
