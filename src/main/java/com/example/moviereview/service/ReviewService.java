package com.example.moviereview.service;

import com.example.moviereview.model.Movie;
import com.example.moviereview.model.Review;
import com.example.moviereview.model.User;
import com.example.moviereview.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review save(Review review) { return reviewRepository.save(review); }
    public void delete(Review review) { reviewRepository.delete(review); }
    public Optional<Review> findById(Long id) { return reviewRepository.findById(id); }
    public Optional<Review> findByIdAndUser(Long id, User user) { return reviewRepository.findByIdAndUser(id, user); }
    public List<Review> findByMovie(Movie movie) { return reviewRepository.findByMovie(movie); }
    public List<Review> findByUser(User user) { return reviewRepository.findByUser(user); }
}


