package com.arcarshowcaseserver.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recommendation_history",
       indexes = {
           @Index(name = "idx_user_shown", columnList = "user_id,shown_at"),
           @Index(name = "idx_shown_at", columnList = "shown_at")
       })
public class RecommendationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "car_id", nullable = false)
    private Long carId;

    @Column(name = "shown_at", nullable = false)
    private LocalDateTime shownAt;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "action")
    private String action;

    public RecommendationHistory() {}

    public RecommendationHistory(Long userId, Long carId, String sessionId) {
        this.userId = userId;
        this.carId = carId;
        this.sessionId = sessionId;
        this.shownAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getCarId() { return carId; }
    public void setCarId(Long carId) { this.carId = carId; }

    public LocalDateTime getShownAt() { return shownAt; }
    public void setShownAt(LocalDateTime shownAt) { this.shownAt = shownAt; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
}
