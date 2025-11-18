package com.example.moviereview.controller;

import com.example.moviereview.model.Movie;
import com.example.moviereview.model.Review;
import com.example.moviereview.service.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Integer year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        Page<Movie> result = movieService.search(q, genre, year, page, size);
        List<Map<String, Object>> items = result.getContent().stream().map(this::toSummary).collect(Collectors.toList());
        return ResponseEntity.ok(Map.<String, Object>of(
                "content", items,
                "page", result.getNumber(),
                "totalPages", result.getTotalPages(),
                "totalElements", result.getTotalElements()
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> get(@PathVariable Long id) {
        Movie m = movieService.findById(id).orElse(null);
        if (m == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toDetail(m));
    }

    @PostMapping
    public ResponseEntity<Movie> create(@RequestBody Movie movie) {
        return ResponseEntity.ok(movieService.save(movie));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Movie movie) {
        return movieService.findById(id)
                .map(existing -> {
                    existing.setTitle(movie.getTitle());
                    existing.setGenre(movie.getGenre());
                    existing.setReleaseYear(movie.getReleaseYear());
                    existing.setDescription(movie.getDescription());
                    existing.setPosterUrl(movie.getPosterUrl());
                    return ResponseEntity.ok(movieService.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        movieService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Map<String, Object> toSummary(Movie m) {
        DoubleSummaryStatistics stats = m.getReviews().stream()
                .collect(Collectors.summarizingDouble(r -> r.getRating() == null ? 0 : r.getRating()))
                ;
        double avg = m.getReviews().isEmpty() ? 0.0 : stats.getAverage();
        return Map.of(
                "movie_id", m.getId(),
                "title", m.getTitle(),
                "genre", m.getGenre(),
                "release_year", m.getReleaseYear(),
                "poster_url", m.getPosterUrl(),
                "director", m.getDirector(),
                "average_rating", Math.round(avg * 10.0) / 10.0
        );
    }

    private Map<String, Object> toDetail(Movie m) {
        List<Map<String, Object>> reviews = m.getReviews().stream().map(r -> Map.<String, Object>of(
                "review_id", r.getId(),
                "username", r.getUser().getUsername(),
                "rating", r.getRating(),
                "comment", r.getComment(),
                "created_at", r.getCreatedAt().toString()
        )).collect(Collectors.toList());
        Map<String, Object> summary = toSummary(m);
        return Map.of(
                "movie_id", m.getId(),
                "title", m.getTitle(),
                "genre", m.getGenre(),
                "release_year", m.getReleaseYear(),
                "description", m.getDescription(),
                "poster_url", m.getPosterUrl(),
                "director", m.getDirector(),
                "average_rating", summary.get("average_rating"),
                "reviews", reviews
        );
    }
}


