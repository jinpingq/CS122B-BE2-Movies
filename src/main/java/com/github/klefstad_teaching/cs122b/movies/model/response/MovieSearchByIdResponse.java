package com.github.klefstad_teaching.cs122b.movies.model.response;

import com.github.klefstad_teaching.cs122b.core.base.ResponseModel;
import com.github.klefstad_teaching.cs122b.movies.model.data.Genre;
import com.github.klefstad_teaching.cs122b.movies.model.data.Movie;
import com.github.klefstad_teaching.cs122b.movies.model.data.MovieDetail;
import com.github.klefstad_teaching.cs122b.movies.model.data.Person;

import java.util.List;

public class MovieSearchByIdResponse extends ResponseModel {
    private MovieDetail movies;
    private List<Genre> genres;
    private List<Person> persons;

    public MovieDetail getMovieDetail() {
        return movies;
    }

    public MovieSearchByIdResponse setMovieDetail(MovieDetail movieDetail) {
        this.movies = movieDetail;
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
