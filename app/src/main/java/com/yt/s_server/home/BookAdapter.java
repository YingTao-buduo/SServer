package com.yt.s_server.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yt.s_server.R;

import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<Book> {
    private int resourceId;
    private int total;

    public BookAdapter(Context context, int textViewResourceId, final ArrayList<Book> objects, boolean dm){
        super(context, textViewResourceId, objects);
        resourceId =textViewResourceId;
        total = objects.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Book book = getItem(position);

        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        } else {
            view = convertView;
        }

        TextView num = view.findViewById(R.id.tv_history_num);
        TextView date = view.findViewById(R.id.tv_history_date);
        TextView title = view.findViewById(R.id.tv_history_title);
        TextView author = view.findViewById(R.id.tv_history_author);
        TextView fine = view.findViewById(R.id.tv_history_fine);

        num.setText(String.valueOf(total - position));
        date.setText(book.getDate());
        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        fine.setText(book.getFine());

        return  view;
    }
}
