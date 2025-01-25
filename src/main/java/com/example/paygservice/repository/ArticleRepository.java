package com.example.paygservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.paygservice.model.Article;
import java.lang.Long;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
} 