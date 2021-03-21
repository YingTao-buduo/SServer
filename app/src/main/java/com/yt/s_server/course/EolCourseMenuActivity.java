package com.yt.s_server.course;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.yt.s_server.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class EolCourseMenuActivity extends AppCompatActivity {
    Handler handler;
    private ListView courseMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eol_course_menu);

        courseMenu = findViewById(R.id.lv_courseMenu);

        Intent intent = getIntent();
        final String id = intent.getStringExtra("id");
        final String cookie = intent.getStringExtra("cookie");

        new Thread() {
            @Override
            public void run() {
                String result = "";
                try {
                    URL courseHomeUrl = new URL("http://eol.bnuz.edu.cn/meol/jpk/course/layout/newpage/default_demonstrate.jsp?courseId=" + id);
                    URLConnection connCourse = courseHomeUrl.openConnection();
                    connCourse.setRequestProperty("Cookie", cookie);
                    connCourse.setDoInput(true);
                    result = inputStreamTOString(connCourse.getInputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.what = 1;
                message.obj = result;
                handler.sendMessage(message);
            }
        }.start();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = msg.obj.toString();

                Document doc = Jsoup.parseBodyFragment(result);
                Elements body = doc.getElementsByClass("body2");

                Elements elements = body.select("li");
                final ArrayList<String> menuItems = new ArrayList<>();
                for (Element el : elements) {
                    menuItems.add(el.text());
                }

                final CourseMenuAdapter adapter = new CourseMenuAdapter(EolCourseMenuActivity.this, R.layout.view_course_menu, menuItems);
                courseMenu.setAdapter(adapter);
            }
        };
    }

    private static String inputStreamTOString(InputStream in) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int count = -1;
        while ((count = in.read(data, 0, 1024)) != -1)
            outStream.write(data, 0, count);
        data = null;
        return new String(outStream.toByteArray(), "GB2312");
    }
}
