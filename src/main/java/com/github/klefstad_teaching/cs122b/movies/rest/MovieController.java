package com.github.klefstad_teaching.cs122b.movies.rest;

import com.github.klefstad_teaching.cs122b.movies.model.data.Movie;
import com.github.klefstad_teaching.cs122b.movies.model.response.MovieSearchResponse;
import com.github.klefstad_teaching.cs122b.movies.repo.MovieRepo;
import com.github.klefstad_teaching.cs122b.movies.util.Validate;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.github.klefstad_teaching.cs122b.core.result.MoviesResults;

import java.util.List;
import java.util.Optional;

@RestController
public class MovieController
{
    private final MovieRepo repo;
    private final Validate validate;

    @Autowired
    public MovieController(MovieRepo repo, Validate validate)
    {
        this.repo = repo;
        this.validate = validate;
    }

    @GetMapping("/movie/search")
    public ResponseEntity<MovieSearchResponse> movieSearch(
            @AuthenticationPrincipal SignedJWT user,
            @RequestParam Optional<String> title,
            @RequestParam Optional<Integer> year,
            @RequestParam Optional<String> director,
            @RequestParam Optional<String> genre,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> orderby,
            @RequestParam Optional<String> direction)
    {

        List<Movie> movies = repo.movieSearch(title, year, director,genre,
                limit, page, orderby,direction);

        System.out.println("DIRECTROR: " + director.get());
        MovieSearchResponse response = new MovieSearchResponse();
        response.setMovies(movies);
        response.setResult(MoviesResults.MOVIES_FOUND_WITHIN_SEARCH);
        return response.toResponse();
    }
}
