package com.gianvittorio.reactor.service;

import com.gianvittorio.reactor.domain.Review;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.List;

public class ReviewService {

    private WebClient webClient = null;

    public ReviewService(WebClient webClient) {
        this.webClient = webClient;
    }

    public ReviewService() {
    }

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

    public Flux<Review> retrieveReviewsFluxRestClient(long movieInfoId) {
//
//        URI uri = UriComponentsBuilder.fromUriString("/v1/reviews")
//                .queryParam("movieInfoId", movieInfoId)
//                .buildAndExpand()
//                .toUri();

        Flux<Review> reviewFlux = webClient.get()
                .uri("/v1/reviews?movieInfoId={id}", movieInfoId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Review.class);

        return reviewFlux;
    }
}
