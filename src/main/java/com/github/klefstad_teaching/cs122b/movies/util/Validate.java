package com.github.klefstad_teaching.cs122b.movies.util;

import com.github.klefstad_teaching.cs122b.core.error.ResultError;
import com.github.klefstad_teaching.cs122b.core.result.MoviesResults;
import com.github.klefstad_teaching.cs122b.movies.model.data.MovieOrderBy;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Optional;

@Component
public class Validate
{
    public Integer limitValidate(Optional<Integer> limit_request)
    {
        Integer limit = limit_request.orElse(10);
        if (!(limit == 10 || limit == 25 || limit == 50 || limit == 100))
            throw new ResultError(MoviesResults.INVALID_LIMIT);
        return limit;
    }

    public String directionValidate(Optional<String> direction_request)
    {
        String direct = direction_request.orElse("ASC");
        direct = direct.toUpperCase().trim();
        if (! (direct.equals("ASC") || direct.equals("DESC")))
            throw new ResultError(MoviesResults.INVALID_DIRECTION);
        return direct;
    }

    public Integer pageValidate(Optional<Integer> page_request)
    {
        Integer page_loc = page_request.orElse(1);
        if (page_loc <= 0)
            throw new ResultError(MoviesResults.INVALID_PAGE);
        return page_loc;
    }

    public String orderByValidate(Optional<String> orderby_request)
    {
        String order = orderby_request.orElse("TITLE");
        order = order.toUpperCase().trim();
        if (! (order.equals("TITLE") || order.equals("YEAR") || order.equals("RATING")))
            throw new ResultError(MoviesResults.INVALID_ORDER_BY);
        return order;
    }

    public String orderByForPersonValidate(Optional<String> orderby_request)
    {
        String order = orderby_request.orElse("NAME");
        order = order.toUpperCase().trim();
        if (! (order.equals("NAME") || order.equals("POPULARITY") || order.equals("BIRTHDAY")))
            throw new ResultError(MoviesResults.INVALID_ORDER_BY);
        return order;
    }
}
