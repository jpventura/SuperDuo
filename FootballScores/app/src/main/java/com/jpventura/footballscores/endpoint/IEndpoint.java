package com.jpventura.footballscores.endpoint;

import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;
import retrofit.http.Query;

public interface IEndpoint {
    String BASE_URL = "http://api.football-data.org/alpha";

    @GET("/fixtures")
    ResultPage<Fixture> getFixtures(@Header("X-Auth-Token") String authToken, @Query("timeFrame") String timeFrame);

    @GET("/teams/{id}")
    Team getTeam(@Header("X-Auth-Token") String authToken, @Path("id") String id);
}
