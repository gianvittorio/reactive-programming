package com.gianvittorio.reactor.service;

import com.gianvittorio.reactor.domain.Movie;
import com.gianvittorio.reactor.exception.MovieException;
import com.gianvittorio.reactor.exception.NetworkException;
import com.gianvittorio.reactor.exception.ServiceException;
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
import static org.mockito.Mockito.*;

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
    @DisplayName("Must throw MovieException.")
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

    @Test
    @DisplayName("Must keep retrying, at most 3 times, whenever throwing MovieException error.")
    public void getAllMoviesWithRetryTest() {
        // Given

        // When
        when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();

        String errorMessage = "Exception occurred int ReviewService";
        when(reviewService.retrieveReviewsFlux(anyLong()))
                .thenThrow(new RuntimeException(errorMessage));

        Flux<Movie> moviesFlux = movieReactiveService.getAllMoviesWithRetry();

        // Then
        StepVerifier.create(moviesFlux.log())
                .expectErrorMessage(errorMessage)
                .verify();

        verify(reviewService, times(4))
                .retrieveReviewsFlux(isA(Long.class));
    }

    @Test
    @DisplayName("Must keep retrying, at most 3 times, once every 500ms, whenever throwing MovieException error.")
    public void getAllMoviesWithRetryWhenTest() {
        // Given

        // When
        when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();

        String errorMessage = "Exception occurred int ReviewService";
        when(reviewService.retrieveReviewsFlux(anyLong()))
                .thenThrow(new NetworkException(errorMessage));

        Flux<Movie> moviesFlux = movieReactiveService.getAllMoviesWithRetryWhen();

        // Then
        StepVerifier.create(moviesFlux.log())
                .expectErrorMessage(errorMessage)
                .verify();

        verify(reviewService, times(4))
                .retrieveReviewsFlux(isA(Long.class));
    }

    @Test
    @DisplayName("Must keep retrying, at most 3 times, once every 500ms, whenever throwing MovieException error.")
    public void getAllMoviesWithRetryWhen1Test() {
        // Given

        // When
        when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();

        String errorMessage = "Exception occurred int ReviewService";
        when(reviewService.retrieveReviewsFlux(anyLong()))
                .thenThrow(new ServiceException(errorMessage));

        Flux<Movie> moviesFlux = movieReactiveService.getAllMoviesWithRetryWhen();

        // Then
        StepVerifier.create(moviesFlux.log())
                .expectErrorMessage(errorMessage)
                .verify();

        verify(reviewService, atMostOnce())
                .retrieveReviewsFlux(isA(Long.class));
    }

    @Test
    @DisplayName("Must keep retrying, at most 3 times, once every 500ms, whenever throwing MovieException error.")
    public void getAllMoviesWithRepeatTest() {
        // Given

        // When
        when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();

        when(reviewService.retrieveReviewsFlux(anyLong()))
                .thenCallRealMethod();

        Flux<Movie> moviesFlux = movieReactiveService.getAllMoviesWithRepeat();

        // Then
        StepVerifier.create(moviesFlux.log())
                .expectNextCount(6)
                .thenCancel()
                .verify();

        verify(reviewService, times(6))
                .retrieveReviewsFlux(isA(Long.class));
    }

    @Test
    @DisplayName("Must keep retrying, at most 3 times, once every 500ms, whenever throwing MovieException error.")
    public void getAllMoviesWithRepeatNumberOfTimesTest() {
        // Given
        final long numberOfTimes = 2l;

        // When
        when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();

        when(reviewService.retrieveReviewsFlux(anyLong()))
                .thenCallRealMethod();

        Flux<Movie> moviesFlux = movieReactiveService.getAllMoviesWithRepeat(numberOfTimes);

        // Then
        StepVerifier.create(moviesFlux.log())
                .expectNextCount(9)
                .verifyComplete();

        verify(reviewService, times(9))
                .retrieveReviewsFlux(isA(Long.class));
    }
}
