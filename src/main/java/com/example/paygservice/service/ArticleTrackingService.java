package com.example.paygservice.service;

import com.example.paygservice.model.ReadingProgress;
import com.example.paygservice.repository.ArticleRepository;
import com.example.paygservice.repository.ReadingProgressRepository;
import com.example.paygservice.dtos.TrackingRequest;
import com.example.paygservice.dtos.ReadingProgressDTO;
import com.example.paygservice.exception.ResourceNotFoundException;
import com.example.paygservice.service.UserBalanceService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ArticleTrackingService {
    private final ReadingProgressRepository progressRepository;
    private final UserBalanceService balanceService;
    private final ArticleRepository articleRepository;
    
    public ArticleTrackingService(ReadingProgressRepository progressRepository, 
                                UserBalanceService balanceService,
                                ArticleRepository articleRepository) {
        this.progressRepository = progressRepository;
        this.balanceService = balanceService;
        this.articleRepository = articleRepository;
    }

    public void trackReading(Long userId, Long articleId, TrackingRequest request) {
        ReadingProgress progress = progressRepository.findByUserIdAndArticleId(userId, articleId)
                .orElse(new ReadingProgress(userId, articleId));
        
        progress.setProgress(request.getProgress());
        progress.setCompleted(request.getCompleted());
        progress.setLastReadTime(LocalDateTime.now());
        
        progressRepository.save(progress);
    }
    
    public ReadingProgressDTO getProgress(Long userId, Long articleId) {
        ReadingProgress progress = progressRepository.findByUserIdAndArticleId(userId, articleId)
            .orElseThrow(() -> new ResourceNotFoundException("Reading progress not found"));
            
        return ReadingProgressDTO.builder()
            .userId(progress.getUserId())
            .articleId(progress.getArticleId())
            .progressPercentage(progress.getProgressPercentage())
            .timeSpentSeconds(progress.getTimeSpentSeconds())
            .lastReadPosition(progress.getLastReadPosition())
            .lastReadAt(progress.getUpdatedAt())
            .build();
    }
} 