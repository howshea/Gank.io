package com.haipo.gankio.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by haipo on 2016/10/13.
 */

public class GankList {

    String error;

    @SerializedName("results")
    List<Gank> mGanks;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Gank> getGanks() {
        return mGanks;
    }

    public void setGanks(List<Gank> ganks) {
        mGanks = ganks;
    }
}
