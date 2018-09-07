package com.example.stutee.bakingappdemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailAdapter extends RecyclerView.Adapter<RecipeDetailAdapter.RecipeDetailAdapterViewHolder> {

    private String[] mShortDescription;
    private String[] mFullDescription;
    private String[] mVideoUrl;
    private String[] mThumbnail;

    private RecipeDetailAdapterOnClickHandler mClickHandler;

    public interface RecipeDetailAdapterOnClickHandler{

        void onClick(String description,String videoUrl,String thumbnail);

    }

    public RecipeDetailAdapter(RecipeDetailAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public RecipeDetailAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_detail_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new RecipeDetailAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeDetailAdapterViewHolder holder, int position) {
        String currentName = mShortDescription[position];
        holder.mShowSteps.setText(currentName);

    }

    @Override
    public int getItemCount() {
        if (null == mShortDescription) return 0;
        return mShortDescription.length;
    }

    public class RecipeDetailAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_recipe_steps)
        TextView mShowSteps;
        public RecipeDetailAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String description = mFullDescription[adapterPosition];
            String videoUrl = mVideoUrl[adapterPosition];
            String thumbnail = mThumbnail[adapterPosition];
            mClickHandler.onClick(description,videoUrl,thumbnail);
        }
    }
    public void setShortDescription(String[] shortDescription) {
        mShortDescription = shortDescription;
        notifyDataSetChanged();
    }
    public void setFullDescription(String[] fullDescription) {
        mFullDescription = fullDescription;
    }

    public void setVideoUrl(String[] videoUrl) {
        mVideoUrl = videoUrl;
    }

    public void setThumbnail(String[] thumbnail) {
        mThumbnail = thumbnail;
    }
}
