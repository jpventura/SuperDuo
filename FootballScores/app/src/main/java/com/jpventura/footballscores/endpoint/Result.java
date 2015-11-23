package com.jpventura.footballscores.endpoint;

import com.google.gson.Gson;

public class Result {
    public Integer goalsAwayTeam;

    public Integer goalsHomeTeam;

    @Override
    public String toString() {
        return new Gson().toJson(this).toString();
    }
}
