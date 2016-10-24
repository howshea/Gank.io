package com.howshea.gankio.net;

import com.howshea.gankio.Entity.GankList;
import com.howshea.gankio.Entity.History;
import com.howshea.gankio.Entity.MeiziList;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by haipo on 2016/10/13.
 */

public interface ApiService {

    @GET("data/福利/{count}/{page}")
    Observable<MeiziList> getMeizi(@Path("count") int count, @Path("page") int page);


    @GET("data/{type}/{count}/{page}")
    Observable<GankList> getGank(@Path("type") String type, @Path("count") int count, @Path("page") int page);

    @GET("day/history")
    Observable<History> getDate();
}
