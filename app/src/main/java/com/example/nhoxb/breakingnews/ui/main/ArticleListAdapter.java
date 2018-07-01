package com.example.nhoxb.breakingnews.ui.main;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nhoxb.breakingnews.R;
import com.example.nhoxb.breakingnews.data.remote.model.Article;
import com.example.nhoxb.breakingnews.data.remote.model.Multimedia;
import com.example.nhoxb.breakingnews.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    public void setOnItemClickListener(OnArticleClickListener listener) {
        this.mListener = listener;
    }

    public void setArticles(List<Article> articleList) {
        this.mArticleList.clear();
        this.mArticleList.addAll(articleList);
        notifyDataSetChanged();
    }

    public void addArticles(List<Article> articleList) {
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
        switch (viewType) {
            case NORMAL_ARTICLE:
                itemView = layoutInflater.inflate(R.layout.item_article, parent, false);
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
                    bindNormalArticle((ArticleViewHolder) holder, article);
                    break;

                case TEXT_ARTICLE:
                    bindTextArticle((TextArticleViewHolder) holder, article);
                    break;
            }
        }
    }

    private void bindNormalArticle(ArticleViewHolder holder, Article article) {
        Multimedia multimedia = article.getMultimediaList().get(0);

        ViewGroup.LayoutParams params = holder.imageView.getLayoutParams();
        params.height = (int) UiUtils.convertDpToPixel(multimedia.getHeight(), holder.imageView.getContext());
        holder.imageView.setLayoutParams(params);
        Glide.with(holder.imageView.getContext())
                .load(multimedia.getUrl())
                .into(holder.imageView);
        holder.textView.setText(article.getSnippet());
    }

    private void bindTextArticle(TextArticleViewHolder viewHolder, Article article) {
        viewHolder.textView.setText(article.getSnippet());
    }


    @Override
    public int getItemCount() {
        return mArticleList.size();
    }

    public interface OnArticleClickListener {
        void onArticleClick(Article article);
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_article)
        public ImageView imageView;
        @BindView(R.id.tv_description)
        public TextView textView;
        @BindView(R.id.cv_item_container)
        public CardView itemContainer;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemContainer.setOnClickListener(view -> mListener.onArticleClick(mArticleList.get(getAdapterPosition())));
        }
    }

    class TextArticleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_description)
        public TextView textView;

        public TextArticleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            textView.setOnClickListener(view -> mListener.onArticleClick(mArticleList.get(getAdapterPosition())));
        }
    }
}
