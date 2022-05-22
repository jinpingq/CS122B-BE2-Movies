package com.github.klefstad_teaching.cs122b.movies.rest;

import com.github.klefstad_teaching.cs122b.core.result.MoviesResults;
import com.github.klefstad_teaching.cs122b.core.security.JWTManager;
import com.github.klefstad_teaching.cs122b.movies.model.data.Movie;
import com.github.klefstad_teaching.cs122b.movies.model.data.Person;
import com.github.klefstad_teaching.cs122b.movies.model.response.MovieSearchResponse;
import com.github.klefstad_teaching.cs122b.movies.model.response.PersonSearchByPersonIdResponse;
import com.github.klefstad_teaching.cs122b.movies.model.response.PersonSearchResponse;
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
    public ResponseEntity<PersonSearchResponse> personSearch(
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
        String orderBy_str = validate.orderByForPersonValidate(orderBy);

        List<Person> persons = repo.personSearch(name, birthday, movieTitle,
                    limit_val, page_val, orderBy_str,direction_str);

        PersonSearchResponse response = new PersonSearchResponse();
        response.setPersons(persons);
        response.setResult(MoviesResults.PERSONS_FOUND_WITHIN_SEARCH);
        return response.toResponse();
    }

    @GetMapping("/person/{personId}")
    public ResponseEntity<PersonSearchByPersonIdResponse> personSearchById(
            @AuthenticationPrincipal SignedJWT user,
            @PathVariable Long personId) throws ParseException {


        Person person = repo.personSearchByPersonId(personId);

        PersonSearchByPersonIdResponse response = new PersonSearchByPersonIdResponse();
        response.setPerson(person);
        response.setResult(MoviesResults.PERSON_WITH_ID_FOUND);
        return response.toResponse();
    }

}
