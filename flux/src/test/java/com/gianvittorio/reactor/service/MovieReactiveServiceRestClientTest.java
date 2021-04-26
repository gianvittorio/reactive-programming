package com.gianvittorio.reactor.service;

import com.gianvittorio.reactor.domain.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class MovieReactiveServiceRestClientTest {

    private MovieInfoService movieInfoService;
    private ReviewService reviewService;

    private MovieReactiveService movieReactiveService;

    @BeforeEach
    public void setUp() {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8080/movies")
                .build();

        movieInfoService = new MovieInfoService(webClient);
        reviewService = new ReviewService(webClient);

        movieReactiveService = new MovieReactiveService(movieInfoService, reviewService);
    }

    @Test
    public void getAllMoviesRestClient() {
        // Given

        // When
        Flux<Movie> movieFlux = movieReactiveService.getAllMoviesRestClient();

        // Then
        StepVerifier.create(movieFlux.log())
                .expectSubscription()
                .expectNextCount(7)
                .expectComplete();
    }

    @Test
    public void getAllMoviesByIdRestClient() {
        // Given
        final long movieId = 1l;

        // When
        Mono<Movie> movieFlux = movieReactiveService.getMovieByIdRestClient(movieId);

        // Then
        StepVerifier.create(movieFlux.log())
                .expectSubscription()
                .expectNextCount(1)
                .expectComplete();
    }
}
