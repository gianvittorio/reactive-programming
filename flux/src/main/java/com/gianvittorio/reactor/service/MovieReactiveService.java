package com.gianvittorio.reactor.service;

import com.gianvittorio.reactor.domain.Movie;
import com.gianvittorio.reactor.domain.MovieInfo;
import com.gianvittorio.reactor.domain.Review;
import com.gianvittorio.reactor.exception.MovieException;
import com.gianvittorio.reactor.exception.NetworkException;
import com.gianvittorio.reactor.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import java.time.Duration;
import java.util.List;

@Slf4j
public class MovieReactiveService {

    private final MovieInfoService movieInfoService;

    private final ReviewService reviewService;

    public MovieReactiveService(MovieInfoService movieInfoService, ReviewService reviewService) {
        this.movieInfoService = movieInfoService;
        this.reviewService = reviewService;
    }

    public Flux<Movie> getAllMovies() {
        Flux<MovieInfo> movieInfoFlux = movieInfoService.retrieveMoviesFlux();

        return movieInfoFlux.flatMap(movieInfo -> {
            Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                    .collectList();

            return reviewsMono.map(reviewsList -> Movie.builder().reviewList(reviewsList).movieInfo(movieInfo).build());
        })
                .onErrorMap(ex -> {
                    log.error("Exception is: " + ex);

                    throw new MovieException(ex.getMessage());
                });
    }

    public Flux<Movie> getAllMoviesWithRetry() {
        Flux<MovieInfo> movieInfoFlux = movieInfoService.retrieveMoviesFlux();

        return movieInfoFlux.flatMap(movieInfo -> {
            Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                    .collectList();

            return reviewsMono.map(reviewsList -> Movie.builder().reviewList(reviewsList).movieInfo(movieInfo).build());
        })
                .onErrorMap(ex -> {
                    log.error("Exception is: " + ex);

                    throw new MovieException(ex.getMessage());
                })
                .retry(3);
    }

    public Flux<Movie> getAllMoviesWithRetryWhen() {
        Flux<MovieInfo> movieInfoFlux = movieInfoService.retrieveMoviesFlux();

        return movieInfoFlux.flatMap(movieInfo -> {
            Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                    .collectList();

            return reviewsMono.map(reviewsList -> Movie.builder().reviewList(reviewsList).movieInfo(movieInfo).build());
        })
                .onErrorMap(ex -> {
                    log.error("Exception is: " + ex);

                    if (ex instanceof NetworkException) {
                        throw new MovieException(ex.getMessage());
                    }

                    throw new ServiceException(ex.getMessage());
                })
                .retryWhen(
                        getRetrySpec()
                );
    }

    public Flux<Movie> getAllMoviesWithRepeat() {
        Flux<MovieInfo> movieInfoFlux = movieInfoService.retrieveMoviesFlux();

        return movieInfoFlux.flatMap(movieInfo -> {
            Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                    .collectList();

            return reviewsMono.map(reviewsList -> Movie.builder().reviewList(reviewsList).movieInfo(movieInfo).build());
        })
                .onErrorMap(ex -> {
                    log.error("Exception is: " + ex);

                    if (ex instanceof NetworkException) {
                        throw new MovieException(ex.getMessage());
                    }

                    throw new ServiceException(ex.getMessage());
                })
                .retryWhen(
                        getRetrySpec()
                )
                .repeat();
    }

    public Flux<Movie> getAllMoviesWithRepeat(long times) {
        Flux<MovieInfo> movieInfoFlux = movieInfoService.retrieveMoviesFlux();

        return movieInfoFlux.flatMap(movieInfo -> {
            Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                    .collectList();

            return reviewsMono.map(reviewsList -> Movie.builder().reviewList(reviewsList).movieInfo(movieInfo).build());
        })
                .onErrorMap(ex -> {
                    log.error("Exception is: " + ex);

                    if (ex instanceof NetworkException) {
                        throw new MovieException(ex.getMessage());
                    }

                    throw new ServiceException(ex.getMessage());
                })
                .retryWhen(
                        getRetrySpec()
                )
                .repeat(times);
    }

    private static RetryBackoffSpec getRetrySpec() {
        return Retry.fixedDelay(3, Duration.ofMillis(500))
                .filter(ex -> ex instanceof MovieException)
                .onRetryExhaustedThrow(
                        (retryBackoffSpec, retrySignal) -> Exceptions.propagate(retrySignal.failure())
                );
    }

    public Mono<Movie> getMovieById(long movieId) {

        return Mono.just(movieId)
                .flatMap(movieInfoService::retrieveMovieInfoMonoUsingId)
                .flatMap(movieInfo -> {
                    Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                            .collectList();

                    return reviewsMono.map(reviews -> Movie.builder().reviewList(reviews).movieInfo(movieInfo).build());
                });
    }
}
