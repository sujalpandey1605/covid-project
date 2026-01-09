package com.covid.dashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/tables")
    public List<Map<String, Object>> showTables() {
        return jdbcTemplate.queryForList("SHOW TABLES");
    }

    @GetMapping("/columns")
    public List<Map<String, Object>> showColumns(@org.springframework.web.bind.annotation.RequestParam(defaultValue = "country_wise_latest") String tableName) {
        return jdbcTemplate.queryForList("SHOW COLUMNS FROM " + tableName);
    }

    @GetMapping("/raw")
    public List<Map<String, Object>> showRawData() {
        return jdbcTemplate.queryForList("SELECT * FROM country_wise_latest LIMIT 5");
    }
}
