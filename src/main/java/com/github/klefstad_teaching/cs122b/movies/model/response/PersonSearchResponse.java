package com.github.klefstad_teaching.cs122b.movies.model.response;

import com.github.klefstad_teaching.cs122b.core.base.ResponseModel;
import com.github.klefstad_teaching.cs122b.movies.model.data.Person;

import java.util.List;

public class PersonSearchResponse extends ResponseModel {
    private List<Person> persons;

    public List<Person> getPersons() {
        return persons;
    }

    public PersonSearchResponse setPersons(List<Person> persons) {
        this.persons = persons;
        return this;
    }
}
