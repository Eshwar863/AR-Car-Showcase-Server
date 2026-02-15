package com.arcarshowcaseserver.repository;

import com.arcarshowcaseserver.model.RecommendationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecommendationHistoryRepository extends JpaRepository<RecommendationHistory, Long> {

    @Query("SELECT DISTINCT rh.carId FROM RecommendationHistory rh WHERE rh.userId = :userId AND rh.shownAt >= :since")
    List<Long> findShownCarIdsByUserSince(@Param("userId") Long userId, @Param("since") LocalDateTime since);

    @Modifying
    @Transactional
    @Query("DELETE FROM RecommendationHistory rh WHERE rh.shownAt < :cutoff")
    void deleteByShownAtBefore(@Param("cutoff") LocalDateTime cutoff);
}
