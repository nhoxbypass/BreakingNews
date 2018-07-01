package com.example.nhoxb.breakingnews.data.remote.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by nhoxb on 10/21/2016.
 */

@Parcel
public class ArticleResponse {
    @SerializedName("docs")
    List<Article> listArticle;

    // empty constructor needed by the Parceler library
    public ArticleResponse() {
    }

    public List<Article> getListArticle() {
        return listArticle;
    }
}
