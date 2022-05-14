package com.github.klefstad_teaching.cs122b.movies.repo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Component
public class MovieRepo
{
    private NamedParameterJdbcTemplate template;
    private final ObjectMapper objectMapper;

    @Autowired
    public MovieRepo(ObjectMapper objectMapper, NamedParameterJdbcTemplate template)
    {
        this.objectMapper = objectMapper;
        this.template = template;
    }

    private final static String MOVIE_NO_GENRE =
            "SELECT DISTINCT id,title,year,director_id,rating,num_votes,budget,revenue,overview,backdrop_path,poster_path,hidden" +
                    "FROM movie";

    private final static String MOVIE_WITH_GENRE =
            "SELECT m.id,m.title,m.year,m.director_id,m.rating,m.num_votes,m.budget,m.revenue,m.overview,backdrop_path,m.poster_path,m.hidden,genre_id,g.name\n" +
                    "FROM movie m" +
                    "JOIN movie_genre mg on m.id = mg.movie_id" +
                    "JOIN genre g on g.id = mg.genre_id";

    public void dynamic_query(
            @RequestParam Optional<String> title,
            @RequestParam Optional<Integer> year,
            @RequestParam Optional<String> director,
            @RequestParam Optional<String> genre,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<String> orderby,
            @RequestParam Optional<String> direction)
    {
        StringBuilder sql;
        MapSqlParameterSource source = new MapSqlParameterSource();
        boolean whereAdded = false;

        if (genre.isPresent())
        {
            sql = new StringBuilder(MOVIE_WITH_GENRE);
            sql.append("WHERE m.genre LIKE :genre ");

            String wildcardSearch = "%" + genre + "%";
            source.addValue("genre", wildcardSearch, Types.VARCHAR);
            whereAdded = true;

        }
        else {
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


    }

//    public List<Person> personSearch(PersonPageRequest request)
//    {
//        StringBuilder sql;
//        MapSqlParameterSource source = new MapSqlParameterSource();
//        boolean whereAdded = false;
//
//        sql = new StringBuilder(PERSON_SEARCH);
//        if (request.getName() != null){
//            sql.append(" WHERE p.name LIKE :personName ");
//            String wildcardSearch = "%" + request.getName() + "%";
//            source.addValue("title", wildcardSearch, Types.VARCHAR);
//            whereAdded = true;
//        }
//    }
}
