package com.jpventura.footballscores.endpoint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jpventura.footballscores.BuildConfig;

import java.util.Date;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.converter.GsonConverter;
import retrofit.http.Query;

public class Endpoint implements IEndpoint, RequestInterceptor {
    private static IEndpoint sEndpoint;
    private static final Object lock = new Object();

    private String mApiKey;
    private IEndpoint mEndpoint;

    public static IEndpoint getInstance(String apiKey) {
        synchronized (lock) {
            if (null == sEndpoint) {
                sEndpoint = new Endpoint(apiKey);
            }
        }

        return sEndpoint;
    }

    @Override
    public void intercept(RequestFacade request) {
        request.addHeader("X-Auth-Token", mApiKey);
    }

    @Override
    public ResultPage<Fixture> getFixtures(@Query("timeFrame") String timeFrame) {
        return mEndpoint.getFixtures(timeFrame);
    }

    private Endpoint(String apiKey) {
        mApiKey = apiKey;

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
