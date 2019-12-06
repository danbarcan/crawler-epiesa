package com.dd.web.carcrawler.repositories;

import com.dd.web.carcrawler.entities.Power;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PowersRepository extends JpaRepository<Power, Long> {
}
