package com.example.nhoxb.breakingnews.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nhoxb on 10/21/2016.
 */
public class Article {

    @SerializedName("_id")
    private String id;
    @SerializedName("web_url")
    private String webUrl;
    @SerializedName("snippet")
    private String snippet;
    @SerializedName("pub_date")
    private String publishDate;
    @SerializedName("news_desk")
    private String newsDesk;
    @SerializedName("section_name")
    private String sectionName;
    @SerializedName("multimedia")
    private List<Multimedia> multimediaList;

    public Article(String id, String webUrl, String snippet, String publishDate, String newsDesk, String sectionName, List<Multimedia> multimediaList) {
        this.id = id;
        this.webUrl = webUrl;
        this.snippet = snippet;
        this.publishDate = publishDate;
        this.newsDesk = newsDesk;
        this.sectionName = sectionName;
        this.multimediaList = multimediaList;
    }

    public String getSnippet() {
        return snippet;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getId() {
        return id;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public String getNewsDesk() {
        return newsDesk;
    }

    public String getSectionName() {
        return sectionName;
    }

    public List<Multimedia> getMultimediaList() {
        return multimediaList;
    }
}
