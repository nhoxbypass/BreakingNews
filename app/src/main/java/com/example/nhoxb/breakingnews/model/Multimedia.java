package com.example.nhoxb.breakingnews.model;

import com.example.nhoxb.breakingnews.utils.Constants;

/**
 * Created by nhoxb on 10/21/2016.
 */
public class Multimedia {

    private Integer width;
    private String url;
    private Integer height;
    private String subtype;
    private String type;

    public Multimedia(Integer width, String url, Integer height, String subtype, String type)
    {
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
        return Constants.IMG_BASE_URL + url;
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