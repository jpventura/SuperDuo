/*
 * Copyright 2015 Udacity, Inc.
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
package com.jpventura.footballscores.content;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jpventura.footballscores.content.DatabaseContract.ScoresTable;

public class ScoresDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Scores.db";
    private static final int DATABASE_VERSION = 3;

    public ScoresDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CreateScoresTable = "CREATE TABLE " + DatabaseContract.SCORES_TABLE + " ("
                + ScoresTable._ID + " INTEGER PRIMARY KEY,"
                + ScoresTable.DATE_COL + " TEXT NOT NULL,"
                + ScoresTable.TIME_COL + " INTEGER NOT NULL,"
                + ScoresTable.HOME_COL + " TEXT NOT NULL,"
                + ScoresTable.AWAY_COL + " TEXT NOT NULL,"
                + ScoresTable.LEAGUE_COL + " INTEGER NOT NULL,"
                + ScoresTable.HOME_GOALS_COL + " TEXT NOT NULL,"
                + ScoresTable.AWAY_GOALS_COL + " TEXT NOT NULL,"
                + ScoresTable.HOME_CREST_URL_COL + " TEXT NULL,"
                + ScoresTable.AWAY_CREST_URL_COL + " TEXT NULL,"
                + ScoresTable.MATCH_ID + " INTEGER NOT NULL,"
                + ScoresTable.MATCH_DAY + " INTEGER NOT NULL,"
                + " UNIQUE (" + ScoresTable.MATCH_ID + ") ON CONFLICT REPLACE"
                + " );";
        db.execSQL(CreateScoresTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.SCORES_TABLE);
    }
}
