package com.haipo.gankio.net;

import com.haipo.gankio.Entity.Meizi;
import com.haipo.gankio.Entity.MeiziList;
import com.haipo.gankio.Utils.Utils;

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

    public void getMeizi(Subscriber<Meizi> subscriber, int count, int page) {
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
                        float radio = Utils.getImageInfo(meizi.getUrl());
//                        Bitmap bitmap = Utils.imageLoader(new MyApp().getContext(), meizi.getUrl());
//                        if (bitmap!=null){
//                            float radio = bitmap.getWidth() / (float) bitmap.getHeight();
//
//                            System.out.println("比例是："+radio);
//                        }
                        meizi.setRadio(radio);
                        return meizi;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


}
