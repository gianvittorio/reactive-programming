package com.gianvittorio.reactor.service;

import com.gianvittorio.reactor.domain.Movie;
import com.gianvittorio.reactor.exception.MovieException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovieReactiveServiceMockTest {

    @Mock
    private MovieInfoService movieInfoService;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private MovieReactiveService movieReactiveService;

    @Test
    @DisplayName("Must return all movies.")
    public void getAllMoviesTest() {
        // Given

        // When
        when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();

//        when(reviewService.retrieveReviewsFlux(anyLong()))
//                .thenCallRealMethod();

        Flux<Movie> moviesFlux = movieReactiveService.getAllMovies();

        // Then
        StepVerifier.create(moviesFlux.log())
                .expectNextCount(3)
                .expectComplete();
    }

    @Test
    @DisplayName("Must return all movies.")
    public void getAllMoviesErrorTest() {
        // Given

        // When
        when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();

        String errorMessage = "Exception occurred int ReviewService";
        when(reviewService.retrieveReviewsFlux(anyLong()))
                .thenThrow(new RuntimeException(errorMessage));

        Flux<Movie> moviesFlux = movieReactiveService.getAllMovies();

        // Then
        StepVerifier.create(moviesFlux.log())
                .expectErrorMessage(errorMessage)
                .verify();
    }
}
