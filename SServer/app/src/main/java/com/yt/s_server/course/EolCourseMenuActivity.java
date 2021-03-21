package com.yt.s_server.course;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
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
                    URL courseHomeUrl = new URL("http://eol.bnuz.edu.cn/meol//homepage/course/course_index.jsp?courseId=" + id);// 获取通知
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
                Elements body = doc.getElementsByClass("nav");

                Elements elements = body.select("li");
                final ArrayList<CourseMenuItem> menuItems = new ArrayList<>();
                for (Element el : elements) {
                    String menuWrap = el.getElementsByTag("a").eq(0).text();
                    String menuWrapUrl = el.getElementsByTag("a").eq(0).attr("href");
                    menuWrapUrl = "http://eol.bnuz.edu.cn/meol/jpk/course/" + menuWrapUrl.substring(6, menuWrapUrl.length());
                    if (menuWrapUrl.substring(menuWrapUrl.length() - 3, menuWrapUrl.length()).matches("[0-9]{1,}")) {
                        menuItems.add(new CourseMenuItem(menuWrap, menuWrapUrl));
                    }
                }

                final CourseMenuAdapter adapter = new CourseMenuAdapter(EolCourseMenuActivity.this, R.layout.course_menu_view, menuItems);
                courseMenu.setAdapter(adapter);
                courseMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                        Intent intent = new Intent(EolCourseMenuActivity.this, EolHomeActivity.class);
                        intent.putExtra("url", menuItems.get(position).getMenuWrapUrl());
                        intent.putExtra("cookie", cookie);
                        startActivity(intent);
                    }
                });
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
