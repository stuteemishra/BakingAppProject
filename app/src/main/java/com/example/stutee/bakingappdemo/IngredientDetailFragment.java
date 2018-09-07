package com.example.stutee.bakingappdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientDetailFragment extends Fragment {

    @BindView(R.id.rv_ingredient_list)
    RecyclerView mRecyclerView;
    IngredientDetailAdapter mAdapter;

    private String[] ingredientQuantity;
    private String[] ingredientMeasure;
    private String[] ingredientName;

    public void setIngredientQuantity(String[] ingredientQuantity) {
        this.ingredientQuantity = ingredientQuantity;
    }

    public void setIngredientMeasure(String[] ingredientMeasure) {
        this.ingredientMeasure = ingredientMeasure;
    }

    public void setIngredientName(String[] ingredientName) {
        this.ingredientName = ingredientName;
    }

    public IngredientDetailFragment() {

    }


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_recipe_ingredients, container, false);
        ButterKnife.bind(this,rootView);

        mAdapter = new IngredientDetailAdapter();
        mAdapter.setIngredientName(ingredientName);
        mAdapter.setIngredientQuantity(ingredientQuantity);
        mAdapter.setIngredientMeasure(ingredientMeasure);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(mAdapter);


        return rootView;

    }
}
