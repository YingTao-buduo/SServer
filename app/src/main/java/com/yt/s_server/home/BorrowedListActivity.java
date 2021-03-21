package com.yt.s_server.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.yt.s_server.R;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class BorrowedListActivity extends AppCompatActivity {

    Handler handler;
    ListView borrowedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowed_list);

        borrowedList = findViewById(R.id.lv_borrowed);

        new Thread(){
            @Override
            public void run() {
                super.run();
                try{
                    String url = "http://lib.bnuz.edu.cn:8080/F/?func=login_session&login_source=bor-info&bor_library=BZH50&bor_id=%s&bor_verification=%s";
                    SharedPreferences sharedPreferences = getSharedPreferences("studentInfo", MODE_PRIVATE);
                    String ln = sharedPreferences.getString("stuId", "");
                    String u = String.format(url, ln, ln);
                    Connection conn = Jsoup.connect(u);
                    Connection.Response response = conn.ignoreContentType(true).method(Connection.Method.GET).execute();
                    String result = response.body();
                    //System.out.println(result);

                    Document doc = Jsoup.parseBodyFragment(result);
                    String logout = doc.getElementsByAttributeValue("title", "退出系统").eq(0).attr("href");
                    Elements table = doc.getElementsByClass("indent1");
                    Elements tableRow = table.get(0).getElementsByTag("tr");

                    final List<String> replacePages = new ArrayList<>();

                    for(Element el: tableRow){
                        try{
                            String replacePage = el.getElementsByTag("a").eq(0).attr("href");
                            replacePage = replacePage.substring(replacePage.indexOf("'") + 1, replacePage.lastIndexOf("'"));
                            replacePages.add(replacePage);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    Connection conn_1 = Jsoup.connect(replacePages.get(0));
                    Connection.Response response_1 = conn_1.ignoreContentType(true).method(Connection.Method.GET).execute();
                    String result_1 = response_1.body();


                    Message message = new Message();
                    message.what = 1;
                    message.obj = result_1;
                    handler.sendMessage(message);


                    Connection conn_2 = Jsoup.connect(logout);
                    Connection.Response response_2 = conn_2.ignoreContentType(true).method(Connection.Method.GET).execute();
                    String result_2 = response.body();

                } catch(Exception e){
                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    System.out.println(e);
                }
            }
        }.start();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = msg.obj.toString();

                ArrayList<Book> books = new ArrayList<>();
                Document history = Jsoup.parseBodyFragment(result);
                Elements trs = history.getElementsByTag("tr");
                for(Element el: trs){
                    String[] bookInfo = el.text().split(" ");
                    Elements tds = el.getElementsByTag("td");
                    try{
                        if(Integer.parseInt(bookInfo[0]) > 0){
                            books.add(new Book(tds.get(3).text(), tds.get(2).text(), tds.get(5).text(), tds.get(7).text()));
                        }
                    } catch (Exception e){

                    }
                }

                final ArrayAdapter adapter = new BookAdapter(BorrowedListActivity.this, R.layout.view_book, books, false);
                borrowedList.setAdapter(adapter);
            }
        };

    }
}