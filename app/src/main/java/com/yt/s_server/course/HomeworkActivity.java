package com.yt.s_server.course;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.yt.s_server.Home;
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

public class HomeworkActivity extends AppCompatActivity {
    Handler handler;
    private ListView homeworkList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);
        homeworkList = findViewById(R.id.lv_homework_list);

        Intent intent = getIntent();
        final String id = intent.getStringExtra("id");
        final String cookie = intent.getStringExtra("cookie");



        new Thread() {
            @Override
            public void run() {
                String result = "";
                try {
                    URL courseHomeUrl = new URL("http://eol.bnuz.edu.cn/meol/jpk/course/layout/newpage/index.jsp?courseId=" + id);
                    URLConnection connCourse = courseHomeUrl.openConnection();
                    connCourse.setRequestProperty("Cookie", cookie);
                    connCourse.setDoInput(true);
                    result = inputStreamTOString(connCourse.getInputStream());

                    URL homeworkUrl = new URL("http://eol.bnuz.edu.cn/meol/common/hw/student/hwtask.jsp");
                    URLConnection connHomework = homeworkUrl.openConnection();
                    connHomework.setRequestProperty("Cookie", cookie);
                    connHomework.setDoInput(true);
                    result = inputStreamTOString(connHomework.getInputStream());

                    URL logoutUrl = new URL("http://eol.bnuz.edu.cn/meol/homepage/V8/include/logout.jsp");// 退出网络教学平台
                    URLConnection connection1 = logoutUrl.openConnection();
                    connection1.setRequestProperty("Cookie", cookie);
                    connection1.setDoInput(true);
                    String logoutHtml = inputStreamTOString(connection1.getInputStream());
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
                Elements body = doc.getElementsByClass("valuelist");

                Elements elements = body.select("tr");
                final ArrayList<Homework> homeworkItems = new ArrayList<>();
                for (Element el : elements) {
                    Elements a = el.select("td");
                    ArrayList<String> text = new ArrayList<>();
                    for(Element el1 : a){
                        text.add(el1.text());
                    }
                    try{
                        homeworkItems.add(new Homework(text.get(0), text.get(1), text.get(2), text.get(3)));
                        System.out.println(text);
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }

                final HomeworkAdapter adapter = new HomeworkAdapter(HomeworkActivity.this, R.layout.view_homework, homeworkItems);
                homeworkList.setAdapter(adapter);
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
