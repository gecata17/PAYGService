package com.example.paygservice.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackingRequest {
    private Integer progress;
    private Boolean completed;
    private Long timeSpent;  // in seconds
} 