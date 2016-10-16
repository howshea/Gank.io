package com.haipo.gankio.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by haipo on 2016/10/13.
 */

public class MeiziList {

    String error;

    @SerializedName("results")
    List<Meizi> mMeizis;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Meizi> getMeizis() {
        return mMeizis;
    }

    public void setMeizis(List<Meizi> meizis) {
        this.mMeizis = meizis;
    }


}
