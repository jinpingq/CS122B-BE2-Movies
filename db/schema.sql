CREATE SCHEMA IF NOT EXISTS movies;

CREATE TABLE IF NOT EXISTS movies.genre
(
    id         INT            NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(256)   NOT NULL
    );

CREATE TABLE IF NOT EXISTS movies.person
(
    id    INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name  VARCHAR(256) NOT NULL UNIQUE,
    birthday INT          NOT NULL
    biography
    birthplace
    popularity
    profile_path
    );

CREATE TABLE IF NOT EXISTS movies.movie
(
    id INT NOT NULL,
    title VARCHAR(256) NOT NULL
    year INT NOT NULL
    director_id INT NOT NULL,
    rating INT NOT NULL,
    num_votes INT NOT NULL,
    budget INT NOT NULL,
    revenue INT NOT NULL,
    overview VARCHAR(256) NOT NULL
    backdrop_path VARCHAR(256) NOT NULL
    poster_path VARCHAR(256) NOT NULL
    hidden
    class_id   INT NOT NULL,
    PRIMARY KEY (student_id, class_id),
    FOREIGN KEY (student_id) REFERENCES activity.student (id)
    ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (class_id) REFERENCES activity.class (id)
    ON UPDATE CASCADE ON DELETE CASCADE
    );
