package com.jpventura.footballscores.endpoint;

import com.google.gson.Gson;

public class Team {
    public String code;

    public String crestUrl;

    public String name;

    public String shortName;

    public String squadMarketValue;

    @Override
    public String toString() {
        return new Gson().toJson(this).toString();
    }
}
