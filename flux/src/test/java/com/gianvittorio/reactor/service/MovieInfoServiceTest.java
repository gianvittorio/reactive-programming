package com.gianvittorio.reactor.service;

import com.gianvittorio.reactor.domain.MovieInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MovieInfoServiceTest {

    private MovieInfoService movieInfoService;

    private WebClient webClient;

    @BeforeEach
    public void setUp() {
        webClient = WebClient.builder()
                .baseUrl("http://localhost:8080/movies")
                .build();

        movieInfoService = new MovieInfoService(webClient);
    }

    @Test
    public void retrieveAllMovieInfoRestClient() {
        // Given

        // When
        Flux<MovieInfo> movieInfoFlux = movieInfoService.retrieveAllMovieInfoRestClient();

        // Then
        StepVerifier.create(movieInfoFlux.log())
                .expectSubscription()
                .expectNextCount(7)
                .verifyComplete();
    }

    @Test
    public void retrieveAllMovieInfoByIdRestClient() {
        // Given
        final long movieId = 1l;

        // When
        Mono<MovieInfo> movieInfoMono = movieInfoService.retrieveAllMovieInfoByIdRestClient(movieId);

        // Then
        StepVerifier.create(movieInfoMono.log())
                .expectSubscription()
                .assertNext(consumer -> {
                    assertThat(consumer.getName())
                            .isEqualTo("Batman Begins");
                    assertThat(consumer.getCast().size())
                            .isEqualTo(2);
                    assertThat(consumer.getCast())
                            .isEqualTo(List.of("Christian Bale", "Michael Cane"));
                })
                .verifyComplete();
    }
}
