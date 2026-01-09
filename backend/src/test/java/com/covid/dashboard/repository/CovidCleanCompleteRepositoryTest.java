package com.covid.dashboard.repository;

import com.covid.dashboard.model.CovidCleanComplete;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CovidCleanCompleteRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CovidCleanCompleteRepository covidCleanCompleteRepository;

    @Test
    void whenFindByCountryRegion_thenReturnCovidData() {
        // given
        CovidCleanComplete data = new CovidCleanComplete();
        data.setCountryRegion("TestCountry");
        data.setConfirmed(100L);
        data.setDeaths(10L);
        data.setRecovered(90L);
        data.setActive(0L);
        data.setDate("2020-01-22");
        entityManager.persist(data);
        entityManager.flush();

        // when
        List<CovidCleanComplete> found = covidCleanCompleteRepository.findByCountryRegion(data.getCountryRegion());

        // then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getCountryRegion()).isEqualTo(data.getCountryRegion());
    }
}
