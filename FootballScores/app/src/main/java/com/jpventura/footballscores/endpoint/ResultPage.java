package com.jpventura.footballscores.endpoint;

import com.google.gson.Gson;

import java.util.Date;
import java.util.List;

public class ResultPage<T> {
    public Date timeFrameStart;

    public Date timeFrameEnd;

    public Integer count;

    public List<T> fixtures;

    @Override
    public String toString() {
        return new Gson().toJson(this).toString();
    }
}
