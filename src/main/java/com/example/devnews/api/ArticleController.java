package com.example.devnews.api;


import com.example.devnews.model.Article;
import com.example.devnews.repository.ArticleRepository;
import com.example.devnews.api.exception.ResourceNotFoundException;
import com.example.devnews.model.Topic;
import com.example.devnews.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ArticleController {

    ArticleRepository articleRepository;
    TopicRepository topicRepository;

    @Autowired
    public ArticleController(ArticleRepository articleRepository, TopicRepository topicRepository) {
        this.articleRepository = articleRepository;
        this.topicRepository = topicRepository;
    }

    @GetMapping("/articles")
    public ResponseEntity<List<Article>> listAllArticles(
            @RequestParam(
                    required = false,
                    value="someAttr") Long topicId) {
        return ResponseEntity.ok(articleRepository.findAll());
    }

    @GetMapping("/topics/{topicId}/articles")
    public ResponseEntity<List<Article>> listArticlesByTopic(@PathVariable Long topicId) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(ResourceNotFoundException::new);
        return ResponseEntity.ok(topic.getArticles());
    }

    @GetMapping("/articles/{id}")
    public ResponseEntity<Article> getArticle(@PathVariable Long id) {
        return ResponseEntity.ok(
                articleRepository.findById(id)
                        .orElseThrow(ResourceNotFoundException::new));
    }

    @PostMapping("/articles")
    public ResponseEntity<Article> createArticle(@Valid @RequestBody Article article) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleRepository.save(article));
    }

    @PutMapping("/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @Valid @RequestBody Article updatedArticle) {
        articleRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        updatedArticle.setId(id);
        return ResponseEntity.ok(articleRepository.save(updatedArticle));
    }

    @DeleteMapping("/articles/{id}")
    public ResponseEntity<Article> deleteArticle(@PathVariable Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        articleRepository.delete(article);
        return ResponseEntity.ok(article);
    }

}
