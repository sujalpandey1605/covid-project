package com.covid.dashboard.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Data
@Table(name = "covid_19_clean_complete")
public class CovidCleanComplete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Assuming no natural ID in this table, using surrogate

    @Column(name = "Province/State")
    private String provinceState;

    @Column(name = "Country/Region")
    private String countryRegion;

    @Column(name = "Lat")
    private Double lat;

    @Column(name = "Long")
    private Double lng;

    @Column(name = "Date")
    private String date;

    @Column(name = "Confirmed")
    private Long confirmed;

    @Column(name = "Deaths")
    private Long deaths;

    @Column(name = "Recovered")
    private Long recovered;

    @Column(name = "Active")
    private Long active;

    @Column(name = "WHO Region")
    private String whoRegion;
}
