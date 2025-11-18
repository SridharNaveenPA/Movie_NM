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
    private final MovieService movieService;

    public ReviewService(ReviewRepository reviewRepository, MovieService movieService) {
        this.reviewRepository = reviewRepository;
        this.movieService = movieService;
    }

    public Review save(Review review) {
        Review savedReview = reviewRepository.save(review);
        movieService.updateMovieRating(review.getMovie().getId());
        return savedReview;
    }
    public void delete(Review review) {
        Long movieId = review.getMovie().getId();
        reviewRepository.delete(review);
        movieService.updateMovieRating(movieId);
    }
    public Optional<Review> findById(Long id) { return reviewRepository.findById(id); }
    public Optional<Review> findByIdAndUser(Long id, User user) { return reviewRepository.findByIdAndUser(id, user); }
    public List<Review> findByMovie(Movie movie) { return reviewRepository.findByMovie(movie); }
    public List<Review> findByUser(User user) { return reviewRepository.findByUser(user); }
}


