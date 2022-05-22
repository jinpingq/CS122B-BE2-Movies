package com.github.klefstad_teaching.cs122b.movies.rest;

import com.github.klefstad_teaching.cs122b.core.result.MoviesResults;
import com.github.klefstad_teaching.cs122b.core.security.JWTManager;
import com.github.klefstad_teaching.cs122b.movies.model.data.Movie;
import com.github.klefstad_teaching.cs122b.movies.model.data.Person;
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

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@RestController
public class PersonController
{
    private final MovieRepo repo;
    private final Validate validate;

    @Autowired
    public PersonController(MovieRepo repo, Validate validate)
    {
        this.repo = repo;
        this.validate = validate;
    }

    @GetMapping("/person/search")
    public ResponseEntity<MovieSearchResponse> personSearch(
            @AuthenticationPrincipal SignedJWT user,
            @RequestParam Optional<String> name,
            @RequestParam Optional<String> birthday,
            @RequestParam Optional<String> movieTitle,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> orderBy,
            @RequestParam Optional<String> direction) throws ParseException {

        Integer limit_val = validate.limitValidate(limit);
        Integer page_val = validate.pageValidate(page);
        String direction_str = validate.directionValidate(direction);
        String orderBy_str = validate.orderByValidate(orderBy);

        List<Person> persons = repo.personSearch(title, year, director,genre,
                    limit_val, page_val, orderBy_str,direction_str);


        MovieSearchResponse response = new MovieSearchResponse();
        response.setMovies(movies);
        response.setResult(MoviesResults.MOVIES_FOUND_WITHIN_SEARCH);
        return response.toResponse();
    }
}
