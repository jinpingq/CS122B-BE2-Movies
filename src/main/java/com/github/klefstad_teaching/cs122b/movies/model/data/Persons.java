package com.github.klefstad_teaching.cs122b.movies.model.data;

public class Persons {
    private long id;
    private String name;

    public long getId() {
        return id;
    }

    public Persons setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Persons setName(String name) {
        this.name = name;
        return this;
    }
}
