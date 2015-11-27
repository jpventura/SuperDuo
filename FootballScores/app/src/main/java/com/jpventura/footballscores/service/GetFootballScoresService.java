/*
 * Copyright 2015 Joao Paulo Fernandes Ventura.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jpventura.footballscores.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jpventura.footballscores.content.DatabaseContract;
import com.jpventura.footballscores.R;
import com.jpventura.footballscores.endpoint.Endpoint;
import com.jpventura.footballscores.endpoint.Fixture;
import com.jpventura.footballscores.endpoint.IEndpoint;
import com.jpventura.footballscores.endpoint.Team;

public class GetFootballScoresService extends IntentService {
    public static final String TAG = GetFootballScoresService.class.getSimpleName();

    public GetFootballScoresService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        insertTimeFramedFixtures("n2");
        insertTimeFramedFixtures("p2");
    }

    private void insertTimeFramedFixtures(String timeFrame) {
        ContentValues[] values = getInterestingFixtures(timeFrame);
        getContentResolver().bulkInsert(DatabaseContract.BASE_CONTENT_URI, values);
    }

    /**
     * Get from server interesting fixtures for a given time frame.
     *
     * @param timeFrame season time frame.
     * @return ContentValues array of interesting fixtures.
     */
    private ContentValues[] getInterestingFixtures(String timeFrame) {
        IEndpoint endpoint = Endpoint.getInstance();
        List<Fixture> interestingFixtures = new ArrayList<>();
        Map<String, Team> teams = new HashMap<>();

        try {
            String authToken = getString(R.string.api_key);
            for (Fixture fixture : endpoint.getFixtures(authToken, timeFrame).fixtures) {
                if (!isInterestedLeague(fixture.getLeagueId())) continue;
                interestingFixtures.add(fixture);
                teams.put(fixture.awayTeamName, endpoint.getTeam(authToken, fixture.getAwayTeamId()));
                teams.put(fixture.homeTeamName, endpoint.getTeam(authToken, fixture.getHomeTeamId()));
            }
        } catch (retrofit.RetrofitError e) {
            Log.e(TAG, "Too many request. Create your authentication key.");
        }

        ContentValues[] values = new ContentValues[interestingFixtures.size()];
        for (int i = 0; i < interestingFixtures.size(); i++) {
            values[i] = contentValuesFromFixture(interestingFixtures.get(i), teams);
        }

        return values;
    }

    /**
     * Check if league is one of which we are interested.
     *
     * @param league UEFA league name.
     * @return true if we are interested, false otherwise.
     */
    private static boolean isInterestedLeague(String league) {
        final String BUNDES_LEAGUE_1 = "394";
        final String BUNDES_LEAGUE_2 = "395";
        final String PREMIER_LEAGUE = "398";
        final String PRIMERA_DIVISION = "399";
        final String SERIE_A = "401";

        if (league.equals(PREMIER_LEAGUE) || league.equals(SERIE_A)) {
            return true;
        }

        if (league.equals(BUNDES_LEAGUE_1) || league.equals(BUNDES_LEAGUE_2)) {
            return true;
        }

        if (league.equals(PRIMERA_DIVISION)) {
            return true;
        }

        return false;
    }

    private ContentValues contentValuesFromFixture(Fixture fixture, Map<String, Team> teams) {
        ContentValues values = new ContentValues();

        values.put(DatabaseContract.ScoresTable.MATCH_ID, fixture.getMatchId());
        values.put(DatabaseContract.ScoresTable.DATE_COL, fixture.getDate());
        values.put(DatabaseContract.ScoresTable.TIME_COL, fixture.getTime());
        values.put(DatabaseContract.ScoresTable.HOME_COL, fixture.homeTeamName);
        values.put(DatabaseContract.ScoresTable.AWAY_COL, fixture.awayTeamName);
        values.put(DatabaseContract.ScoresTable.HOME_GOALS_COL, fixture.result.goalsHomeTeam);
        values.put(DatabaseContract.ScoresTable.AWAY_GOALS_COL, fixture.result.goalsAwayTeam);
        values.put(DatabaseContract.ScoresTable.LEAGUE_COL, fixture.getLeagueId());
        values.put(DatabaseContract.ScoresTable.MATCH_DAY, fixture.matchday);

        if (teams.containsKey(fixture.homeTeamName)) {
            values.put(DatabaseContract.ScoresTable.HOME_CREST_URL_COL, teams.get(fixture.homeTeamName).crestUrl);
        }

        if (teams.containsKey(fixture.awayTeamName)) {
            values.put(DatabaseContract.ScoresTable.AWAY_CREST_URL_COL, teams.get(fixture.awayTeamName).crestUrl);
        }

        return values;
    }
}
