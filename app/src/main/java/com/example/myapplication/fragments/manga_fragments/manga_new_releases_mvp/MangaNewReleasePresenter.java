package com.example.myapplication.fragments.manga_fragments.manga_new_releases_mvp;

import android.util.Log;

import com.zhkrb.cloudflare_scrape_android.Cloudflare;

import java.net.HttpCookie;
import java.util.List;
import java.util.Map;

public class MangaNewReleasePresenter {
    private MangaNewReleaseInterface newReleaseInterface;

    public MangaNewReleasePresenter(MangaNewReleaseInterface newReleaseInterface) {
        this.newReleaseInterface = newReleaseInterface;
    }

    public void getNewReleasesMangaData(int pageCount, String newReleasesURL, String hitStatus) {
        Cloudflare cf = new Cloudflare(newReleasesURL);
        cf.setUser_agent("Mozilla/5.0");
        cf.getCookies(new Cloudflare.cfCallback() {
            @Override
            public void onSuccess(List<HttpCookie> cookieList, boolean hasNewUrl, String newUrl) {
                Log.e("getNewURL?", String.valueOf(hasNewUrl));
                Map<String, String> cookies = Cloudflare.List2Map(cookieList);
                if (hasNewUrl) {
                    passToJsoup(pageCount, newUrl, hitStatus, cookies);
                    Log.e("NEWURL", newUrl);
                } else {
                    passToJsoup(pageCount, newReleasesURL, hitStatus, cookies);
                }
            }

            @Override
            public void onFail() {
                newReleaseInterface.onGetNewReleasesDataFailed();
            }
        });
    }

    private void passToJsoup(int pageCount, String newReleasesURL, String hitStatus, Map<String, String> cookies) {

    }
}
