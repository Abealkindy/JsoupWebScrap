package com.example.myapplication.activities.mangapage.manga_detail_mvp;

import com.example.myapplication.networks.ApiEndPointService;
import com.example.myapplication.networks.RetrofitConfig;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MangaDetailPresenter {
    private MangaDetailInterface mangaDetailInterface;

    public MangaDetailPresenter(MangaDetailInterface mangaDetailInterface) {
        this.mangaDetailInterface = mangaDetailInterface;
    }

    public void getDetailMangaData(String detailPageURL) {
        String URLAfterCut = detailPageURL.substring(22);
        ApiEndPointService apiEndPointService = RetrofitConfig.getInitMangaRetrofit();
        apiEndPointService.getDiscoverMangaData(URLAfterCut)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String detailHTMLResult) {
                        mangaDetailInterface.onGetDetailDataSuccess(detailHTMLResult);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        mangaDetailInterface.onGetDetailDataFailed();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
