package com.example.paygservice.model;

import com.example.paygservice.model.Article;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadingProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;
    
    private Integer progress;
    private LocalDateTime lastReadTime;
    private Boolean completed;
    private Integer lastReadPosition;
    private Long timeSpentSeconds;

    public ReadingProgress(Long userId, Long articleId) {
        this.user = new User();
        this.user.setId(userId);
        this.article = new Article();
        this.article.setId(articleId);
        this.progress = 0;
        this.lastReadTime = LocalDateTime.now();
        this.completed = false;
        this.lastReadPosition = 0;
        this.timeSpentSeconds = 0L;
    }

    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    public Long getArticleId() {
        return article != null ? article.getId() : null;
    }

    public Integer getProgressPercentage() {
        return progress;
    }

    public Long getTimeSpentSeconds() {
        return timeSpentSeconds;
    }

    public Integer getLastReadPosition() {
        return lastReadPosition;
    }

    public LocalDateTime getUpdatedAt() {
        return lastReadTime;
    }
} 