package com.jpventura.footballscores.endpoint;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

public class Fixture {
    public static final String FIXTURES = IEndpoint.BASE_URL + "/fixtures/";
    public static final String SOCCER_SEASON = IEndpoint.BASE_URL + "/soccerseasons/";

    public Map<String, Link> _links;

    public String awayTeamName;

    public Date date;

    public String homeTeamName;

    public Result result;

    public String status;

    public Integer matchday;

    public String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setTimeZone(TimeZone.getDefault());
        return format.format(date);
    }

    public String getLeague() {
        return _links.get("soccerseason").href.replace(SOCCER_SEASON, "");
    }

    public String getMatchId() {
        return _links.get("self").href.replace(FIXTURES, "");
    }

    public String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        format.setTimeZone(TimeZone.getDefault());
        return format.format(date);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this).toString();
    }
}
