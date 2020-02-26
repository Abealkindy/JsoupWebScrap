package com.example.myapplication.activities.mangapage.read_manga_mvp;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.myapplication.models.mangamodels.ReadMangaModel;
import com.example.myapplication.networks.JsoupConfig;
import com.google.gson.Gson;
import com.zhkrb.cloudflare_scrape_android.Cloudflare;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReadMangaPresenter {
    private ReadMangaInterface readMangaInterface;
    private ReadMangaModel readMangaModel = new ReadMangaModel();
    private List<ReadMangaModel.AllChapterDatas> allChapterDatasList = new ArrayList<>();
    List<ReadMangaModel.AllChapterDatas> allChapterDatas;

    public ReadMangaPresenter(ReadMangaInterface readMangaInterface) {
        this.readMangaInterface = readMangaInterface;
    }

    public void getMangaContent(String contentURL) {
        Cloudflare cf = new Cloudflare(contentURL);
        cf.setUser_agent("Mozilla/5.0");
        cf.getCookies(new Cloudflare.cfCallback() {
            @Override
            public void onSuccess(List<HttpCookie> cookieList, boolean hasNewUrl, String newUrl) {
                Log.e("getNewURL?", String.valueOf(hasNewUrl));
                Map<String, String> cookies = Cloudflare.List2Map(cookieList);
                if (hasNewUrl) {
                    passToJsoup(newUrl, cookies);
                    Log.e("NEWURL", newUrl);
                } else {
                    passToJsoup(contentURL, cookies);
                }
            }

            @Override
            public void onFail() {
                readMangaInterface.onGetMangaContentDataFailed();
            }
        });
    }

    @SuppressLint("LongLogTag")
    private void passToJsoup(String newUrl, Map<String, String> cookies) {
        Document doc = JsoupConfig.setInitJsoup(newUrl, cookies);
        if (doc != null) {
            //get chapter title
            Elements getChapterTitle = doc.getElementsByTag("h1");
            String chapterTitle = getChapterTitle.text();
            if (chapterTitle.contains(" Bahasa")) {
                chapterTitle = chapterTitle.substring(0, chapterTitle.length() - 17);
            }
            readMangaModel.setChapterTitle(chapterTitle);

            //get all chapter data
            Elements getAllChapterDatas = doc.select("option[value^=https://komikcast.com/chapter/]");
            if (allChapterDatasList != null || !allChapterDatasList.isEmpty()) {
                allChapterDatasList.clear();
            }
            for (Element element : getAllChapterDatas) {
                String allChapterTitles = element.getElementsContainingOwnText("Chapter").text();
                String allChapterURLs = element.absUrl("value");
                ReadMangaModel.AllChapterDatas chapterDatas = new ReadMangaModel().new AllChapterDatas();
                chapterDatas.setChapterTitle(allChapterTitles);
                chapterDatas.setChapterUrl(allChapterURLs);
                allChapterDatasList.add(chapterDatas);
            }
            allChapterDatas = removeDuplicates(allChapterDatasList);

            //get previous chapter URL
            Elements getPreviousChapterURL = doc.select("a[rel=prev]");
            if (getPreviousChapterURL == null || getPreviousChapterURL.isEmpty()) {
                readMangaModel.setPreviousMangaURL(null);
            } else {
                Element prevElement = getPreviousChapterURL.get(0);
                String previousChapterUrl = prevElement.absUrl("href");
                readMangaModel.setPreviousMangaURL(previousChapterUrl);
            }

            //get next chapter URL
            Elements getNextChapterURL = doc.select("a[rel=next]");
            if (getNextChapterURL == null || getNextChapterURL.isEmpty()) {
                readMangaModel.setNextMangaURL(null);
            } else {
                Element nextElement = getNextChapterURL.get(0);
                String nextChapButtonterUrl = nextElement.absUrl("href");
                readMangaModel.setNextMangaURL(nextChapButtonterUrl);
            }

            //get manga image content
            Elements getMangaImageContentNewerSeries = doc.select("img[src^=https://cdn.komikcast.com/wp-content/]");
            Elements getMangaImageContentOlderSeries = doc.select("img[src^=https://i0.wp.com/lh3.googleusercontent.com/]");

            Elements getMangaImageContentOtherSeries = doc.select("img[src^=https://docs.google.com/uc?export=view]");
            Elements getMangaImageContentOtherAgainSeries = doc.select("img[src^=https://4.bp.blogspot.com/]");
            Elements getMangaImageContentOther1Series = doc.select("img[src^=https://3.bp.blogspot.com/]");
            Elements getMangaImageContentOther2Series = doc.select("img[src^=https://1.bp.blogspot.com/]");
            Elements getMangaImageContentOther3Series = doc.select("img[src^=https://2.bp.blogspot.com/]");
            if (readMangaModel.getImageContent() != null || !readMangaModel.getImageContent().isEmpty()) {
                readMangaModel.getImageContent().clear();
            }
            if ((getMangaImageContentNewerSeries == null || getMangaImageContentNewerSeries.isEmpty())
                    && (getMangaImageContentOlderSeries == null || getMangaImageContentOlderSeries.isEmpty())) {
                Log.e("getContentWithNewerandgetContentWithOlder", "null");
                for (Element element : getMangaImageContentOtherSeries) {
                    String mangaContent = element.absUrl("src");
                    readMangaModel.getImageContent().add(mangaContent);
                }
            }
            if ((getMangaImageContentNewerSeries == null || getMangaImageContentNewerSeries.isEmpty())
                    && (getMangaImageContentOlderSeries == null || getMangaImageContentOlderSeries.isEmpty())
                    && (getMangaImageContentOtherSeries.isEmpty() || getMangaImageContentOtherSeries == null)) {
                for (Element element : getMangaImageContentOtherAgainSeries) {
                    String mangaContent = element.absUrl("src");
                    readMangaModel.getImageContent().add(mangaContent);
                }
            }
            if ((getMangaImageContentNewerSeries == null || getMangaImageContentNewerSeries.isEmpty())
                    && (getMangaImageContentOlderSeries == null || getMangaImageContentOlderSeries.isEmpty())
                    && (getMangaImageContentOtherSeries == null || getMangaImageContentOtherSeries.isEmpty())
                    && (getMangaImageContentOtherAgainSeries.isEmpty() || getMangaImageContentOtherAgainSeries == null)) {
                for (Element element : getMangaImageContentOther1Series) {
                    String mangaContent = element.absUrl("src");
                    readMangaModel.getImageContent().add(mangaContent);
                }
            }
            if ((getMangaImageContentNewerSeries == null || getMangaImageContentNewerSeries.isEmpty())
                    && (getMangaImageContentOlderSeries == null || getMangaImageContentOlderSeries.isEmpty())
                    && (getMangaImageContentOtherSeries.isEmpty() || getMangaImageContentOtherSeries == null)
                    && (getMangaImageContentOtherAgainSeries.isEmpty() || getMangaImageContentOtherAgainSeries == null)
                    && (getMangaImageContentOther1Series.isEmpty() || getMangaImageContentOther1Series == null)) {
                for (Element element : getMangaImageContentOther2Series) {
                    String mangaContent = element.absUrl("src");
                    readMangaModel.getImageContent().add(mangaContent);
                }
            }
            if ((getMangaImageContentNewerSeries == null || getMangaImageContentNewerSeries.isEmpty())
                    && (getMangaImageContentOlderSeries == null || getMangaImageContentOlderSeries.isEmpty())
                    && (getMangaImageContentOtherSeries.isEmpty() || getMangaImageContentOtherSeries == null)
                    && (getMangaImageContentOtherAgainSeries.isEmpty() || getMangaImageContentOtherAgainSeries == null)
                    && (getMangaImageContentOther1Series.isEmpty() || getMangaImageContentOther1Series == null)
                    && (getMangaImageContentOther2Series.isEmpty() || getMangaImageContentOther2Series == null)) {
                for (Element element : getMangaImageContentOther3Series) {
                    String mangaContent = element.absUrl("src");
                    readMangaModel.getImageContent().add(mangaContent);
                }
            }
            if (getMangaImageContentNewerSeries == null || getMangaImageContentNewerSeries.isEmpty()) {
                Log.e("getContentWithNewer", "null");
                for (Element element : getMangaImageContentOlderSeries) {
                    String mangaContent = element.absUrl("src");
                    readMangaModel.getImageContent().add(mangaContent);
                }
            } else {
                Log.e("getContentWithNewer", "success");
                for (Element element : getMangaImageContentNewerSeries) {
                    String mangaContent = element.absUrl("src");
                    readMangaModel.getImageContent().add(mangaContent);
                }
            }

            Log.e("mangaChapterContent", new Gson().toJson(readMangaModel.getImageContent()));

            //get manga detail page URL
            Elements getMangaDetail = doc.select("a[href^=https://komikcast.com/komik/]");
            readMangaModel.setMangaDetailURL(getMangaDetail.attr("href"));

            //store data from JSOUP
            readMangaInterface.onGetMangaChaptersDataSuccess(allChapterDatas);
            readMangaInterface.onGetMangaContentDataSuccess(readMangaModel);
        } else {
            readMangaInterface.onGetMangaContentDataFailed();
        }
    }

    private static List<ReadMangaModel.AllChapterDatas> removeDuplicates(List<ReadMangaModel.AllChapterDatas> list) {

        // Create a new LinkedHashSet
        // Add the elements to set
        Set<ReadMangaModel.AllChapterDatas> set = new LinkedHashSet<>(list);

        // Clear the list
        list.clear();

        // add the elements of set
        // with no duplicates to the list
        list.addAll(set);

        // return the list
        return list;
    }
}
