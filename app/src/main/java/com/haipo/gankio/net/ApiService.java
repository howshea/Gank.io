package com.haipo.gankio.net;

import com.haipo.gankio.Entity.MeiziList;

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
    Observable<MeiziList> getGank(@Path("type") String type, @Path("count") int count, @Path("page") int page);
}
