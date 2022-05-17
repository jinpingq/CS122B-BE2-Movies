package com.github.klefstad_teaching.cs122b.movies.model.data;

import com.github.klefstad_teaching.cs122b.core.error.ResultError;
import com.github.klefstad_teaching.cs122b.core.result.MoviesResults;

import java.util.Locale;

import static com.github.klefstad_teaching.cs122b.core.result.MoviesResults.*;

public enum MovieOrderBy {
    MOVIE(" ORDER BY m.title "),
    RATING(" ORDER BY m.rating "),
    YEAR(" ORDER BY m.year "),
    MOVIE_DESC(" ORDER BY m.title DESC "),
    RATING_DESC(" ORDER BY m.rating DESC "),
    YEAR_DESC(" ORDER BY m.year DESC ");

    private final String sql;

    MovieOrderBy(String sql) { this.sql = sql; }

    public String toSql(){ return sql; }

    public static MovieOrderBy fromString(String orderby, String direction)
    {
//        Locale.ROOT ??
        String str = orderby.toUpperCase(Locale.ROOT) + " " + direction.toUpperCase(Locale.ROOT);
        if (! (str.contains("ASC") || str.contains("DESC")))
            throw new ResultError(INVALID_DIRECTION);

        switch (str)
        {
            case "MOVIE ASC":
                return MOVIE;
            case "RATING ASC":
                return RATING;
            case "YEAR ASC":
                return YEAR;
            case "MOVIE DESC":
                return MOVIE_DESC;
            case "RATING DESC":
                return RATING_DESC;
            case "YEAR DESC":
                return YEAR_DESC;
            default:
                throw new ResultError(MoviesResults.INVALID_ORDER_BY);
            }
        }
}
