package com.example.nhoxb.breakingnews.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nhoxb on 10/21/2016.
 */
public class ArticleResponse {
    @SerializedName("docs")
    private List<Article> listArticle;

    public List<Article> getListArticle() {
        return listArticle;
    }
}
