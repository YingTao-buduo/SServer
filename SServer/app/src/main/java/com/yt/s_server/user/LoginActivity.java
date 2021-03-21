package com.yt.s_server.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import com.yt.s_server.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LoginActivity extends AppCompatActivity {
    private EditText stuId;
    private EditText stuPassword;
    private EditText inputCode;
    private WebView code;

    Handler handler;
    String path = "http://es.bnuz.edu.cn/";
    String loginUrl = "http://es.bnuz.edu.cn?" +
            "__EVENTTARGET=&" +
            "__EVENTARGUMENT=&" +
            "__VIEWSTATE=" + "%%2FwEPDwUJLTQwNjEzNDEyDxYCHgh1c2VybmFtZWgWAmYPZBYCAhkPFgIeB1Zpc2libGVnZGRjPsIpUJ4aH2luxY44VsHHL9XfHA%%3D%%3D" + "&" +
            "__VIEWSTATEGENERATOR=" + "09394A33&" +
            "__PREVIOUSPAGE=" + "P41Qx-bOUYMcrSUDsalSZQ66PXL-H_8xeQ4t7bJ3gWnYCDI-j8Z8SOoK8eM1" + "&" +
            "__EVENTVALIDATION=" + "%%2FwEWCwLh2%%2FLmDALs0bLrBgLs0fbZDALs0Yq1BQK%%2FwuqQDgKAqenNDQLN7c0VAveMotMNAu6ImbYPArursYYIApXa%%2FeQDLr1E%%2BXsVwFzL04pQEqVeam37aB4%%3D" + "&" +
            "TextBox1=%s&" +
            "TextBox2=%s&" +
            "TextBox3=%s&" +
            "RadioButtonList1=%%E5%%AD%%A6%%E7%%94%%9F&" +
            "Button4_test=";
    private String cookie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        stuId = findViewById(R.id.et_studentId);
        stuPassword = findViewById(R.id.et_studentPassword);
        Button stuLogin = findViewById(R.id.btn_studentLogin);
        code = findViewById(R.id.wv_code);
        inputCode = findViewById(R.id.et_code);

        new Thread() {
            @Override
            public void run() {
                String myCookie = "myCookie";
                try {
                    URL getCodeCookieUrl = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) getCodeCookieUrl.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    if (conn.getResponseCode() == 200) {
                        conn.connect();
                        Map<String, List<String>> map = conn.getHeaderFields();
                        Set<String> set = map.keySet();
                        for (String key : map.keySet()) {
                            if (key != null && key.equals("Set-Cookie")) {
                                List<String> strings = map.get(key);
                                for (String str : strings) {
                                    if (str.startsWith("ASP.NET_SessionId=")) {
                                        myCookie = str.substring(0, str.indexOf(";"));
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.what = 1;
                message.obj = myCookie;
                handler.sendMessage(message);
            }
        }.start();

        handler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                cookie = msg.obj.toString();
                try{
                    String checkCodePath = "http://es.bnuz.edu.cn/CheckCode.aspx";
                    CookieManager.getInstance().setCookie(checkCodePath, cookie);
                    code.loadUrl(checkCodePath);
                    code.setInitialScale(500);
                    code.setWebViewClient(new WebViewClient(){
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            view.loadUrl(url);
                            return true;
                        }
                    });
                } catch (Exception e) {
                    System.err.println("【！！错误】EsCheckCodeActivity中尝试获取验证码出现失败：" + e);
                }
            }
        };

        stuLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("studentInfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("stuId", stuId.getText().toString());
                editor.putString("stuPassword", stuPassword.getText().toString());
                editor.apply();
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try{
                            SharedPreferences sharedPreferences = getSharedPreferences("studentInfo", MODE_PRIVATE);
                            String LN = sharedPreferences.getString("stuId", "");
                            String LP = sharedPreferences.getString("stuPassword", "");
                            String p = String.format(loginUrl,LN,LP,inputCode.getText().toString());
                            URL checkCodeUrl = new URL(p);
                            HttpURLConnection conn = (HttpURLConnection) checkCodeUrl.openConnection();
                            conn.setConnectTimeout(5000);
                            conn.setRequestMethod("GET");
                            conn.setRequestProperty("Cookie", cookie);
                            conn.setDoInput(true);
                            conn.getInputStream();

                            ESConnection esConnection = new ESConnection();
                            esConnection.setCookie(LN, LP, cookie);

                            String scoreHtml = esConnection.getScore();
                            try{
                                ScoreDBHelper dbHelper1 = new ScoreDBHelper(LoginActivity.this, "ESS", null ,1);
                                SQLiteDatabase sqliteDatabase1 = dbHelper1.getWritableDatabase();
                                sqliteDatabase1.execSQL("delete from score");
                                ExamDBHelper dbHelper2 = new ExamDBHelper(LoginActivity.this, "ESC", null ,1);
                                SQLiteDatabase sqliteDatabase2 = dbHelper2.getWritableDatabase();
                                sqliteDatabase2.execSQL("delete from classroom");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            parseScoreHtml(scoreHtml);

                            String classroomHtml = esConnection.getExamInfo();
                            parseClassroomHtml(classroomHtml);

                            String userHtml = esConnection.getUserInfo();
                            parseUserInfo(userHtml);

                            InputStream is = esConnection.getHeadImg();
                            String externalCacheDir = getExternalCacheDir().getPath();
                            File file = new File(externalCacheDir, "head.png");
                            byte[] buffer = new byte[1024];
                            int len;
                            try {
                                OutputStream os = new FileOutputStream(file);
                                while((len = is.read(buffer)) != -1){
                                    os.write(buffer,0,len);
                                }
                                os.close();
                                is.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                        finish();
                    }
                }.start();

            }
        });
    }

    public void parseScoreHtml(String html){
        ScoreDBHelper dbHelper = new ScoreDBHelper(LoginActivity.this, "ESS", null ,1);
        SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
        try{
            Document doc = Jsoup.parseBodyFragment(html);
            Elements body = doc.getElementsByClass("table");
            Elements elements = body.select("tr");
            for(Element e : elements) {
                Elements tds = e.select("td");
                List<String> score_items = new ArrayList<>();
                for(Element td : tds) {
                    score_items.add(td.text());
                }

                try {
                    ContentValues values = new ContentValues();
                    values.put("year", score_items.get(0));
                    values.put("term", score_items.get(1));
                    values.put("id", score_items.get(2));
                    values.put("name", score_items.get(3));
                    values.put("type_1", score_items.get(4));
                    values.put("type", score_items.get(5));
                    values.put("credit", (int)Float.parseFloat(score_items.get(6)));
                    values.put("mark", Integer.parseInt(score_items.get(7)));
                    values.put("gp", Float.parseFloat(score_items.get(8)));
                    values.put("type_2", score_items.get(9));
                    sqliteDatabase.insert("score", null, values);
                } catch(Exception e1) {
                    e1.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseClassroomHtml(String html){
        ExamDBHelper dbHelper = new ExamDBHelper(LoginActivity.this, "ESC", null ,1);
        SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
        try{
            Document doc = Jsoup.parseBodyFragment(html);
            Elements elements = doc.select("tbody");
            Elements trs = elements.select("tr");
            for(Element tr : trs) {
                Elements tds = tr.select("td");
                List<String> classroom_items = new ArrayList<>();
                for(Element td : tds) {
                    classroom_items.add(td.text());
                }
                try {
                    ContentValues values = new ContentValues();
                    values.put("num", classroom_items.get(0));
                    values.put("college", classroom_items.get(1));
                    values.put("id", classroom_items.get(2));
                    values.put("course", classroom_items.get(3));
                    values.put("startWeek", classroom_items.get(4));
                    values.put("endWeek", classroom_items.get(5));
                    values.put("time", classroom_items.get(6));
                    values.put("classroom", classroom_items.get(7));
                    values.put("seat", classroom_items.get(8));
                    values.put("remarks", classroom_items.get(9));
                    values.put("delay", classroom_items.get(10));
                    values.put("teacher", classroom_items.get(11));
                    sqliteDatabase.insert("classroom", null, values);
                } catch(Exception e1) {
                    e1.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseUserInfo(String html){
        Document doc = Jsoup.parseBodyFragment(html);
        Element user_name = doc.getElementById("xm");
        Element user_id = doc.getElementById("xh");
        Element user_college = doc.getElementById("lbl_xy");
        Element user_subject = doc.getElementById("lbl_zymc");
        SharedPreferences sharedPreferences = getSharedPreferences("studentInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userName", user_name.text());
        editor.putString("userId", user_id.text());
        editor.putString("userCollege", user_college.text());
        editor.putString("userSubject", user_subject.text());
        editor.apply();
    }
}
