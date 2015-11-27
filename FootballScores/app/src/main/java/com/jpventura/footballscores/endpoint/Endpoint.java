package com.jpventura.footballscores.endpoint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jpventura.footballscores.BuildConfig;

import java.util.Date;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.converter.GsonConverter;
import retrofit.http.Header;
import retrofit.http.Path;
import retrofit.http.Query;

public class Endpoint implements IEndpoint{
    private static IEndpoint sEndpoint;
    private static final Object lock = new Object();

    private IEndpoint mEndpoint;

    public static IEndpoint getInstance() {
        synchronized (lock) {
            if (null == sEndpoint) {
                sEndpoint = new Endpoint();
            }
        }

        return sEndpoint;
    }

    @Override
    public ResultPage<Fixture> getFixtures(@Header("X-Auth-Token") String authToken, @Query("timeFrame") String timeFrame) {
        return mEndpoint.getFixtures(authToken, timeFrame);
    }

    @Override
    public Team getTeam(@Header("X-Auth-Token") String authToken, @Path("id") String id) {
        return mEndpoint.getTeam(authToken, id);
    }

    private Endpoint() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(IEndpoint.BASE_URL)
                .setConverter(new GsonConverter(gson))
                .setLogLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE)
                .build();

        mEndpoint = restAdapter.create(IEndpoint.class);
    }
}
