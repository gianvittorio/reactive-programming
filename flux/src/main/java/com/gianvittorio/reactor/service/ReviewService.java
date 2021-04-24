package com.gianvittorio.reactor.service;

import com.gianvittorio.reactor.domain.Review;
import reactor.core.publisher.Flux;

import java.util.List;

public class ReviewService {

    public List<Review> retrieveReviews(long movieInfoId) {
        return List.of(
                Review.builder()
                        .reviewId(2l)
                        .movieInfoId(movieInfoId)
                        .comment("Excellent movie")
                        .rating(9.)
                        .build()
        );
    }

    public Flux<Review> retrieveReviewsFlux(long movieInfoId) {
        List<Review> reviewList = List.of(
                Review.builder()
                        .reviewId(2l)
                        .movieInfoId(movieInfoId)
                        .comment("Excellent movie")
                        .rating(9.)
                        .build()
        );

        return Flux.fromIterable(reviewList);
    }
}
