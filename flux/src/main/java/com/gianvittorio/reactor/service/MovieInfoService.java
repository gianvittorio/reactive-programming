package com.gianvittorio.reactor.service;

import com.gianvittorio.reactor.domain.MovieInfo;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.gianvittorio.reactor.util.CommonUtil.delay;

public class MovieInfoService {

    private WebClient webClient = null;

    public MovieInfoService(WebClient webClient) {
        this.webClient = webClient;
    }

    public MovieInfoService() {
    }

    public Flux<MovieInfo> retrieveAllMovieInfoRestClient() {
        return webClient.get()
                .uri("/v1/movie_infos")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(MovieInfo.class);
    }

    public Mono<MovieInfo> retrieveAllMovieInfoByIdRestClient(long movieId) {
        return webClient.get()
                .uri("/v1/movie_infos/{id}", movieId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(MovieInfo.class);
    }

    public Flux<MovieInfo> retrieveMoviesFlux() {
        List<MovieInfo> movieInfoList = List.of(
                MovieInfo.builder().movieInfoId(100l).name("Batman Begins").year(2005).cast(List.of("Christian Bale", "Liam Neeson")).build(),
                MovieInfo.builder().movieInfoId(101l).name("The Dark Knight").year(2008).cast(List.of("Christian Bale", "Heath Ledger")).build(),
                MovieInfo.builder().movieInfoId(102l).name("Dark Knight Rises").year(2012).cast(List.of("Christian Bale", "Tom Hardy")).build()
        );

        return Flux.fromIterable(movieInfoList);
    }

    public Mono<MovieInfo> retrieveMovieInfoMonoUsingId(long movieId) {
        MovieInfo movie = MovieInfo.builder()
                .movieInfoId(movieId)
                .name("Batman Begins")
                .year(2005)
                .cast(List.of("Christian Bale", "Liam Neeson"))
                .build();

        return Mono.just(movie);
    }

    public List<MovieInfo> movieList() {
        delay(1000);

        return List.of(
                MovieInfo.builder().movieInfoId(100l).name("Batman Begins").year(2005).cast(List.of("Christian Bale", "Liam Neeson")).build()
        );
    }
}
