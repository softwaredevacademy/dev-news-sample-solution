package com.example.devnews.repository;

import com.example.devnews.model.Article;
import com.example.devnews.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByTopics(Topic topic);

}
