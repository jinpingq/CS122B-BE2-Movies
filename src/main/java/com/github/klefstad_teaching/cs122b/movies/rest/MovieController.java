package com.github.klefstad_teaching.cs122b.movies.rest;

import com.github.klefstad_teaching.cs122b.core.security.JWTManager;
import com.github.klefstad_teaching.cs122b.movies.model.data.Genre;
import com.github.klefstad_teaching.cs122b.movies.model.data.Movie;
import com.github.klefstad_teaching.cs122b.movies.model.data.MovieDetail;
import com.github.klefstad_teaching.cs122b.movies.model.data.Person;
import com.github.klefstad_teaching.cs122b.movies.model.response.MovieSearchByIdResponse;
import com.github.klefstad_teaching.cs122b.movies.model.response.MovieSearchResponse;
import com.github.klefstad_teaching.cs122b.movies.repo.MovieRepo;
import com.github.klefstad_teaching.cs122b.movies.util.Validate;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.github.klefstad_teaching.cs122b.core.result.MoviesResults;

import java.text.ParseException;
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
            @RequestParam Optional<String> orderBy,
            @RequestParam Optional<String> direction) throws ParseException {

        List<String> roles = user.getJWTClaimsSet().getStringListClaim(JWTManager.CLAIM_ROLES);
        List<Movie> movies;
        Integer limit_val = validate.limitValidate(limit);
        Integer page_val = validate.pageValidate(page);
        String direction_str = validate.directionValidate(direction);
        String orderBy_str = validate.orderByValidate(orderBy);

        if (roles.contains("ADMIN") || roles.contains("EMPLOYEE"))
             movies = repo.movieSearch(title, year, director,genre,
                limit_val, page_val, orderBy_str,direction_str, true);
        else
            movies = repo.movieSearch(title, year, director,genre,
                    limit_val, page_val, orderBy_str,direction_str, false);

        MovieSearchResponse response = new MovieSearchResponse();
        response.setMovies(movies);
        response.setResult(MoviesResults.MOVIES_FOUND_WITHIN_SEARCH);
        return response.toResponse();
    }

    ///////////////////////////////////
    @GetMapping("/movie/search/person/{personId}")
    public ResponseEntity<MovieSearchResponse> movieSearchByPersonId(
            @AuthenticationPrincipal SignedJWT user,
            @PathVariable Long personId,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> orderBy,
            @RequestParam Optional<String> direction) throws ParseException {

        List<String> roles = user.getJWTClaimsSet().getStringListClaim(JWTManager.CLAIM_ROLES);
        List<Movie> movies;
        Integer limit_val = validate.limitValidate(limit);
        Integer page_val = validate.pageValidate(page);
        String direction_str = validate.directionValidate(direction);
        String orderBy_str = validate.orderByValidate(orderBy);

        if (roles.contains("ADMIN") || roles.contains("EMPLOYEE"))
            movies = repo.movieSearchPersonId(personId, limit_val, page_val, orderBy_str,direction_str,true);
        else
            movies = repo.movieSearchPersonId(personId, limit_val, page_val, orderBy_str,direction_str,false);

        MovieSearchResponse response = new MovieSearchResponse();
        response.setMovies(movies);
        response.setResult(MoviesResults.MOVIES_WITH_PERSON_ID_FOUND);
        return response.toResponse();
    }

    //////////////////////////////////
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<MovieSearchByIdResponse> movieSearchByMovieId(
            @AuthenticationPrincipal SignedJWT user,
            @PathVariable Long movieId) throws ParseException {

        List<String> roles = user.getJWTClaimsSet().getStringListClaim(JWTManager.CLAIM_ROLES);
        MovieDetail movie;

        if (roles.contains("ADMIN") || roles.contains("EMPLOYEE"))
            movie = repo.movieSearchById(movieId,true);
        else
            movie = repo.movieSearchById(movieId,false);

        List<Genre> genres = repo.movieSearchForGenre(movieId);
        List<Person> persons = repo.movieSearchForPerson(movieId);
        MovieSearchByIdResponse response = new MovieSearchByIdResponse();
        response.setMovieDetail(movie);
        response.setGenres(genres);
        response.setPersons(persons);
        System.out.println("P888888888888");
        response.setResult(MoviesResults.MOVIE_WITH_ID_FOUND);
        System.out.println("P999999");
        return response.toResponse();
    }
}
