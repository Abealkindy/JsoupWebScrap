package com.example.myapplication.networks;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;

public class JsoupConfig {
    public static Document setInitJsoup(String url, Map<String, String> cookies) {
        try {
            return Jsoup.connect(url)
                    .header("Accept-Encoding", "gzip, deflate")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                    .cookies(cookies)
                    .execute()
                    .parse();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Document setHtmlParseJsoup(String htmlPage) {
        return Jsoup.parse(htmlPage);
    }
}
