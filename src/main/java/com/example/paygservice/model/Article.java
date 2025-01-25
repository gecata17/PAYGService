package com.example.paygservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String content;
    private Double price;
    private String author;
    
    @Column(columnDefinition = "TEXT")
    private String description;

    // Custom setter and getter for id if needed
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
} 