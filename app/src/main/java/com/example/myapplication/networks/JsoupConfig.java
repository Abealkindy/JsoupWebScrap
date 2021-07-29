package com.example.myapplication.networks;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Objects;

import static com.example.myapplication.MyApp.cookiesz;
import static com.example.myapplication.MyApp.ua;

public class JsoupConfig {
    public static Document setInitJsoup(String url) {
        try {
            return Jsoup.connect(url).timeout(60 * 1000)
                    .header("Accept-Encoding", "gzip, deflate")
                    .userAgent(ua)
                    .cookies(cookiesz)
                    .execute()
                    .parse();
        } catch (IOException e) {
            Log.e("jsoupError", Objects.requireNonNull(e.getMessage()));
            e.printStackTrace();
            return null;
        }
    }

    public static Document setHtmlParseJsoup(String htmlPage) {
        return Jsoup.parse(htmlPage);
    }
}
