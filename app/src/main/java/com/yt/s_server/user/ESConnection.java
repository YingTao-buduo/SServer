package com.yt.s_server.user;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;

public class ESConnection {
	private String LN;
	private String name;
	private String cookie;


	public void setCookie(String LN, String name, String cookie) {
		this.LN = LN;
		this.name = name;
		this.cookie = cookie;
	}

	public String getUserInfo() {
		String esUserInfo = "https://es.bnuz.edu.cn/jwgl/xsgrxx.aspx?xh=%s&xm=%s&gnmkdm=N121501";
		return getHtml(String.format(esUserInfo, LN, name));
	}

	public String getExamInfo() {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		int y;
		int t;
		System.out.println(year+"__"+month);
		if(month < 8 && month >= 2){
			y = year;
			t = 2;
		}else{
			y = year + 1;
			t = 1;
		}

		String xn = String.valueOf(y - 1) + "-" + String.valueOf(y);
		String esExamUrl = "https://es.bnuz.edu.cn/jwgl/xskscx.aspx?xh=%s&xm=%s&__EVENTTARGET=&__EVENTARGUMENT=&ccd_xn_ClientState=" + xn + ":::" + xn + "&ccd_xq_ClientState=" + t + ":::&bt_kscx=查询";
		System.out.println(esExamUrl);
		return getHtml(String.format(esExamUrl, LN, name));
	}

	public String getScore() {
		String esScoreUrl = "https://es.bnuz.edu.cn/jwgl/xscjcx.aspx?xh=%s&xm=%s&__EVENTTARGET=&Button6=主修专业最高成绩";
		String h = getHtml(String.format(esScoreUrl, LN, name));
		System.out.println("==============================");
		System.out.println(h);
		System.out.println("==============================");
		return h;
	}

	public InputStream getHeadImg() {
		String esHeadImgUrl = "https://es.bnuz.edu.cn/readimagexs.aspx?xh=%s";
		String url = String.format(esHeadImgUrl, LN);
		Connection conn = Jsoup.connect(url);
		conn.header("Cookie", cookie);
		conn.header("Referer", "https://es.bnuz.edu.cn/jwgl/xsgrxx.aspx?xh="+LN+"&xm="+name+"&gnmkdm=N121501");
		try {
			Response r = conn.ignoreContentType(true).method(Method.GET).execute();
			return r.bodyStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getHtml(String url){
		Connection conn = Jsoup.connect(url);
		conn.header("Cookie", cookie);
		conn.header("Referer", String.format("https://es.bnuz.edu.cn/xs_main.aspx?xh=%s", LN));
		try {
			Response r = conn.ignoreContentType(true).method(Method.GET).execute();
			return r.body();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
