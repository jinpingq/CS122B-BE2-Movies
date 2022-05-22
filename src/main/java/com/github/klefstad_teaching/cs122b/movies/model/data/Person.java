package com.github.klefstad_teaching.cs122b.movies.model.data;

import org.springframework.lang.Nullable;

public class Person {
    private Long id;
    private String name;
    private @Nullable String birthday;
    private @Nullable String biography;
    private @Nullable String birthplace;
    private @Nullable Float popularity;
    private @Nullable String profilePath;

    public Long getId() {
        return id;
    }

    public Person setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Person setName(String name) {
        this.name = name;
        return this;
    }

    @Nullable
    public String getBirthday() {
        return birthday;
    }

    public Person setBirthday(@Nullable String birthday) {
        this.birthday = birthday;
        return this;
    }

    @Nullable
    public String getBiography() {
        return biography;
    }

    public Person setBiography(@Nullable String biography) {
        if (biography != null) {
            biography = biography.replace("\r", "");
        }
        this.biography = biography;
        return this;
    }

    @Nullable
    public String getBirthplace() {
        return birthplace;
    }

    public Person setBirthplace(@Nullable String birthplace) {
        this.birthplace = birthplace;
        return this;
    }

    @Nullable
    public Float getPopularity() {
        return popularity;
    }

    public Person setPopularity(@Nullable Float popularity) {
        this.popularity = popularity;
        return this;
    }

    @Nullable
    public String getProfilePath() {
        return profilePath;
    }

    public Person setProfilePath(@Nullable String profilePath) {
        this.profilePath = profilePath;
        return this;
    }
}
