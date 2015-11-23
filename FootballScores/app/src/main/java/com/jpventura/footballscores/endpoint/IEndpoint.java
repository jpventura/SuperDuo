package com.jpventura.footballscores.endpoint;

import retrofit.http.GET;
import retrofit.http.Query;

public interface IEndpoint {
    public String BASE_URL = "http://api.football-data.org/alpha";

    @GET("/fixtures")
    ResultPage<Fixture> getFixtures(@Query("timeFrame") String timeFrame);
}
