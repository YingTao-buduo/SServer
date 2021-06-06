package com.yt.s_server;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yt.s_server.user.ESConnection;
import com.yt.s_server.user.ExamDBHelper;
import com.yt.s_server.user.LoginActivity;
import com.yt.s_server.user.ScoreDBHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class AboutUser extends Fragment {
    Handler handler;

    private Button btnUpdate;
    private Button btnUserLogin;

    private ImageView head;
    private TextView userName;
    private TextView userId;
    private TextView userCollege;
    private TextView userSubject;

    private String path = "https://es.bnuz.edu.cn/";
    private String loginUrl = "https://es.bnuz.edu.cn/default2.aspx?" +
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

    Bitmap bitmap_0;
    Bitmap bitmap_1;
    boolean head_flag = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about_user, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        btnUpdate = getActivity().findViewById(R.id.btn_update);
        btnUserLogin = getActivity().findViewById(R.id.btn_user_login);

        head = getActivity().findViewById(R.id.iv_head);
        userName = getActivity().findViewById(R.id.tv_user_name);
        userId = getActivity().findViewById(R.id.tv_user_id);
        userCollege = getActivity().findViewById(R.id.tv_user_college);
        userSubject = getActivity().findViewById(R.id.tv_user_subject);

        try{
            String localIconNormal = "/storage/emulated/0/Android/data/com.yt.s_server/cache/head.png";
            FileInputStream localStream = new FileInputStream(localIconNormal);
            bitmap_0 = BitmapFactory.decodeStream(localStream);
            head.setImageBitmap(bitmap_0);
        } catch (Exception e){
            e.printStackTrace();
        }

        new Thread(){
            @Override
            public void run() {
                super.run();
                try{
                    String localIconNormal = "/storage/emulated/0/Android/data/com.yt.s_server/cache/head.png";
                    FileInputStream localStream = new FileInputStream(localIconNormal);
                    bitmap_1 = rsBlur(getContext(),BitmapFactory.decodeStream(localStream),25, 7);
                    head.setImageBitmap(bitmap_1);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();

        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(head_flag){
                    head.setImageBitmap(bitmap_0);
                    head_flag = false;
                } else {
                    head.setImageBitmap(bitmap_1);
                    head_flag = true;
                }
            }
        });

        try{
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("studentInfo", MODE_PRIVATE);
            userName.setText(sharedPreferences.getString("userName", ""));
            userId.setText(sharedPreferences.getString("userId", ""));
            userCollege.setText(sharedPreferences.getString("userCollege", ""));
            userSubject.setText(sharedPreferences.getString("userSubject", ""));
        } catch (Exception e){
            e.printStackTrace();
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                View view = inflater.inflate(R.layout.alert_check_code, null);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                Button close = view.findViewById(R.id.btn_check_update);
                final WebView checkCode = view.findViewById(R.id.wv_check_code);
                final EditText inputCode = view.findViewById(R.id.et_check_code);

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
                        System.out.println("get code cookie:"+cookie);
                        try{
                            String checkCodePath = "https://es.bnuz.edu.cn/checkcode.aspx";
                            CookieManager.getInstance().setCookie(checkCodePath, cookie);
                            checkCode.loadUrl(checkCodePath);
                            checkCode.setInitialScale(500);
                            checkCode.setWebViewClient(new WebViewClient(){
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

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();

                                try{
                                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("studentInfo", MODE_PRIVATE);
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
                                    System.out.println(p);
//                                    System.out.println("!!!!!!!!!!!!!!!\n"+inputStreamTOString(conn.getInputStream()));

                                    ESConnection esConnection = new ESConnection();
                                    esConnection.setCookie(LN, LP, cookie);

                                    String scoreHtml = esConnection.getScore();
                                    try{
                                        ScoreDBHelper dbHelper1 = new ScoreDBHelper(getContext(), "ESS", null ,1);
                                        SQLiteDatabase sqliteDatabase1 = dbHelper1.getWritableDatabase();
                                        sqliteDatabase1.execSQL("delete from score");
                                        ExamDBHelper dbHelper2 = new ExamDBHelper(getContext(), "ESC", null ,1);
                                        SQLiteDatabase sqliteDatabase2 = dbHelper2.getWritableDatabase();
                                        sqliteDatabase2.execSQL("delete from classroom");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    parseScoreHtml(scoreHtml);

                                    String classroomHtml = esConnection.getExamInfo();
                                    System.out.println(classroomHtml);
                                    parseClassroomHtml(classroomHtml);

                                    String userHtml = esConnection.getUserInfo();
                                    parseUserInfo(userHtml);

                                    InputStream is = esConnection.getHeadImg();
                                    String externalCacheDir = getContext().getExternalCacheDir().getPath();
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
                            }
                        }.start();
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });

        btnUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        Button daily = getActivity().findViewById(R.id.btn_daily_check);
        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String daily_url = "http://xgfx.bnuz.edu.cn/xsdtfw/sys/swmxsyqxxsjapp/*default/index.do?wxType=1#/zxtw";
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(daily_url));
                startActivity(intent);
            }
        });

        Button inOut = getActivity().findViewById(R.id.btn_in_and_out);
        inOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String daily_url = "http://xgfx.bnuz.edu.cn/xsdtfw/sys/emapfunauth/pages/welcome.do?service=%2Fxsdtfw%2Fsys%2Fswmwcsqglapp%2F*default%2Findex.do#/";
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(daily_url));
                startActivity(intent);
            }
        });

        Button es = getActivity().findViewById(R.id.btn_es);
        es.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://es.bnuz.edu.cn/";
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        Button eol = getActivity().findViewById(R.id.btn_eol);
        es.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://eol.bnuz.edu.cn/meol/index.do";
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        Button tm = getActivity().findViewById(R.id.btn_tm);
        es.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://tm.bnuz.edu.cn/ui/login";
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    public void parseScoreHtml(String html){
        ScoreDBHelper dbHelper = new ScoreDBHelper(getContext(), "ESS", null ,1);
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
        ExamDBHelper dbHelper = new ExamDBHelper(getContext(), "ESC", null ,1);
        SQLiteDatabase sqliteDatabase = dbHelper.getWritableDatabase();
        try{
            Document doc = Jsoup.parseBodyFragment(html);
            Elements elements = doc.select("tbody");
            System.out.println(elements);
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
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("studentInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userName", user_name.text());
        editor.putString("userId", user_id.text());
        editor.putString("userCollege", user_college.text());
        editor.putString("userSubject", user_subject.text());
        editor.apply();
    }

    private static Bitmap rsBlur(Context context, Bitmap source, int radius, int times){
        Bitmap inputBmp = source;
        RenderScript renderScript =  RenderScript.create(context);
        for(int i = 0; i < times; i++){
            final Allocation input = Allocation.createFromBitmap(renderScript,inputBmp);
            final Allocation output = Allocation.createTyped(renderScript,input.getType());
            ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, android.renderscript.Element.U8_4(renderScript));
            scriptIntrinsicBlur.setInput(input);
            scriptIntrinsicBlur.setRadius(radius);
            scriptIntrinsicBlur.forEach(output);
            output.copyTo(inputBmp);
            renderScript.destroy();
        }
        return inputBmp;
    }

    private static String inputStreamTOString(InputStream in) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int count = -1;
        while ((count = in.read(data, 0, 1024)) != -1)
            outStream.write(data, 0, count);
        data = null;
        return new String(outStream.toByteArray(), "UTF-8");
    }
}