package edu.cczu.table.lib;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Crawler {
    static private Crawler instance;

    private Crawler() {
    }


    public static Crawler getInstance() {
        if (instance == null) {
            return instance = new Crawler();
        }
        return instance;
    }

    public List<List<String>> getTimeTable(String username, String password) throws IOException {
        final String ROOT = "http://jwgl.cczu.edu.cn/";
        final String URL_LOGIN = ROOT + "login_xk.aspx";
        final String URL_TIMETABLE = ROOT + "web_jxrw/cx_kb_xsgrkb.aspx";

        Map<String, String> login_data = new HashMap<>();
        login_data.put("__VIEWSTATE", "/wEPDwUJLTEzMzgyMzYxD2QWAgIDD2QWAgIHDxYCHglpbm5lcmh0bWxlZGTJqVoKHoads78DQVlmweLpZesN/w==");
        login_data.put("__VIEWSTATEGENERATOR", "B3276D7E");
        login_data.put("getpassword", "登录");
        login_data.put("UserName", username);
        login_data.put("Password", password);

        Connection.Response res = Jsoup.connect(URL_LOGIN).data(login_data).method(Connection.Method.POST).execute();
        Map<String, String> loginCookies = res.cookies();
        Document doc = Jsoup.connect(URL_TIMETABLE).cookies(loginCookies).get();

        Elements table = doc.select("#GVxkkb");
        Elements rows = table.select(".dg1-item");
        List<List<String>> result = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            result.add(new ArrayList<String>());
        }

        for (int i = 0; i < rows.size(); i++) {
            Elements courses = rows.get(i).select("td");
            for (int j = 1; j < courses.size(); j++) {
                String info = courses.get(j).text();
                result.get(j - 1).add(info);
            }
        }
        return result;
    }
}
