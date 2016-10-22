package com.example.nhoxb.breakingnews.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nhoxb.breakingnews.R;
import com.example.nhoxb.breakingnews.model.Article;
import com.example.nhoxb.breakingnews.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nhoxb on 10/21/2016.
 */
public class ArticleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int NORMAL_ARTICLE = 0;
    private static final int TEXT_ARTICLE = 1;
    //Variables
    List<Article> mArticleList;
    OnArticleClickListener mListener;

    public ArticleListAdapter() {
        mArticleList = new ArrayList<>();
    }

    public ArticleListAdapter(List<Article> articleList) {
        this.mArticleList = articleList;
    }

    public void setOnItemClickListener(OnArticleClickListener listener)
    {
        this.mListener = listener;
    }

    public void setArticles(List<Article> articleList)
    {
        this.mArticleList.clear();
        this.mArticleList.addAll(articleList);
        notifyDataSetChanged();
    }

    public void addArticles(List<Article> articleList)
    {
        int position = this.mArticleList.size();
        this.mArticleList.addAll(articleList);
        notifyItemRangeInserted(position, articleList.size());
    }

    @Override
    public int getItemViewType(int position) {
        if (mArticleList.get(position).getMultimediaList() == null || mArticleList.get(position).getMultimediaList().isEmpty())
            return TEXT_ARTICLE;
        else
            return NORMAL_ARTICLE;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView;
        switch (viewType)
        {
            case NORMAL_ARTICLE:
                itemView = layoutInflater.inflate(R.layout.item_article,parent,false);
                return new ArticleViewHolder(itemView);

            case TEXT_ARTICLE:
                itemView = layoutInflater.inflate(R.layout.item_text_article, parent, false);
                return new TextArticleViewHolder(itemView);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Article article = mArticleList.get(position);

        if (article != null) {
            switch (getItemViewType(position)) {
                case NORMAL_ARTICLE:
                    ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;
                    Picasso.with(articleViewHolder.imageView.getContext())
                            .load(article.getMultimediaList().get(0).getUrl())
                            .into(articleViewHolder.imageView);
                    articleViewHolder.textView.setText(article.getSnippet());
                    articleViewHolder.itemContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mListener.onArticleClick(article);
                        }
                    });
                    break;

                case TEXT_ARTICLE:
                    TextArticleViewHolder textViewHolder = (TextArticleViewHolder) holder;
                    textViewHolder.textView.setText(article.getSnippet());
                    textViewHolder.textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mListener.onArticleClick(article);
                        }
                    });
                    break;
            }
        }
    }


    @Override
    public int getItemCount() {
        return mArticleList.size();
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView imageView;
        public TextView textView;
        public CardView itemContainer;

        public ArticleViewHolder(View itemView) {
            super(itemView);

            itemContainer = (CardView) itemView.findViewById(R.id.cv_item_container);
            imageView = (ImageView) itemView.findViewById(R.id.iv_article);
            textView = (TextView) itemView.findViewById(R.id.tv_description);
        }
    }

    class TextArticleViewHolder extends RecyclerView.ViewHolder
    {
        public TextView textView;

        public TextArticleViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_description);
        }
    }

    public interface OnArticleClickListener
    {
        void onArticleClick(Article article);
    }
}
