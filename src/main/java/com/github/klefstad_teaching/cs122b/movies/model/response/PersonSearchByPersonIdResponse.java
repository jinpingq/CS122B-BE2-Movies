package com.github.klefstad_teaching.cs122b.movies.model.response;

import com.github.klefstad_teaching.cs122b.core.base.ResponseModel;
import com.github.klefstad_teaching.cs122b.movies.model.data.Person;

public class PersonSearchByPersonIdResponse extends ResponseModel {
    private Person person;

    public Person getPerson() {
        return person;
    }

    public PersonSearchByPersonIdResponse setPerson(Person person) {
        this.person = person;
        return this;
    }
}
