package com.example.nhoxb.breakingnews.data.remote.model;

import com.example.nhoxb.breakingnews.data.remote.ApiEndPoint;

import org.parceler.Parcel;

/**
 * Created by nhoxb on 10/21/2016.
 */

@Parcel
public class Multimedia {

    Integer width;
    String url;
    Integer height;
    String subtype;
    String type;

    // empty constructor needed by the Parceler library
    public Multimedia() {
    }

    public Multimedia(Integer width, String url, Integer height, String subtype, String type) {
        this.width = width;
        this.url = url;
        this.height = height;
        this.subtype = subtype;
        this.type = type;
    }

    public Integer getWidth() {
        return width;
    }

    public String getUrl() {
        return ApiEndPoint.IMG_BASE_URL + url;
    }

    public Integer getHeight() {
        return height;
    }

    public String getSubtype() {
        return subtype;
    }

    public String getType() {
        return type;
    }
}
