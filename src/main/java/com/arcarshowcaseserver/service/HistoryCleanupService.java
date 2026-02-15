package com.arcarshowcaseserver.service;

import com.arcarshowcaseserver.repository.RecommendationHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HistoryCleanupService {

    @Autowired
    private RecommendationHistoryRepository historyRepository;

    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanupOldHistory() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(7);
        historyRepository.deleteByShownAtBefore(cutoff);
    }
}
