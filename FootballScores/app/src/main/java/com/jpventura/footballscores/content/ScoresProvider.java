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

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.jpventura.footballscores.content.DatabaseContract.ScoresTable;

public class ScoresProvider extends ContentProvider {
    private static ScoresDBHelper mOpenHelper;
    private static final int MATCHES = 100;
    private static final int MATCHES_WITH_LEAGUE = 101;
    private static final int MATCHES_WITH_ID = 102;
    private static final int MATCHES_WITH_DATE = 103;

    private static final String SCORES_BY_LEAGUE = ScoresTable.LEAGUE_COL + " = ?";
    private static final String SCORES_BY_DATE = ScoresTable.DATE_COL + " LIKE ?";
    private static final String SCORES_BY_ID = ScoresTable.MATCH_ID + " = ?";

    private UriMatcher mUriMatcher = buildUriMatcher();

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DatabaseContract.BASE_CONTENT_URI.toString();
        matcher.addURI(authority, null , MATCHES);
        matcher.addURI(authority, "league" , MATCHES_WITH_LEAGUE);
        matcher.addURI(authority, "id" , MATCHES_WITH_ID);
        matcher.addURI(authority, "date" , MATCHES_WITH_DATE);
        return matcher;
    }

    private int matchUri(Uri uri) {
        String link = uri.toString();

        if(link.contentEquals(DatabaseContract.BASE_CONTENT_URI.toString())) {
            return MATCHES;
        } else if(link.contentEquals(ScoresTable.buildScoreWithDate().toString())) {
            return MATCHES_WITH_DATE;
        } else if(link.contentEquals(ScoresTable.buildScoreWithId().toString())) {
            return MATCHES_WITH_ID;
        } else if(link.contentEquals(ScoresTable.buildScoreWithLeague().toString())) {
            return MATCHES_WITH_LEAGUE;
        }

        return -1;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new ScoresDBHelper(getContext());
        return false;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        final int match = mUriMatcher.match(uri);

        switch (match) {
            case MATCHES:
                return ScoresTable.CONTENT_TYPE;
            case MATCHES_WITH_LEAGUE:
                return ScoresTable.CONTENT_TYPE;
            case MATCHES_WITH_ID:
                return ScoresTable.CONTENT_ITEM_TYPE;
            case MATCHES_WITH_DATE:
                return ScoresTable.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri :" + uri );
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String sel, String[] args, String order) {
        Cursor cursor;

        switch (matchUri(uri)) {
            case MATCHES:
                cursor = queryScores(projection, null, null, order);
                break;
            case MATCHES_WITH_DATE:
                cursor = queryScores(projection, SCORES_BY_DATE, args, order);
                break;
            case MATCHES_WITH_ID:
                cursor = queryScores(projection, SCORES_BY_ID, args, order);
                break;
            case MATCHES_WITH_LEAGUE:
                cursor = queryScores(projection, SCORES_BY_LEAGUE, args, order);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri" + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        switch (matchUri(uri)) {
            case MATCHES:
                getContext().getContentResolver().notifyChange(uri, null);
                return bulkInsertIntoScores(values);
            default:
                return super.bulkInsert(uri,values);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    private Cursor queryScores(String[] projection, String sel, String[] args, String order) {
        return mOpenHelper.getReadableDatabase()
                .query(DatabaseContract.SCORES_TABLE, projection, sel, args, null, null, order);
    }

    private int bulkInsertIntoScores(ContentValues[] values) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int ret = 0;

        try {
            db.beginTransaction();
            for(ContentValues value : values) {
                long id = db.insertWithOnConflict(DatabaseContract.SCORES_TABLE, null, value,
                        SQLiteDatabase.CONFLICT_REPLACE);
                if (-1 != id) ret++;
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return ret;
    }
}
