package com.github.klefstad_teaching.cs122b.movies.repo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.klefstad_teaching.cs122b.core.error.ResultError;
import com.github.klefstad_teaching.cs122b.core.result.MoviesResults;
import com.github.klefstad_teaching.cs122b.movies.model.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Types;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.github.klefstad_teaching.cs122b.core.result.MoviesResults.INVALID_DIRECTION;

@Component
public class MovieRepo {
    private NamedParameterJdbcTemplate template;
    private final ObjectMapper objectMapper;

    @Autowired
    public MovieRepo(ObjectMapper objectMapper, NamedParameterJdbcTemplate template) {
        this.objectMapper = objectMapper;
        this.template = template;
    }
    public NamedParameterJdbcTemplate getTemplate() {
        return template;
    }

    private final static String MOVIE_NO_GENRE =
            "SELECT DISTINCT m.id,m.title,m.year,p.name,m.rating,m.num_votes,m.budget,m.revenue, " +
                    " m.overview,m.backdrop_path,m.poster_path,m.hidden " +
                    " FROM movies.movie m " +
                    " JOIN movies.person p on m.director_id = p.id ";


    private final static String MOVIE_WITH_GENRE =
            MOVIE_NO_GENRE +
                    "JOIN movies.movie_genre mg on m.id = mg.movie_id " +
                    "JOIN movies.genre g on g.id = mg.genre_id ";

    private final static String MOVIE_WITH_PERSON =
            MOVIE_NO_GENRE +
                    " JOIN movies.movie_person mp on m.id = mp.movie_id ";

    public List<Movie> movieSearch(Optional<String> title, Optional<Integer> year, Optional<String> director,
                                   Optional<String> genre, Integer limit, Integer page,
                                   String orderBy, String direction, Boolean role_advanced) {
        StringBuilder sql;
        MapSqlParameterSource source = new MapSqlParameterSource();
        boolean whereAdded = false;

        if (genre.isPresent()) {
            sql = new StringBuilder(MOVIE_WITH_GENRE);
            sql.append("WHERE g.name LIKE :genre ");

            String wildcardSearch = "%" + genre.get() + "%";
            source.addValue("genre", wildcardSearch, Types.VARCHAR);
            whereAdded = true;

        } else {
            sql = new StringBuilder(MOVIE_NO_GENRE);
        }
        if (title.isPresent()) {
            if (whereAdded) {
                sql.append(" AND ");
            } else {
                sql.append(" WHERE ");
                whereAdded = true;
            }
            String wildcardSearch = "%" + title.get() + "%";
            sql.append(" m.title LIKE :title ");
            source.addValue("title", wildcardSearch, Types.VARCHAR);
        }
        if (year.isPresent()) {
            if (whereAdded) {
                sql.append(" AND ");
            } else {
                sql.append(" WHERE ");
                whereAdded = true;
            }
            sql.append(" m.year = :year ");
            source.addValue("year", year.get(), Types.INTEGER);
        }

        if (director.isPresent()) {
            if (whereAdded) {
                sql.append(" AND ");
            } else {
                sql.append(" WHERE ");
                whereAdded = true;
            }
            String wildcardSearch = "%" + director.get() + "%";
            sql.append(" name LIKE :director ");
            source.addValue("director", wildcardSearch, Types.VARCHAR);
        }

        if (!role_advanced) {
            if (whereAdded) {
                sql.append(" AND ");
            } else {
                sql.append(" WHERE ");
                whereAdded = true;
            }
            sql.append(" hidden = false ");
        }

        MovieOrderBy orderby =
                MovieOrderBy.fromString(orderBy, direction,"ID");
        sql.append(orderby.toSql());

        Integer offset = (page - 1) * limit;

        sql.append(" LIMIT " + limit + " OFFSET " + offset);

        try {
            List<Movie> movies = this.template.query(
                    sql.toString(),
                    source,
                    (rs, rowNum) ->
                            new Movie()
                                    .setHidden(rs.getBoolean("hidden"))
                                    .setYear(rs.getInt("year"))
                                    .setDirector(rs.getString("name"))
                                    .setRating(rs.getDouble("rating"))
                                    .setId(rs.getLong("id"))
                                    .setBackdropPath(rs.getString("backdrop_path"))
                                    .setTitle(rs.getString("title"))
                                    .setPosterPath(rs.getString("poster_path")));
            if (movies.isEmpty())
                throw new ResultError(MoviesResults.NO_MOVIES_FOUND_WITHIN_SEARCH);
            return movies;
        } catch (DataAccessException e) {
            throw new ResultError(MoviesResults.NO_MOVIES_FOUND_WITHIN_SEARCH);
        }
    }




    public List<Movie> movieSearchPersonId(Long personId, Integer limit, Integer page,
                                           String orderBy, String direction, Boolean role_advanced)
    {
        StringBuilder sql = new StringBuilder(MOVIE_WITH_PERSON);
        MapSqlParameterSource source = new MapSqlParameterSource();
        sql.append(" WHERE  mp.person_id = :person_id");
        source.addValue("person_id", personId, Types.INTEGER);

        if (! role_advanced)
            sql.append(" AND hidden = false ");

        MovieOrderBy orderby =
                MovieOrderBy.fromString(orderBy, direction, "ID");
        sql.append(orderby.toSql());
        Integer offset = (page - 1) * limit;
        sql.append(" LIMIT " + limit + " OFFSET " + offset);
        try {
            List<Movie> movies = this.template.query(
                    sql.toString(),
                    source,
                    (rs, rowNum) ->
                            new Movie()
                                    .setId(rs.getLong("id"))
                                    .setTitle(rs.getString("title"))
                                    .setYear(rs.getInt("year"))
                                    .setDirector(rs.getString("name"))
                                    .setRating(rs.getDouble("rating"))
                                    .setBackdropPath(rs.getString("backdrop_path"))
                                    .setPosterPath(rs.getString("poster_path"))
                                    .setHidden(rs.getBoolean("hidden")));
            if (movies.isEmpty())
                throw new ResultError(MoviesResults.NO_MOVIES_WITH_PERSON_ID_FOUND);
            return movies;
        } catch (DataAccessException e) {
            throw new ResultError(MoviesResults.NO_MOVIES_WITH_PERSON_ID_FOUND);
        }
    }

    public MovieDetail movieSearchById(Long movieId, Boolean role_advanced)
    {
        StringBuilder sql = new StringBuilder(MOVIE_NO_GENRE);
        MapSqlParameterSource source = new MapSqlParameterSource();
        sql.append(" WHERE  m.id = :id");
        source.addValue("id", movieId, Types.INTEGER);

        if (!role_advanced)
            sql.append(" AND hidden = false ");
        System.out.println("Movie SQL PRINT: "  + sql);
        try {
            MovieDetail movies = this.template.queryForObject(
                    sql.toString(),
                    source,
                    (rs, rowNum) ->
                            new MovieDetail()
                                    .setId(rs.getLong("id"))
                                    .setTitle(rs.getString("title"))
                                    .setYear(rs.getInt("year"))
                                    .setDirector(rs.getString("name"))
                                    .setRating(rs.getDouble("rating"))
                                    .setNumVotes(rs.getInt("num_votes"))
                                    .setBudget(rs.getLong("budget"))
                                    .setRevenue(rs.getLong("revenue"))
                                    .setOverview(rs.getString("overview"))
                                    .setBackdropPath(rs.getString("backdrop_path"))
                                    .setPosterPath(rs.getString("poster_path"))
                                    .setHidden(rs.getBoolean("hidden")));
            return movies;
        } catch (DataAccessException e) {
            throw new ResultError(MoviesResults.NO_MOVIE_WITH_ID_FOUND);
        }
    }

    public List<Genre> movieSearchForGenre(Long movieId)
    {
        String str = "SELECT DISTINCT mg.genre_id, g.name " +
                " FROM movies.movie_genre mg " +
                " JOIN movies.genre g on g.id = mg.genre_id ";
        StringBuilder sql = new StringBuilder(str);
        MapSqlParameterSource source = new MapSqlParameterSource();
        sql.append(" WHERE mg.movie_id = :movie_id");
        source.addValue("movie_id", movieId, Types.INTEGER);
        sql.append(" ORDER BY g.name ");
        System.out.println("Genre SQL PRINT: "  + sql);
        try {
            List<Genre> genres = this.template.query(
                    sql.toString(),
                    source,
                    (rs, rowNum) ->
                            new Genre()
                                    .setGenreId(rs.getLong("genre_id"))
                                    .setName(rs.getString("name")));
            return genres;
        } catch (DataAccessException e) {
            throw new ResultError(MoviesResults.NO_MOVIE_WITH_ID_FOUND);  // genre not found?
        }
    }

    public List<Person> movieSearchForPerson(Long movieId)
    {
        String str = "SELECT DISTINCT p.name, p.popularity, p.id, mp.movie_id " +
                " FROM movies.person p " +
                " JOIN movies.movie_person mp on p.id = mp.person_id ";
        StringBuilder sql = new StringBuilder(str);
        MapSqlParameterSource source = new MapSqlParameterSource();
        sql.append(" WHERE mp.movie_id = :movie_id ");
        source.addValue("movie_id", movieId, Types.INTEGER);
        sql.append(" ORDER BY p.popularity DESC, p.id ");
        System.out.println("Person SQL PRINT: "  + sql);
        try {
            List<Person> persons = this.template.query(
                    sql.toString(),
                    source,
                    (rs, rowNum) ->
                            new Person()
                                    .setPersonId(rs.getLong("id"))
                                    .setName(rs.getString("name")));
            return persons;
        } catch (DataAccessException e) {
            throw new ResultError(MoviesResults.NO_MOVIE_WITH_ID_FOUND);  // person not found?
        }
    }

    /////////////////PERSON SEARCH

}
