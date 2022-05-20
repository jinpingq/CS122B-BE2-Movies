package com.github.klefstad_teaching.cs122b.movies.model.response;

import com.github.klefstad_teaching.cs122b.core.base.ResponseModel;
import com.github.klefstad_teaching.cs122b.movies.model.data.Genre;
import com.github.klefstad_teaching.cs122b.movies.model.data.Movie;
import com.github.klefstad_teaching.cs122b.movies.model.data.Person;

import java.util.List;

public class MovieSearchByIdResponse extends ResponseModel {
    private Movie moive;
    private List<Genre> genres;
    private List<Person> persons;

    public Movie getMoive() {
        return moive;
    }

    public MovieSearchByIdResponse setMoive(Movie moive) {
        this.moive = moive;
        return this;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public MovieSearchByIdResponse setGenres(List<Genre> genres) {
        this.genres = genres;
        return this;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public MovieSearchByIdResponse setPersons(List<Person> persons) {
        this.persons = persons;
        return this;
    }
}
