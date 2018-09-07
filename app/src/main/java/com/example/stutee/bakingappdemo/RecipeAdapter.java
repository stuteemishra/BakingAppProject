package com.example.stutee.bakingappdemo;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    Context mContext;

    private String[] mRecipeName;
    private String[] mUrl;
    private String[] mRecipeImageUrl;
    private String[][] mRecipeIngredientQuantity;
    private String[][] mRecipeIngredientMeasure;
    private String[][] mRecipeIngredientsIngredient;
    private String[][] mRecipeStepsShortDescription;
    private String[][] mRecipeStepsDescription;
    private String[][] mRecipeStepsVideoUrl;
    private String[][] mRecipeStepsThumbnail;

    private RecipeAdapterOnClickHandler mClickHandler;

    public interface RecipeAdapterOnClickHandler{

        void onClick(String[] quantity,String[] measure,String[] ingredient,String[] shortDescription,String[] description,String[] videoUrl,String[] thumbnail);

    }

    public RecipeAdapter(Context context, RecipeAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
        mContext = context;
    }




    @NonNull
    @Override
    public RecipeAdapter.RecipeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.RecipeAdapterViewHolder holder, int position) {

        String currentRecipeName = mRecipeName[position];
        String currentRecipeImage = mRecipeImageUrl[position];
        String currentUrl = mUrl[position];
        holder.recipeNameTv.setText(currentRecipeName);

        Uri imageUri = Uri.parse(currentRecipeImage).buildUpon().build();
        Picasso.with(mContext).load(imageUri)
                .into(holder.recipeImageView);
        Picasso.with(mContext).load(currentUrl)
                .into(holder.recipeImageView);


    }

    @Override
    public int getItemCount() {
        if (null == mRecipeName) return 0;
        return mRecipeName.length;
    }

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_recipe_name)
        TextView recipeNameTv;
        @BindView(R.id.recipe_image)
        ImageView recipeImageView;

        public RecipeAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String[] quantity = mRecipeIngredientQuantity[adapterPosition];
            String[] measure = mRecipeIngredientMeasure[adapterPosition];
            String[] ingredient = mRecipeIngredientsIngredient[adapterPosition];
            String[] shortDescription = mRecipeStepsShortDescription[adapterPosition];
            String[] description = mRecipeStepsDescription[adapterPosition];
            String[] videoUrl = mRecipeStepsVideoUrl[adapterPosition];
            String[] thumbnail = mRecipeStepsThumbnail[adapterPosition];
            mClickHandler.onClick(quantity,measure,ingredient,shortDescription,description,videoUrl,thumbnail);

        }
    }

    public void setRecipeName(String[] recipeName) {
        mRecipeName = recipeName;
        notifyDataSetChanged();
    }

    public void setRecipeImageUrl(String[] recipeImageUrl) {
        mRecipeImageUrl = recipeImageUrl;
    }

    public void setRecipeIngredientQuantity(String[][] recipeIngredientQuantity) {
        mRecipeIngredientQuantity = recipeIngredientQuantity;
    }

    public void setRecipeIngredientMeasure(String[][] recipeIngredientMeasure) {
        mRecipeIngredientMeasure = recipeIngredientMeasure;
    }

    public void setRecipeIngredientsIngredient(String[][] recipeIngredientsIngredient) {
        mRecipeIngredientsIngredient = recipeIngredientsIngredient;
    }

    public void setRecipeStepsShortDescription(String[][] recipeStepsShortDescription) {
        mRecipeStepsShortDescription = recipeStepsShortDescription;
    }

    public void setRecipeStepsDescription(String[][] recipeStepsDescription) {
        mRecipeStepsDescription = recipeStepsDescription;
    }

    public void setRecipeStepsVideoUrl(String[][] recipeStepsVideoUrl) {
        mRecipeStepsVideoUrl = recipeStepsVideoUrl;
    }

    public void setRecipeStepsThumbnail(String[][] recipeStepsThumbnail) {
        mRecipeStepsThumbnail = recipeStepsThumbnail;
    }

    public void setUrl(String[] url) {
        mUrl = url;
    }
}
