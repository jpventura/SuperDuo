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
package com.jpventura.footballscores.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;
import com.jpventura.footballscores.R;
import com.jpventura.footballscores.Utility;
import com.jpventura.footballscores.app.OnItemShareListener;

import java.io.InputStream;

/**
 * Created by yehya khaled on 2/26/2015.
 */
public class ScoresAdapter extends CursorAdapter implements OnClickListener {
    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_DATE = 1;
    public static final int COL_LEAGUE = 5;
    public static final int COL_MATCHDAY = 9;
    public static final int COL_ID = 8;
    public static final int COL_MATCHTIME = 2;
    public double detail_match_id = 0;

    private static final Object lock = new Object();
    private static GenericRequestBuilder sRequestBuilder;

    private String FOOTBALL_SCORES_HASHTAG = "#Football_Scores";
    private OnItemShareListener mOnItemShareListener;

    public ScoresAdapter(Context context, Cursor cursor, int flags) {
        super(context,cursor,flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View mItem = LayoutInflater.from(context).inflate(R.layout.scores_list_item, parent, false);
        ViewHolder mHolder = new ViewHolder(mItem);
        mItem.setTag(mHolder);
        //Log.v(FetchScoreTask.LOG_TAG,"new View inflated");
        return mItem;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final ViewHolder mHolder = (ViewHolder) view.getTag();
        mHolder.homeName.setText(cursor.getString(COL_HOME));
        mHolder.awayName.setText(cursor.getString(COL_AWAY));
        mHolder.date.setText(cursor.getString(COL_MATCHTIME));
        mHolder.score.setText(Utility.getScores(cursor.getInt(COL_HOME_GOALS), cursor.getInt(COL_AWAY_GOALS)));
        mHolder.matchId = cursor.getDouble(COL_ID);

        String homeCrestUrl = cursor.getString(cursor.getColumnIndex("home_crest_url"));
        loadImageInto(context, homeCrestUrl, mHolder.homeCrest);

        String awayCrestUrl = cursor.getString(cursor.getColumnIndex("away_crest_url"));
        loadImageInto(context, awayCrestUrl, mHolder.awayCrest);

        //Log.v(FetchScoreTask.LOG_TAG,mHolder.homeName.getText() + " Vs. " + mHolder.awayName.getText() +" id " + String.valueOf(mHolder.matchId));
        //Log.v(FetchScoreTask.LOG_TAG,String.valueOf(detail_match_id));
        LayoutInflater vi = (LayoutInflater) context.getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.detail_fragment, null);
        ViewGroup container = (ViewGroup) view.findViewById(R.id.details_fragment_container);
        if(mHolder.matchId == detail_match_id) {
            //Log.v(FetchScoreTask.LOG_TAG,"will insert extraView");

            container.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.MATCH_PARENT));
            TextView match_day = (TextView) v.findViewById(R.id.matchday_textview);
            match_day.setText(Utility.getMatchDay(cursor.getInt(COL_MATCHDAY),
                    cursor.getInt(COL_LEAGUE)));
            TextView league = (TextView) v.findViewById(R.id.league_textview);
            league.setText(Utility.getLeague(cursor.getInt(COL_LEAGUE)));
            Button share_button = (Button) v.findViewById(R.id.share_button);

            share_button.setTag(mHolder);
            share_button.setOnClickListener(this);
        } else {
            container.removeAllViews();
        }

    }

    @Override
    public void onClick(View button) {
        final ViewHolder mHolder = (ViewHolder) button.getTag();
        //add Share Action
        mContext.startActivity(createShareForecastIntent(mHolder.homeName.getText()+" "
                +mHolder.score.getText()+" "+mHolder.awayName.getText() + " "));

        if (null != mOnItemShareListener) {
            mOnItemShareListener.onItemShare(mHolder.homeName.getText().toString());
        }
    }

    public void setOnItemShareListener(OnItemShareListener onItemShareListener) {
        mOnItemShareListener = onItemShareListener;
    }

    public Intent createShareForecastIntent(String ShareText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, ShareText + FOOTBALL_SCORES_HASHTAG);
        return shareIntent;
    }

    private static void loadImageInto(Context context, String crestUrl, ImageView imageView) {
        synchronized (lock) {
            if (null == sRequestBuilder) {
                sRequestBuilder = Glide.with(context)
                        .using(Glide.buildStreamModelLoader(Uri.class, context), InputStream.class)
                        .from(Uri.class)
                        .as(SVG.class)
                        .transcode(new SVGTranscoder(), PictureDrawable.class)
                        .sourceEncoder(new StreamEncoder())
                        .cacheDecoder(new FileToStreamDecoder<>(new SVGDecoder()))
                        .decoder(new SVGDecoder())
                        .error(R.drawable.ic_launcher)
                        .listener(new DrawableRequestListener());
            }
        }

        try {
            sRequestBuilder.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .load(Uri.parse(crestUrl))
                    .placeholder(R.drawable.ic_launcher)
                    .into(imageView);
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}

