package com.inai.medTech;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalysisImageRepository extends JpaRepository<AnalysisImageEntity, Long> {
}
