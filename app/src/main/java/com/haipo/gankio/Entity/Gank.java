package com.haipo.gankio.Entity;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by haipo on 2016/10/13.
 */

public class Gank {


    /**
     * _id : 57fd9d61421aa95dd78e8df4
     * createdAt : 2016-10-12T10:18:09.871Z
     * desc : Cell 弹性效果
     * images : ["http://img.gank.io/2178220c-a9bd-46ce-b159-5c6b3df8fd14"]
     * publishedAt : 2016-10-12T11:40:02.146Z
     * source : chrome
     * type : iOS
     * url : https://github.com/anyashka/CellBounceEffect
     * used : true
     * who : 代码家
     */
    @SerializedName("_id")
    private String id;

    private String createdAt;
    private String desc;
    private String publishedAt;
    private String source;
    private String type;
    private String url;
    private boolean used;
    private String who;

    @Nullable
    private List<String> images;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

}

