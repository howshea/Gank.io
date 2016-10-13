package com.haipo.gankio.net;

import com.haipo.gankio.Meizi;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by haipo on 2016/10/13.
 */

public interface ApiService {

    @GET("data/%E7%A6%8F%E5%88%A9/{count}/{page}")
    Observable<Meizi> getMeizi(@Path("count") int count, @Path("page") int page);
}
