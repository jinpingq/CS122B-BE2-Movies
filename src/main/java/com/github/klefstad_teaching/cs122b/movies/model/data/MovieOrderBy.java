package com.github.klefstad_teaching.cs122b.movies.model.data;

import com.github.klefstad_teaching.cs122b.core.error.ResultError;
import com.github.klefstad_teaching.cs122b.core.result.MoviesResults;

import java.util.Locale;
import java.util.Optional;

import static com.github.klefstad_teaching.cs122b.core.result.MoviesResults.*;

public enum MovieOrderBy {

    TITLE(" ORDER BY m.title, m.id "),
    RATING(" ORDER BY m.rating, m.id "),
    YEAR(" ORDER BY m.year, m.id "),
    TITLE_DESC(" ORDER BY m.title DESC, m.id "),
    RATING_DESC(" ORDER BY m.rating DESC, m.id "),
    YEAR_DESC(" ORDER BY m.year DESC, m.id ");
    private final String sql;

    MovieOrderBy(String sql) { this.sql = sql; }

    public String toSql(){ return sql; }

    public static MovieOrderBy fromString(String orderby, String direction, String secondOrder)
    {
//        Locale.ROOT ??
        String str;
            str = orderby.toUpperCase(Locale.ROOT) + " " + secondOrder + " " + direction.toUpperCase(Locale.ROOT);

        switch (str)
        {
            case "TITLE ID ASC":
                return TITLE;
            case "RATING ID ASC":
                return RATING;
            case "YEAR ID ASC":
                return YEAR;
            case "TITLE ID DESC":
                return TITLE_DESC;
            case "RATING ID DESC":
                return RATING_DESC;
            case "YEAR ID DESC":
                return YEAR_DESC;
            default:
                throw new ResultError(MoviesResults.INVALID_ORDER_BY);
            }
        }
}

