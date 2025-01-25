package com.example.paygservice.dtos;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ReadingProgressDTO {
    private Long userId;
    private Long articleId;
    private Integer progressPercentage;
    private Long timeSpentSeconds;
    private Integer lastReadPosition;
    private LocalDateTime lastReadAt;
} 