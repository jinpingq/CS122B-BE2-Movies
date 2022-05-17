package com.github.klefstad_teaching.cs122b.movies.repo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.klefstad_teaching.cs122b.core.error.ResultError;
import com.github.klefstad_teaching.cs122b.core.result.MoviesResults;
import com.github.klefstad_teaching.cs122b.movies.model.data.Movie;
import com.github.klefstad_teaching.cs122b.movies.model.data.MovieOrderBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Types;
import java.util.List;
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
            "SELECT DISTINCT id,title,year,director_id,rating,num_votes,budget,revenue,overview,backdrop_path,poster_path,hidden " +
                    "FROM movie ";

    private final static String MOVIE_WITH_GENRE =
            "SELECT m.id,m.title,m.year,m.director_id,m.rating,m.num_votes,m.budget,m.revenue,m.overview,backdrop_path,m.poster_path,m.hidden,genre_id,g.name " +
                    "FROM movie m " +
                    "JOIN movie_genre mg on m.id = mg.movie_id " +
                    "JOIN genre g on g.id = mg.genre_id ";


    public List<Movie> movieSearch(Optional<String> title, Optional<Integer> year, Optional<String> director,
                                   Optional<String> genre, Optional<Integer> limit, Optional<Integer> page,
                                   Optional<String> orderBy, Optional<String> direction) {
        StringBuilder sql;
        MapSqlParameterSource source = new MapSqlParameterSource();
        boolean whereAdded = false;

        if (genre.isPresent()) {
            sql = new StringBuilder(MOVIE_WITH_GENRE);
            sql.append("WHERE m.genre LIKE :genre ");

            String wildcardSearch = "%" + genre + "%";
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
            String wildcardSearch = "%" + title + "%";
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
            source.addValue("year", year, Types.INTEGER);
        }

        if (director.isPresent()) {
            if (whereAdded) {
                sql.append(" AND ");
            } else {
                sql.append(" WHERE ");
                whereAdded = true;
            }
            String wildcardSearch = "%" + director + "%";
            sql.append(" m.director LIKE :director ");
            source.addValue("director", wildcardSearch, Types.VARCHAR);
        }

        String order;
        String direct;
        if (orderBy.isPresent()) {
            order = orderBy.get();
        } else
            order = "TITLE";
        if (direction.isPresent())
            direct = direction.get();
        else
            direct = "ASC";

        MovieOrderBy orderby =
                MovieOrderBy.fromString(order, direct);
        sql.append(orderby.toSql());


        Integer offset = (page.get() - 1) * limit.get();
        String limit_sql = " LIMIT " + limit + " OFFSET " + offset;
        sql.append(limit_sql);

        try {
            List<Movie> movies = this.template.query(
                    sql.toString(),
                    source,
                    (rs, rowNum) ->
                            new Movie()
                                    .setId(rs.getLong("id"))
                                    .setTitle(rs.getString("title"))
                                    .setYear(rs.getInt("year"))
                                    .setDirector(rs.getString("director"))
                                    .setRating(rs.getDouble("rating"))
                                    .setBackdropPath(rs.getString("backdropPath"))
                                    .setPosterPath(rs.getString("posterPath"))
                                    .setHidden(rs.getBoolean("hidden")));
            return movies;
        } catch (DataAccessException e) {
            throw new ResultError(MoviesResults.NO_MOVIES_FOUND_WITHIN_SEARCH);
        }

    }
}
