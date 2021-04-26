package com.gianvittorio.reactor.service;

import com.gianvittorio.reactor.domain.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ReviewServiceTest {

    private ReviewService reviewService;

    private WebClient webClient;

    @BeforeEach
    public void setUp() {
        webClient = WebClient.builder()
                .baseUrl("http://localhost:8080/movies")
                .build();

        reviewService = new ReviewService(webClient);
    }

    @Test
    public void retrieveReviewsRestClientTest() {
        // Given
        final long movieInfoId = 1l;

        // When
        Flux<Review> reviewFlux = reviewService.retrieveReviewsFluxRestClient(movieInfoId);

        // Then
        StepVerifier.create(reviewFlux.log())
                .expectSubscription()
                .assertNext(movie -> {
                    assertThat(movie.getReviewId())
                            .isEqualTo(1l);
                    assertThat(movie.getRating())
                            .isEqualTo(8.2);
                    assertThat(movie.getComment())
                            .isEqualTo("Nolan is the real superhero");
                })
                .expectComplete();
    }
}
