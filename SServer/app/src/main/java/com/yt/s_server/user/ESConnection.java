package com.yt.s_server.user;

import java.io.IOException;
import java.io.InputStream;

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
		String esUserInfo = "http://es.bnuz.edu.cn/jwgl/xsgrxx.aspx?xh=%s&xm=%s&gnmkdm=N121501";
		return getHtml(String.format(esUserInfo, LN, name));
	}

	public String getExamInfo() {
		String esExamUrl = "http://es.bnuz.edu.cn/jwgl/xskscx.aspx?xh=%s&xm=%s&__EVENTTARGET=&__EVENTARGUMENT=&ccd_xn_ClientState=2020-2021:::2020-2021&ccd_xq_ClientState=2:::&bt_kscx=查询";
		return getHtml(String.format(esExamUrl, LN, name));
	}

	public String getScore() {
		String esScoreUrl = "http://es.bnuz.edu.cn/jwgl/xscjcx.aspx?xh=%s&xm=%s&__EVENTTARGET=&Button6=主修专业最高成绩";
		return getHtml(String.format(esScoreUrl, LN, name));
	}

	public InputStream getHeadImg() {
		String esHeadImgUrl = "http://es.bnuz.edu.cn/readimagexs.aspx?xh=%s";
		String url = String.format(esHeadImgUrl, LN);
		Connection conn = Jsoup.connect(url);
		conn.header("Cookie", cookie);
		conn.header("Referer", "http://es.bnuz.edu.cn/jwgl/xsgrxx.aspx?xh="+LN+"&xm="+name+"&gnmkdm=N121501");
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
		conn.header("Referer", String.format("http://es.bnuz.edu.cn/xs_main.aspx?xh=%s", LN));
		try {
			Response r = conn.ignoreContentType(true).method(Method.GET).execute();
			return r.body();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
