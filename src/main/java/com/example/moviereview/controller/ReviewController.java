package com.example.moviereview.controller;

import com.example.moviereview.model.Movie;
import com.example.moviereview.model.Review;
import com.example.moviereview.model.User;
import com.example.moviereview.service.MovieService;
import com.example.moviereview.service.ReviewService;
import com.example.moviereview.service.UserService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin
public class ReviewController {
    private final ReviewService reviewService;
    private final MovieService movieService;
    private final UserService userService;

    public ReviewController(ReviewService reviewService, MovieService movieService, UserService userService) {
        this.reviewService = reviewService;
        this.movieService = movieService;
        this.userService = userService;
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<Map<String, Object>>> listByMovie(@PathVariable Long movieId) {
        Movie movie = movieService.findById(movieId).orElse(null);
        if (movie == null) return ResponseEntity.notFound().build();
        List<Map<String, Object>> items = reviewService.findByMovie(movie).stream().map(r -> Map.<String, Object>of(
                "review_id", r.getId(),
                "username", r.getUser().getUsername(),
                "rating", r.getRating(),
                "comment", r.getComment(),
                "created_at", r.getCreatedAt().toString()
        )).collect(Collectors.toList());
        return ResponseEntity.ok(items);
    }

    @PostMapping
    public ResponseEntity<?> create(
            @AuthenticationPrincipal(expression = "username") String username,
            @RequestBody Map<String, Object> body
    ) {
        if (username == null) return ResponseEntity.status(401).build();
        User user = userService.findByUsername(username).orElseThrow();
        Long movieId = Long.valueOf(body.get("movie_id").toString());
        Integer rating = Integer.valueOf(body.get("rating").toString());
        if (rating < 1 || rating > 5) return ResponseEntity.badRequest().body("Rating must be 1-5");
        String comment = body.get("comment") == null ? null : body.get("comment").toString();
        Movie movie = movieService.findById(movieId).orElse(null);
        if (movie == null) return ResponseEntity.notFound().build();
        Review r = new Review();
        r.setMovie(movie);
        r.setUser(user);
        r.setRating(rating);
        r.setComment(comment);
        return ResponseEntity.ok(reviewService.save(r));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @AuthenticationPrincipal(expression = "username") String username,
            @PathVariable Long id,
            @RequestBody Map<String, Object> body
    ) {
        if (username == null) return ResponseEntity.status(401).build();
        User user = userService.findByUsername(username).orElseThrow();
        return reviewService.findByIdAndUser(id, user)
                .map(r -> {
                    if (body.containsKey("rating")) {
                        int rating = Integer.parseInt(body.get("rating").toString());
                        if (rating < 1 || rating > 5) return ResponseEntity.badRequest().body("Rating must be 1-5");
                        r.setRating(rating);
                    }
                    if (body.containsKey("comment")) {
                        r.setComment(body.get("comment") == null ? null : body.get("comment").toString());
                    }
                    return ResponseEntity.ok(reviewService.save(r));
                })
                .orElse(ResponseEntity.status(403).body("Not allowed"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @AuthenticationPrincipal(expression = "username") String username,
            @PathVariable Long id
    ) {
        if (username == null) return ResponseEntity.status(401).build();
        User user = userService.findByUsername(username).orElseThrow();
        return reviewService.findByIdAndUser(id, user)
                .map(r -> {
                    reviewService.delete(r);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.status(403).body("Not allowed"));
    }
}


