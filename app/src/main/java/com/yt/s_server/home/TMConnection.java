package com.yt.s_server.home;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TMConnection {
    private static final TMConnection ourInstance = new TMConnection();
    public static TMConnection getInstance() {
        return ourInstance;
    }

    private String LN;
    private String LP;

    private String token;
    private String session;

    private TMConnection() {
    }

    public void Connect(String name, String password){
        this.LN = name;
        this.LP = password;
        try{
            String cookie = getCookie();
            token = cookie.substring(cookie.indexOf("XSRF"), cookie.indexOf(","));
            session = cookie.substring(cookie.indexOf("JSESSIONID"), cookie.indexOf("}"));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getUserInfo() throws Exception {
        Response response = prepareConnection("http://tm.bnuz.edu.cn/api/user").ignoreContentType(true).method(Method.GET).execute();
        return response.body();
    }

    public String getSchedule() throws Exception {
        String url = String.format("http://tm.bnuz.edu.cn/api/core/students/%s/schedules", LN);
        Response response = prepareConnection(url).ignoreContentType(true).method(Method.GET).execute();
        return response.body();
    }
    public String getLeave() throws Exception {
        String url = String.format("http://tm.bnuz.edu.cn/api/here/students/%s/leaves", LN);
        Response response = prepareConnection(url).ignoreContentType(true).method(Method.GET).execute();
        return response.body();
    }
    public String getAttendance() throws Exception {
        String url = String.format("http://tm.bnuz.edu.cn/api/here/students/%s/attendances", LN);
        Response response = prepareConnection(url).ignoreContentType(true).method(Method.GET).execute();
        return response.body();
    }

    public String getWorkOpen() throws Exception {
        String url = String.format("http://tm.bnuz.edu.cn/api/core/users/%s/works?offset=0&max=1000&is=open", LN);
        Response response = prepareConnection(url).ignoreContentType(true).method(Method.GET).execute();
        return response.body();
    }

    public String getWorkClosed() throws Exception {
        String url = String.format("http://tm.bnuz.edu.cn/api/core/users/%s/works?offset=0&max=1000&is=closed", LN);
        Response response = prepareConnection(url).ignoreContentType(true).method(Method.GET).execute();
        return response.body();
    }

    public String getApplyCourse() throws Exception {
        String url = String.format("http://tm.bnuz.edu.cn/api/here/students/%s/leaves/create", LN);
        Connection.Response response = prepareConnection(url).ignoreContentType(true).method(Connection.Method.GET).execute();
        return response.body();
    }

    public String postApply(String leaveType, String reason, String leaveWeek, ArrayList<String> courseId) throws Exception {
        String c_json = "{\"week\":%s,\"taskScheduleId\":\"%s\"}";
        String ids = "";
        for(int i = 0; i < courseId.size(); i++){
            if(i > 0)
                ids += ",";
            ids += String.format(c_json, leaveWeek, courseId.get(i));
        }
        String url = String.format("http://tm.bnuz.edu.cn/api/here/students/%s/leaves", LN);
        String s_json = "{\"type\":%s,\"reason\":\"%s\",\"addedItems\":[%s]}";
        s_json = String.format(s_json, leaveType, reason, ids);
        System.out.println(s_json);
        Connection conn = prepareApplyConnection(url, s_json.length());
        conn.requestBody(s_json);
        Connection.Response response = conn.ignoreContentType(true).method(Connection.Method.POST).execute();
        return response.body();
    }

    public String getApprovers(String lId) throws Exception {
        String url = String.format("http://tm.bnuz.edu.cn/api/here/students/%s/leaves/%s/approvers", LN, lId);
        Connection conn = Jsoup.connect(url);
        conn.header("(Request-Line)", "GET" + url.substring(url.indexOf("cn/") + 4) + "HTTP/1.1");
        conn.header("Accept", "application/json");
        conn.header("Accept-Encoding", "gzip, deflate");
        conn.header("Accept-Language", "en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7");
        conn.header("Connection", "Keep-Alive");
        conn.header("Cookie", session + "; " + token);
        conn.header("Host", "tm.bnuz.edu.cn");
        conn.header("Referer", String.format("http://tm.bnuz.edu.cn/here/students/%s/leaves/%s", LN, lId));
        conn.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36");
        conn.header("X-Requested-With", "XMLHttpRequest");
        Connection.Response response = conn.ignoreContentType(true).method(Connection.Method.GET).execute();
        return response.body();
    }

    public String toSubmit(String lId, String tId) throws Exception {
        String r_json = "{\"title\":\"请假#%s\",\"to\":\"%s\"}";
        r_json = String.format(r_json, lId, tId);
        String url = String.format("http://tm.bnuz.edu.cn/api/here/students/%s/leaves/%s?op=SUBMIT", LN, lId);
        Connection conn = Jsoup.connect(url);
        conn.header("Accept", "application/json");
        conn.header("Accept-Encoding", "gzip, deflate");
        conn.header("Accept-Language", "en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7");
        conn.header("Connection", "Keep-Alive");
        conn.header("Content-Type", "application/json");
        conn.header("Content-Length", String.valueOf(r_json));
        conn.header("Cookie", session + "; " + token);
        conn.header("X-XSRF-TOKEN", token.substring(token.indexOf("XSRF-TOKEN=") + 11));
        conn.header("Host", "tm.bnuz.edu.cn");
        conn.header("Origin", "http://tm.bnuz.edu.cn");
        conn.header("Referer", String.format("http://tm.bnuz.edu.cn/here/students/%s/leaves/%s", LN, lId));
        conn.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36");
        conn.header("X-Requested-With", "XMLHttpRequest");
        conn.requestBody(r_json);
        Connection.Response response = conn.ignoreContentType(true).method(Method.PATCH).execute();
        return response.body();
    }

    public void logout() throws Exception {
        String url = "http://tm.bnuz.edu.cn/logout";
        Response response = prepareConnection(url).ignoreContentType(true).method(Method.GET).execute();
    }

    private Connection prepareConnection(String url) {
        Connection conn = Jsoup.connect(url);
        conn.header("(Request-Line)", "GET" + url.substring(url.indexOf("cn/") + 3) + "HTTP/1.1");
        conn.header("Accept", "application/json");
        conn.header("Accept-Encoding", "gzip, deflate");
        conn.header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        conn.header("Connection", "Keep-Alive");
        conn.header("Cookie", session + "; " + token);
        conn.header("Host", "tm.bnuz.edu.cn");
        conn.header("Referer", "http://tm.bnuz.edu.cn/ui/login");
        conn.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36");
        conn.header("X-Requested-With", "XMLHttpRequest");
        return conn;
    }

    private Connection prepareApplyConnection(String url, int length) {
        System.out.println(length);
        Connection conn = Jsoup.connect(url);
        conn.header("(Request-Line)", "POST" + url.substring(url.indexOf("cn/") + 3) + "HTTP/1.1");
        conn.header("Accept", "application/json");
        conn.header("Accept-Encoding", "gzip, deflate");
        conn.header("Accept-Language", "en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7");
        conn.header("Connection", "Keep-Alive");
        conn.header("Content-Length", String.valueOf(length));
        conn.header("X-XSRF-TOKEN", token.substring(token.indexOf("XSRF-TOKEN=") + 11));
        conn.header("Content-Type", "application/json");
        conn.header("Cookie", session + "; " + token);
        conn.header("Host", "tm.bnuz.edu.cn");
        conn.header("Origin", "http://tm.bnuz.edu.cn");
        conn.header("Referer", url+"/create");
        conn.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36");
        conn.header("X-Requested-With", "XMLHttpRequest");
        return conn;
    }

    private String getCookie() throws Exception {
        // login 1
        Connection loginCookieConn = Jsoup.connect("http://tm.bnuz.edu.cn/uaa/login");
        loginCookieConn.header("(Request-Line)", "POST /uaa/login HTTP/1.1");
        loginCookieConn.header("Accept", "application/json");
        loginCookieConn.header("Accept-Encoding", "gzip, deflate");
        loginCookieConn.header("Accept-Language", "zh-cn");
        loginCookieConn.header("Connection", "Keep-Alive");
        loginCookieConn.header("Host", "tm.bnuz.edu.cn");
        loginCookieConn.header("Referer", "http://tm.bnuz.edu.cn/ui/login");
        loginCookieConn.header("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36");
        loginCookieConn.header("X-Requested-With", "XMLHttpRequest");
        Response loginResponse = loginCookieConn.ignoreContentType(true).method(Method.GET).execute();

        Map<String, String> loginCookies = loginResponse.cookies();
        String loginCookie = loginCookies.toString().substring(1, loginCookies.toString().indexOf("}"));

        //装学号和密码
        Map<String, String> userInfo = new HashMap<String, String>();
        userInfo.put("username", LN);
        userInfo.put("password", LP);

        // LOGIN
        Connection cookieConn = Jsoup.connect("http://tm.bnuz.edu.cn/uaa/login");
        cookieConn.header("(Request-Line)", "POST /uaa/login HTTP/1.1");
        cookieConn.header("Accept", "application/json");
        cookieConn.header("Accept-Encoding", "gzip, deflate");
        cookieConn.header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        cookieConn.header("Connection", "Keep-Alive");
        cookieConn.header("Content-Length", String.valueOf(19 + LN.length() + LP.length()));
        cookieConn.header("Cookie", loginCookie);
        cookieConn.header("Content-Type", "application/x-www-form-urlencoded");
        cookieConn.header("Host", "tm.bnuz.edu.cn");
        cookieConn.header("Origin", "http://tm.bnuz.edu.cn");
        cookieConn.header("Referer", "http://tm.bnuz.edu.cn/ui/login");
        cookieConn.header("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36");
        cookieConn.header("X-Requested-With", "XMLHttpRequest");
        cookieConn.header("X-UAA-XSRF-TOKEN", loginCookie.substring(loginCookie.indexOf("=") + 1));

        Response cookieResponse = cookieConn.ignoreContentType(true).method(Method.POST).data(userInfo)
                .followRedirects(false).execute();

        Map<String, String> cookies = cookieResponse.cookies();
        String cookie = cookies.toString().substring(1, cookies.toString().indexOf("}"));
        String jsessionid = cookie.substring(cookie.indexOf("UAA-XSRF-TOKEN"),
                cookie.indexOf(",", cookie.indexOf("UAA-XSRF-TO KEN")));
        String uaa_xsrf_token = cookie.substring(cookie.indexOf("JSESSIONID"));

        // uaa
        Connection tryUaaConn = Jsoup.connect("http://tm.bnuz.edu.cn/uaa/");
        tryUaaConn.header("(Request-Line)", "GET /uaa/ HTTP/1.1");
        tryUaaConn.header("Accept", "application/json");
        tryUaaConn.header("Accept-Encoding", "gzip, deflate");
        tryUaaConn.header("Accept-Language", "zh-cn");
        tryUaaConn.header("Connection", "Keep-Alive");
        tryUaaConn.header("Cookie", uaa_xsrf_token + "; " + jsessionid);
        tryUaaConn.header("Host", "tm.bnuz.edu.cn");
        tryUaaConn.header("Referer", "http://tm.bnuz.edu.cn/ui/login");
        tryUaaConn.header("User-Agent",
                "User-Agent\", \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36");
        tryUaaConn.header("X-Requested-With", "XMLHttpRequest");
        tryUaaConn.header("X-UAA-XSRF-TOKEN", loginCookie.substring(loginCookie.indexOf("=") + 1));
        Response tryUaaResponse = tryUaaConn.ignoreContentType(true).method(Method.GET).followRedirects(false)
                .execute();

        // login 2
        Connection tryLoginConn = Jsoup.connect("http://tm.bnuz.edu.cn/login");
        tryLoginConn.header("(Request-Line)", "GET /login HTTP/1.1");
        tryLoginConn.header("Accept", "application/json");
        tryLoginConn.header("Accept-Encoding", "gzip, deflate");
        tryLoginConn.header("Accept-Language", "zh-cn");
        tryLoginConn.header("Connection", "Keep-Alive");
        tryLoginConn.header("Host", "tm.bnuz.edu.cn");
        tryLoginConn.header("Referer", "http://tm.bnuz.edu.cn/ui/login");
        tryLoginConn.header("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36");
        tryLoginConn.header("X-Requested-With", "XMLHttpRequest");
        tryLoginConn.header("X-UAA-XSRF-TOKEN", loginCookie.substring(loginCookie.indexOf("=") + 1));
        Response tryLoginResponse = tryLoginConn.ignoreContentType(true).method(Method.GET).followRedirects(false)
                .execute();
        String tryLoginLocation = tryLoginResponse.header("Location");
        String cookie_1 = tryLoginResponse.cookies().toString();
        String xsrf_token_1 = cookie_1.substring(cookie_1.indexOf("XSRF-TOKEN"),
                cookie.indexOf(",", cookie_1.indexOf("XSRF-TOKEN")));
        String jsessionid_1 = cookie_1.substring(cookie_1.indexOf("JSESSIONID"), cookie_1.indexOf("}"));

        // authorize
        Connection authorizeConn = Jsoup.connect(tryLoginLocation);
        authorizeConn.header("(Request-Line)",
                "GET " + tryLoginLocation.substring(tryLoginLocation.indexOf("cn/") + 3) + " HTTP/1.1");
        authorizeConn.header("Accept", "application/json");
        authorizeConn.header("Accept-Encoding", "gzip, deflate");
        authorizeConn.header("Accept-Language", "zh-cn");
        authorizeConn.header("Connection", "Keep-Alive");
        authorizeConn.header("Host", "tm.bnuz.edu.cn");
        authorizeConn.header("Referer", "http://tm.bnuz.edu.cn/ui/login");
        authorizeConn.header("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36");
        authorizeConn.header("X-Requested-With", "XMLHttpRequest");
        authorizeConn.header("X-UAA-XSRF-TOKEN", loginCookie.substring(loginCookie.indexOf("=") + 1));
        authorizeConn.header("Cookie", uaa_xsrf_token + "; " + jsessionid + "; " + xsrf_token_1 + "; " + jsessionid_1);
        Response authorizeResponse = authorizeConn.ignoreContentType(true).method(Method.GET).followRedirects(false)
                .execute();
        String authorizeLocation = authorizeResponse.header("Location");

        // login?code
        Connection codeConn = Jsoup.connect(authorizeLocation);
        codeConn.header("(Request-Line)",
                "GET " + authorizeLocation.substring(authorizeLocation.indexOf("cn/") + 3) + " HTTP/1.1");
        codeConn.header("Accept", "application/json");
        codeConn.header("Accept-Encoding", "gzip, deflate");
        codeConn.header("Accept-Language", "zh-cn");
        codeConn.header("Connection", "Keep-Alive");
        codeConn.header("Host", "tm.bnuz.edu.cn");
        codeConn.header("Referer", "http://tm.bnuz.edu.cn/ui/login");
        codeConn.header("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36");
        codeConn.header("X-Requested-With", "XMLHttpRequest");
        codeConn.header("X-UAA-XSRF-TOKEN", loginCookie.substring(loginCookie.indexOf("=") + 1));
        codeConn.header("Cookie", xsrf_token_1 + "; " + jsessionid_1);
        Response codeResponse = codeConn.ignoreContentType(true).method(Method.GET).execute();
        return codeResponse.cookies().toString();
    }


}
