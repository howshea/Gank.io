package com.howshea.gankio.net;

import android.content.Context;

import com.howshea.gankio.Entity.Gank;
import com.howshea.gankio.Entity.GankList;
import com.howshea.gankio.Entity.History;
import com.howshea.gankio.Entity.Meizi;
import com.howshea.gankio.Entity.MeiziList;
import com.howshea.gankio.Utils.Utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by haipo on 2016/10/13.
 */

public class HttpRequest {

    public static final String BASE_URL = "http://gank.io/api/";

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit mRetrofit;
    private ApiService mApiService;

    private HttpRequest() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        mApiService = mRetrofit.create(ApiService.class);
    }

    public static HttpRequest getInstance() {
        return HttpRequestHolder.sInstance;
    }

    //静态内部类单例模式
    private static class HttpRequestHolder {
        private static final HttpRequest sInstance = new HttpRequest();
    }

    public void getMeizi(Subscriber<Meizi> subscriber, int count, int page, final Context context) {
        mApiService.getMeizi(count, page)
                .flatMap(new Func1<MeiziList, Observable<Meizi>>() {
                    @Override
                    public Observable<Meizi> call(MeiziList meiziList) {
                        return Observable.from(meiziList.getMeizis());
                    }
                })
                .map(new Func1<Meizi, Meizi>() {
                    @Override
                    public Meizi call(final Meizi meizi) {
                        float radio = Utils.imageLoader(context,meizi.getUrl());
                        meizi.setRadio(radio);
                        return meizi;
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

   public void getGank(Subscriber<Gank> subscriber,String type,int count, int page){
       mApiService.getGank(type,count,page)
               .flatMap(new Func1<GankList, Observable<Gank>>() {
                   @Override
                   public Observable<Gank> call(GankList gankList) {
                       return Observable.from(gankList.getGanks());
                   }
               })
               .subscribeOn(Schedulers.io())
               .unsubscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(subscriber);
   }

    public void getDate(Subscriber<History> subscriber){
        mApiService.getDate()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

}
