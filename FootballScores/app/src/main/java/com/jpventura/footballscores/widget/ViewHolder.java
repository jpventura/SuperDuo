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

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpventura.footballscores.R;

/**
 * Created by yehya khaled on 2/26/2015.
 */
public class ViewHolder {
    public TextView homeName;
    public TextView awayName;
    public TextView score;
    public TextView date;
    public ImageView homeCrest;
    public ImageView awayCrest;
    public double matchId;

    public ViewHolder(View view) {
        homeName = (TextView) view.findViewById(R.id.home_name);
        awayName = (TextView) view.findViewById(R.id.away_name);
        score = (TextView) view.findViewById(R.id.score_textview);
        date = (TextView) view.findViewById(R.id.data_textview);
        homeCrest = (ImageView) view.findViewById(R.id.home_crest);
        awayCrest = (ImageView) view.findViewById(R.id.away_crest);
    }
}
