package com.github.klefstad_teaching.cs122b.movies.model.response;

import com.github.klefstad_teaching.cs122b.core.base.ResponseModel;
import com.github.klefstad_teaching.cs122b.movies.model.data.*;

import java.util.List;

public class MovieSearchByIdResponse extends ResponseModel {
    private MovieDetail movie;
    private List<Genre> genres;
    private List<Persons> persons;

    public MovieDetail getMovie() {
        return movie;
    }

    public MovieSearchByIdResponse setMovie(MovieDetail movieDetail) {
        this.movie = movieDetail;
        return this;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public MovieSearchByIdResponse setGenres(List<Genre> genres) {
        this.genres = genres;
        return this;
    }

    public List<Persons> getPersons() {
        return persons;
    }

    public MovieSearchByIdResponse setPersons(List<Persons> persons) {
        this.persons = persons;
        return this;
    }
}
