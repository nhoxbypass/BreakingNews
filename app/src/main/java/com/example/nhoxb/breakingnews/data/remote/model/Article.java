package com.example.nhoxb.breakingnews.data.remote.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by nhoxb on 10/21/2016.
 */

@Parcel
public class Article {

    @SerializedName("_id")
    String id;
    @SerializedName("web_url")
    String webUrl;
    @SerializedName("snippet")
    String snippet;
    @SerializedName("pub_date")
    String publishDate;
    @SerializedName("news_desk")
    String newsDesk;
    @SerializedName("section_name")
    String sectionName;
    @SerializedName("multimedia")
    List<Multimedia> multimediaList;

    // empty constructor needed by the Parceler library
    public Article() {
    }

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
