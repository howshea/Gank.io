package com.haipo.gankio.net;

import com.haipo.gankio.Meizi;
import com.haipo.gankio.Result;

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

    private static final int DEFAULT_TIMEOUT =5;

    private Retrofit mRetrofit;
    private ApiService mApiService;

    private HttpRequest(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        mRetrofit =new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        mApiService = mRetrofit.create(ApiService.class);
    }

    public static HttpRequest getInstance(){
        return HttpRequestHolder.sInstance;
    }

    //静态内部类单例模式
    private  static class HttpRequestHolder{
        private static final HttpRequest sInstance = new HttpRequest();
    }

    public void getMeizi(Subscriber<String> subscriber, int count, int page){
        mApiService.getMeizi(count,page)
                .flatMap(new Func1<Meizi, Observable<Result>>() {
                    @Override
                    public Observable<Result> call(Meizi meizi) {
                        return Observable.from(meizi.getResults());
                    }
                })
                .map(new Func1<Result, String>() {
                    @Override
                    public String call(Result result) {
                        return result.getUrl();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
