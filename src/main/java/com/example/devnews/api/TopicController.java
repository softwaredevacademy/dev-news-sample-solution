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
public class TopicController {

    TopicRepository topicRepository;
    ArticleRepository articleRepository;

    @Autowired
    public TopicController(TopicRepository topicRepository, ArticleRepository articleRepository) {
        this.topicRepository = topicRepository;
        this.articleRepository = articleRepository;
    }

    @GetMapping("/topics")
    public ResponseEntity<List<Topic>> listTopics() {
        return ResponseEntity.ok(topicRepository.findAll());
    }

    @GetMapping("/articles/{articleId}/topics")
    public ResponseEntity<List<Topic>> listTopicsOnArticle(@PathVariable Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(ResourceNotFoundException::new);
        return ResponseEntity.ok(article.getTopics());
    }

    @PostMapping("/topics")
    public ResponseEntity<Topic> createTopic(@Valid @RequestBody Topic topic) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(topicRepository.save(topic));
    }

    @PostMapping("/articles/{articleId}/topics")
    public ResponseEntity<Topic> createTopicOnArticle(@PathVariable Long articleId, @Valid @RequestBody Topic topicParams) {
        Article article = articleRepository.findById(articleId).orElseThrow(ResourceNotFoundException::new);
        Topic topic = topicRepository.findByName(topicParams.getName())
                .orElse(topicParams);
        List<Article> topicArticles = topic.getArticles();
        topicArticles.add(article);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(topicRepository.save(topic));
    }

    @PutMapping("/topics/{id}")
    public ResponseEntity<Topic> updateTopic(@PathVariable Long id, @Valid @RequestBody Topic updatedTopic) {
        topicRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        updatedTopic.setId(id);
        return ResponseEntity.ok(topicRepository.save(updatedTopic));
    }

    @DeleteMapping("/topics/{id}")
    public ResponseEntity<Topic> deleteTopic(@PathVariable Long id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        topicRepository.delete(topic);
        return ResponseEntity.ok(topic);
    }

    @DeleteMapping("/articles/{articleId}/topics/{topicId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTopicOnArticle(@PathVariable Long articleId, @PathVariable Long topicId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(ResourceNotFoundException::new);
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow((ResourceNotFoundException::new));

        if (topic.getArticles().contains(article)) {
            topic.getArticles().remove(article);
            topicRepository.save(topic);
        } else {
            throw new ResourceNotFoundException();
        }
    }

}
