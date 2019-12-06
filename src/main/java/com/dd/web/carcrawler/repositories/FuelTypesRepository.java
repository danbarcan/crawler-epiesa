package com.dd.web.carcrawler.repositories;

import com.dd.web.carcrawler.entities.FuelType;
import com.dd.web.carcrawler.entities.Power;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuelTypesRepository extends JpaRepository<FuelType, Long> {
}
