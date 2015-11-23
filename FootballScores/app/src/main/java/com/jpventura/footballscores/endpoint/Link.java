package com.jpventura.footballscores.endpoint;

import com.google.gson.Gson;

public class Link {
    public String href;

    @Override
    public String toString() {
        return new Gson().toJson(this).toString();
    }
}
