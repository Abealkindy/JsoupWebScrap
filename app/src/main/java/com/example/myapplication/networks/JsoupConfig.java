package com.example.myapplication.networks;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import static com.example.myapplication.MyApp.cookiesz;

public class JsoupConfig {
    public static Document setInitJsoup(String url, Map<String, String> cookies) {
        try {
            return Jsoup.connect(url).timeout(60 * 1000)
                    .header("Accept-Encoding", "gzip, deflate")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                    .cookies(cookiesz)
                    .execute()
                    .parse();
        } catch (IOException e) {
            Log.e("jsoupError", Objects.requireNonNull(e.getMessage()));
            e.printStackTrace();
            return null;
        }
    }

    public static Document setInitJsoup(String url) {
        try {
            return Jsoup.connect(url)
//                    .header("Accept-Encoding", "gzip, deflate")
//                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
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
