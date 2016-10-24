package com.howshea.gankio.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by haipo on 2016/10/13.
 */

public class GankList {

    Boolean error;

    @SerializedName("results")
    List<Gank> mGanks;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<Gank> getGanks() {
        return mGanks;
    }

    public void setGanks(List<Gank> ganks) {
        mGanks = ganks;
    }



}
