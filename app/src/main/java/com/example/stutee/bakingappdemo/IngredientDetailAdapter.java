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

public class IngredientDetailAdapter extends RecyclerView.Adapter<IngredientDetailAdapter.IngredientDetailAdapterViewHolder> {

    private String[] mIngredientQuantity;
    private String[] mIngredientMeasure;
    private String[] mIngredientName;

    @NonNull
    @Override
    public IngredientDetailAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_ingredient_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new IngredientDetailAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientDetailAdapterViewHolder holder, int position) {
        String currentIngredientName = mIngredientName[position];
        String currentIngredientQuantity = mIngredientQuantity[position];
        String currentIngredientMeasure = mIngredientMeasure[position];
        holder.ingredientNameTv.setText(currentIngredientName);
        holder.ingredientQuantityTv.setText(currentIngredientQuantity);
        holder.ingredientMeasureTv.setText(currentIngredientMeasure);

    }

    @Override
    public int getItemCount() {
        if (null == mIngredientName) return 0;
        return mIngredientName.length;
    }

    public class IngredientDetailAdapterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ingredient_name)
        TextView ingredientNameTv;
        @BindView(R.id.ingredient_quantity)
        TextView ingredientQuantityTv;
        @BindView(R.id.ingredient_measure)
        TextView ingredientMeasureTv;

        public IngredientDetailAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    public void setIngredientQuantity(String[] ingredientQuantity) {
        mIngredientQuantity = ingredientQuantity;
    }

    public void setIngredientMeasure(String[] ingredientMeasure) {
        mIngredientMeasure = ingredientMeasure;
    }

    public void setIngredientName(String[] ingredientName) {
        mIngredientName = ingredientName;
    }

}

