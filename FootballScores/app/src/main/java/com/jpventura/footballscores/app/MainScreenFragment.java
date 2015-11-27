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
package com.jpventura.footballscores.app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.jpventura.footballscores.content.DatabaseContract;
import com.jpventura.footballscores.R;
import com.jpventura.footballscores.widget.ViewHolder;
import com.jpventura.footballscores.widget.ScoresAdapter;
import com.jpventura.footballscores.service.GetFootballScoresService;

public class MainScreenFragment extends Fragment
        implements LoaderCallbacks<Cursor>, OnItemClickListener, OnItemShareListener {
    public ScoresAdapter mAdapter;
    public static final int SCORES_LOADER = 0;
    private String[] mDate = new String[1];

    public MainScreenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle instanceState) {
        update_scores();
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final ListView scoreList = (ListView) rootView.findViewById(R.id.scores_list);
        mAdapter = new ScoresAdapter(getActivity(), null, 0);
        mAdapter.setOnItemShareListener(this);
        scoreList.setAdapter(mAdapter);
        getLoaderManager().initLoader(SCORES_LOADER, null, this);
        mAdapter.detail_match_id = MainActivity.selected_match_id;
        scoreList.setOnItemClickListener(this);
        scoreList.setEmptyView(rootView.findViewById(R.id.empty_view));
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        Uri uri = DatabaseContract.ScoresTable.buildScoreWithDate();
        return new CursorLoader(getActivity(), uri, null, null, mDate, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ViewHolder selected = (ViewHolder) view.getTag();
        mAdapter.detail_match_id = selected.matchId;
        MainActivity.selected_match_id = (int) selected.matchId;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemShare(String item) {
    }

    private void update_scores() {
        Intent service_start = new Intent(getActivity(), GetFootballScoresService.class);
        getActivity().startService(service_start);
    }

    public void setFragmentDate(String date) {
        mDate[0] = date;
    }
}
