package com.yt.s_server;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.yt.s_server.course.Course;
import com.yt.s_server.course.CourseAdapter;
import com.yt.s_server.course.EolCourseMenuActivity;
import com.yt.s_server.course.HomeworkActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

public class MyCourse extends Fragment {
    private Handler handler;
    boolean flag = true;
    private ListView courseList;

    private ArrayList<Course> courses = new ArrayList<>();
    private String cookie = "JSESSIONID=";

    public static MyCourse newInstance(String param1, String param2) {
        MyCourse fragment = new MyCourse();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_my_course, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(flag){
            courseList = getActivity().findViewById(R.id.lv_courseList);
            load();
            flag = false;
        }

    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        load();
//    }

    private void load(){
        new Thread() {
            @Override
            public void run() {
                String result = "";
                int splitIndex = 0;
                try {
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("studentInfo", MODE_PRIVATE);
                    String LN = sharedPreferences.getString("stuId", "");
                    String LP = sharedPreferences.getString("stuPassword", "");
                    URL homeUrl = new URL("http://eol.bnuz.edu.cn/meol/loginCheck.do" + "?IPT_LOGINUSERNAME=" + LN
                            + "&IPT_LOGINPASSWORD=" + LP);
                    HttpURLConnection conn = (HttpURLConnection) homeUrl.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    if (conn.getResponseCode() == 200) {
                        String homeHtml = inputStreamTOString(conn.getInputStream());
                        String subStr = homeHtml.substring(homeHtml.indexOf("jsessionid="));
                        cookie += subStr.substring(11, subStr.indexOf("V8/"));
                        URL courseUrl = new URL("http://eol.bnuz.edu.cn/meol/welcomepage/student/course_list_v8.jsp");
                        URLConnection connection = courseUrl.openConnection();
                        connection.setRequestProperty("Cookie", cookie);
                        connection.setDoInput(true);
                        String courseHtml = inputStreamTOString(connection.getInputStream());
                        result = courseHtml;
                        splitIndex = courseHtml.length();

                        URL noticeUrl = new URL("http://eol.bnuz.edu.cn/meol/welcomepage/student/interaction_reminder_v8.jsp");
                        connection = noticeUrl.openConnection();
                        connection.setRequestProperty("Cookie", cookie);
                        connection.setDoInput(true);
                        String noticeHtml = inputStreamTOString(connection.getInputStream());
                        result += noticeHtml;


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.what = 1;
                message.obj = result;
                message.arg1 = splitIndex;
                handler.sendMessage(message);
            }
        }.start();

        //接收子线程访问EOL返回的数据
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = msg.obj.toString();
                //System.out.println(result);
                int splitNum = msg.arg1;
                // 解析网页(Jsoup返回Document就是浏览器Document对象)
                Document doc = Jsoup.parseBodyFragment(result.substring(0, splitNum));
                Elements bodys = doc.getElementsByClass("list");

                Elements elements = bodys.select("li");
                // 获取元素中的内容
                // 封装各对象 来存储爬的数据
                for (Element el : elements) {
                    String title = el.getElementsByClass("title").eq(0).text();
                    String teacher = el.getElementsByClass("realname").eq(0).text();
                    String number = el.getElementsByClass("coursenum").eq(0).text();
                    String coverUrl = el.getElementsByTag("img").eq(0).attr("src");
                    String id = el.getElementsByClass("link").select("a").eq(0).attr("onclick");

                    coverUrl = "http://eol.bnuz.edu.cn" + coverUrl;
                    String[] urlArry = id.split("'");
                    id = urlArry[1].substring(urlArry[1].indexOf("=") + 1, urlArry[1].length());
                    Course course = new Course(title, teacher, number, coverUrl, id);
                    courses.add(course);
                }
//******************************************************************************************************************************************
                //解析互动提醒
                try{
                    String mList = "";
                    doc = Jsoup.parseBodyFragment(result.substring(splitNum + 1));
                    Element body = doc.getElementById("reminder");
                    String pattern = ">.*?<";
                    try {
                        Pattern r = Pattern.compile(pattern);
                        Matcher m = r.matcher(body.toString());
                        //对信息进行格式化
                        String noticeType = "";//【通知：notice】【作业：homework】【测试：test】【问卷：questionnaire】
                        while (m.find()) {
                            if (m.group().equals("><") || m.group().equals("> <"))
                                continue;
                            if (m.group().substring(1, m.group().indexOf("<")).matches("[0-9]*")) {
                                String temp = m.group().substring(1, m.group().indexOf("<"));
                                m.find();
                                temp += m.group().substring(1, m.group().indexOf("<"));
                                noticeType = temp.substring(temp.length() - 2,temp.length());
                            } else {
                                for(Course course : courses){
                                    if(m.group().substring(2, m.group().indexOf("<")).contains(course.getName())){
                                        switch (noticeType){
                                            case "通知":
                                                course.setNotice(true);
                                                break;
                                            case "作业":
                                                course.setHomework(true);
                                                break;
                                            case "测试":
                                                course.setTest(true);
                                                break;
                                            case "问卷":
                                                course.setQuestionnaire(true);
                                                break;
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

                final CourseAdapter adapter = new CourseAdapter(getContext(), R.layout.view_course, courses, false);
                courseList.setAdapter(adapter);
                courseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                        Intent intent = new Intent(getContext(), HomeworkActivity.class);
                        intent.putExtra("id", courses.get(position).getId());
                        System.out.println(courses.get(position).getId());
                        intent.putExtra("cookie", cookie);
                        startActivity(intent);
                    }
                });
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        new Thread(){
            @Override
            public void run() {
                super.run();
                try{
                    URL logoutUrl = new URL("http://eol.bnuz.edu.cn/meol/homepage/V8/include/logout.jsp");// 退出网络教学平台
                    URLConnection connection1 = logoutUrl.openConnection();
                    connection1.setRequestProperty("Cookie", cookie);
                    connection1.setDoInput(true);
                    String logoutHtml = inputStreamTOString(connection1.getInputStream());
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        }.start();
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
