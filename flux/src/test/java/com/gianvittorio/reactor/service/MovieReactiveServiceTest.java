package com.gianvittorio.reactor.service;

import com.gianvittorio.reactor.domain.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

public class MovieReactiveServiceTest {

    private MovieInfoService movieInfoService;
    private ReviewService reviewService;
    private RevenueService revenueService;

    private MovieReactiveService movieReactiveService;

    @BeforeEach
    public void setUp() {
        movieInfoService = new MovieInfoService();
        reviewService = new ReviewService();
        revenueService = new RevenueService();
        movieReactiveService = new MovieReactiveService(movieInfoService, reviewService, revenueService);
    }

    @Test
    @DisplayName("Must return all movies.")
    public void getAllMoviesTest() {
        // Given

        // When
        Flux<Movie> moviesFlux = movieReactiveService.getAllMovies();

        // Then
        StepVerifier.create(moviesFlux.log())
                .expectSubscription()
                .assertNext(movie -> {
                    assertThat(movie.getMovieInfo().getName())
                            .isEqualTo("Batman Begins");
                    assertThat(movie.getReviewList().size())
                            .isEqualTo(1);

                })
                .assertNext(movie -> {
                    assertThat(movie.getMovieInfo().getName())
                            .isEqualTo("The Dark Knight");
                    assertThat(movie.getReviewList().size())
                            .isEqualTo(1);

                })
                .assertNext(movie -> {
                    assertThat(movie.getMovieInfo().getName())
                            .isEqualTo("Dark Knight Rises");
                    assertThat(movie.getReviewList().size())
                            .isEqualTo(1);

                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Must get moviw by id.")
    public void getMovieById() {
        // Given

        // When
        Mono<Movie> movieById = movieReactiveService.getMovieById(100l);

        // Then
        StepVerifier.create(movieById.log())
                .expectSubscription()
                .assertNext(movie -> {
                    assertThat(movie.getMovieInfo().getName())
                            .isEqualTo("Batman Begins");
                    assertThat(movie.getReviewList().size())
                            .isEqualTo(1);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Must get moviw by id.")
    public void getMovieByIdWithRevenue() {
        // Given

        // When
        Mono<Movie> movieById = movieReactiveService.getMovieByIdWithRevenue(100l);

        // Then
        StepVerifier.create(movieById.log())
                .expectSubscription()
                .assertNext(movie -> {
                    assertThat(movie.getMovieInfo().getName())
                            .isEqualTo("Batman Begins");
                    assertThat(movie.getReviewList().size())
                            .isEqualTo(1);
                    assertThat(movie.getRevenue())
                            .isNotNull();
                })
                .verifyComplete();
    }
}
