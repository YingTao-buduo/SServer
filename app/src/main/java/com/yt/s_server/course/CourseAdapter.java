package com.yt.s_server.course;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.yt.s_server.R;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends ArrayAdapter<Course> {
    private int resourceId;

    private List<Bitmap> bitmaps = new ArrayList<>();

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Bitmap bitmap = (Bitmap) msg.obj;
                bitmaps.add(bitmap);
            }
        }
    };

    public CourseAdapter(Context context, int textViewResourceId, final ArrayList<Course> objects, boolean dm) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        new Thread() {
            private HttpURLConnection conn;
            private Bitmap bitmap;
            public void run() {
                for (Course course : objects) {
                    try {
                        URL url = new URL(course.getCoverUrl());
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setConnectTimeout(5000);
                        int code = conn.getResponseCode();
                        if (code == 200) {
                            InputStream is = conn.getInputStream();
                            bitmap = BitmapFactory.decodeStream(is);
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = bitmap;
                            handler.sendMessage(msg);
                        } else {
                            Message msg = new Message();
                            msg.what = 2;
                            handler.sendMessage(msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Message msg = new Message();
                        msg.what = 2;
                        handler.sendMessage(msg);
                    }
                    conn.disconnect();
                }
            }
        }.start();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final Course course = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        }else {
            view = convertView;
        }
        TextView name = view.findViewById(R.id.tv_courseName);
        TextView teacher = view.findViewById(R.id.tv_courseTeacher);
        TextView number = view.findViewById(R.id.tv_courseNumber);
        TextView notice = view.findViewById(R.id.tv_notice);
        TextView homework = view.findViewById(R.id.tv_homework);
        TextView test = view.findViewById(R.id.tv_test);
        TextView questionnaire = view.findViewById(R.id.tv_questionnaire);
        ImageView cover = view.findViewById(R.id.iv_courseCover);
        cover.setImageResource(R.drawable.default_course_img);
        notice.setAlpha(0);
        homework.setAlpha(0);
        test.setAlpha(0);
        questionnaire.setAlpha(0);
        name.setText(course.getName());
        teacher.setText(course.getTeacher());
        number.setText(course.getNumber());
        if(course.isNotice()) notice.setAlpha(1);
        if(course.isHomework()) homework.setAlpha(1);
        if(course.isTest()) test.setAlpha(1);
        if(course.isQuestionnaire()) questionnaire.setAlpha(1);
        try{
            cover.setImageBitmap(bitmaps.get(position));
        } catch (Exception e){
            //e.printStackTrace();
        }
        return view;
    }
}
