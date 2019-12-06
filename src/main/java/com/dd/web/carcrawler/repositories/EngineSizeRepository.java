package com.dd.web.carcrawler.repositories;

import com.dd.web.carcrawler.entities.EngineSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EngineSizeRepository extends JpaRepository<EngineSize, Long> {
}
