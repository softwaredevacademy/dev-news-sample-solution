package com.example.devnews.api;

import com.example.devnews.model.Article;
import com.example.devnews.repository.ArticleRepository;
import com.example.devnews.model.Comment;
import com.example.devnews.repository.CommentRepository;
import com.example.devnews.api.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CommentController {

    CommentRepository commentRepository;
    ArticleRepository articleRepository;

    @Autowired
    public CommentController(CommentRepository commentRepository, ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
    }

    @GetMapping("/articles/{id}/comments")
    public ResponseEntity<List<Comment>> listAllComments(@PathVariable String id) {
        return ResponseEntity.ok(commentRepository.findAll());
    }

    @GetMapping(value = "/comments", params = {"authorName"})
    public ResponseEntity<List<Comment>> listCommentsByAuthorName(@RequestParam String authorName) {
        return ResponseEntity.ok(commentRepository.findByAuthorName(authorName));
    }

    @PostMapping("/articles/{id}/comments")
    public ResponseEntity<Comment> createCommentOnArticle(@PathVariable Long id, @Valid @RequestBody Comment comment) {
        Article article = articleRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        comment.setArticle(article);
        return ResponseEntity.ok(commentRepository.save(comment));
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @RequestBody Comment updatedComment) {
        Comment comment = commentRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        updatedComment.setId(comment.getId());
        updatedComment.setArticle(comment.getArticle());
        return ResponseEntity.ok(commentRepository.save(updatedComment));
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Comment> deleteComment(@PathVariable Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        commentRepository.delete(comment);
        return ResponseEntity.ok(comment);
    }
}
