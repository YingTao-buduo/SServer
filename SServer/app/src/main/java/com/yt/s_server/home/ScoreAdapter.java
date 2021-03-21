package com.yt.s_server.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yt.s_server.R;
import com.yt.s_server.user.Score;

import java.util.ArrayList;

public class ScoreAdapter extends ArrayAdapter<Score> {
    private int resourceId;

    public ScoreAdapter(Context context, int textViewResourceId, final ArrayList<Score> objects, boolean dm) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Score score = getItem(position);

        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        } else {
            view = convertView;
        }

        TextView year = view.findViewById(R.id.tv_score_year);
        TextView term = view.findViewById(R.id.tv_score_term);
        TextView name = view.findViewById(R.id.tv_score_name);
        TextView type = view.findViewById(R.id.tv_score_type);
        TextView credit = view.findViewById(R.id.tv_score_credit);
        TextView mark = view.findViewById(R.id.tv_score_mark);
        TextView gp = view.findViewById(R.id.tv_score_gp);

        year.setText(String.format("%s学年", score.getYear()));
        term.setText(String.format("第%s学期", score.getTerm()));
        name.setText(String.format("%s", score.getName()));
        type.setText(String.format("%s", score.getType()));
        credit.setText(String.format("学分：%s", score.getCredit()));
        mark.setText(String.format("成绩：%s", score.getMark()));
        gp.setText(String.format("绩点：%s", score.getGp()));

        return view;
    }
}
