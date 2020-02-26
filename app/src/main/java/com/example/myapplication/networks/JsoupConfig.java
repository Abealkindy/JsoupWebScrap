package com.example.myapplication.networks;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;

public class JsoupConfig {
    public static Document setInitJsoup(String url, Map<String, String> cookies) {
        try {
            return Jsoup.connect(url).userAgent("Mozilla/5.0").cookies(cookies).execute().parse();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
