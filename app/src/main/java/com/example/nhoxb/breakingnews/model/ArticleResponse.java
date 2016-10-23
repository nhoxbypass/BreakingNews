package com.example.nhoxb.breakingnews.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by nhoxb on 10/21/2016.
 */

@Parcel
public class ArticleResponse {
    // empty constructor needed by the Parceler library
    public ArticleResponse() {
    }

    @SerializedName("docs")
    private List<Article> listArticle;

    public List<Article> getListArticle() {
        return listArticle;
    }
}
