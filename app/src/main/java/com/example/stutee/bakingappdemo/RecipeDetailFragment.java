package com.example.stutee.bakingappdemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stutee.bakingappdemo.data.BakingAppWidgetContract;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailFragment extends Fragment implements RecipeDetailAdapter.RecipeDetailAdapterOnClickHandler{

    @BindView(R.id.rv_recipe_detail_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.recipe_ingredient)
    TextView mTextViewIngredient;

    RecipeDetailAdapter mAdapter;

    OnRecipeDetailClickListener mCallback;
    OnRecipeIngredientClickListener mIngredientCallback;


    public interface OnRecipeDetailClickListener {

        void onRecipeStepSelected(String description,String videoUrl,String thumbnail);

    }

    public interface OnRecipeIngredientClickListener {

        void onRecipeIngredientSelected(String[] quantity,String[] measure,String[] ingredient);


    }



    @Override

    public void onAttach(Context context) {

        super.onAttach(context);

        try {

            mCallback = (OnRecipeDetailClickListener) context;
            mIngredientCallback = (OnRecipeIngredientClickListener) context;

        } catch (ClassCastException e) {

            throw new ClassCastException(context.toString()

                    + " must implement OnImageClickListener");

        }

    }

    // Mandatory empty constructor
    public RecipeDetailFragment() {

    }

    private String[] shortDescription;
    private String[] fullDescription;
    private String[] videoUrl;
    private String[] thumbnail;
    private String[] ingredientName;
    private String[] ingredientQuantity;
    private String[] ingredientMeasure;


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        final View rootView = inflater.inflate(R.layout.fragment_recipe_detail_list, container, false);
        ButterKnife.bind(this,rootView);

        shortDescription = getActivity().getIntent().getExtras().getStringArray("shortDescription");
        fullDescription = getActivity().getIntent().getExtras().getStringArray("description");
        videoUrl = getActivity().getIntent().getExtras().getStringArray("videoUrl");
        ingredientQuantity = getActivity().getIntent().getExtras().getStringArray("quantity");
        ingredientMeasure = getActivity().getIntent().getExtras().getStringArray("measure");
        ingredientName = getActivity().getIntent().getExtras().getStringArray("ingredient");
        thumbnail = getActivity().getIntent().getExtras().getStringArray("thumbnail");


        mAdapter = new RecipeDetailAdapter(this);

        mAdapter.setShortDescription(shortDescription);
        mAdapter.setFullDescription(fullDescription);
        mAdapter.setVideoUrl(videoUrl);
        mAdapter.setThumbnail(thumbnail);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(mAdapter);

        mTextViewIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mIngredientCallback.onRecipeIngredientSelected(ingredientQuantity,ingredientMeasure,ingredientName);
                Cursor c = getContext().getContentResolver().query(BakingAppWidgetContract.IngredientDetailEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        BakingAppWidgetContract.IngredientDetailEntry._ID);
                if(c == null){
                    Log.v("RecipeDetailFragment","error "+c);
                }
                else if(c.getCount()<1){
                    Log.v("RecipeDetailFragment","no elements "+c);
                    insertIngredientData(getContext());
                }
                else{
                    int count = 0;//Initially zero

                    c.moveToLast();
                    int ingredientNameIndex = c.getColumnIndex(BakingAppWidgetContract.IngredientDetailEntry.INGREDIENT_NAME);
                    String name = c.getString(ingredientNameIndex);
                    Log.v(RecipeDetailFragment.class.getSimpleName(),"name------>"+name);
                    Uri uri = BakingAppWidgetContract.IngredientDetailEntry.CONTENT_URI;
                    count = getContext().getContentResolver().delete(uri, null, null);

                    if(count != 0){

                        insertIngredientData(getContext());
                    }
                    else if(count == 0){

                        Log.v("RecipeDetailFragment","nothing to do..");
                    }
                }

                CollectionAppWidgetProvider.sendRefreshBroadcast(getContext());

            }
        });


        return rootView;


    }

    private static ContentValues createContentValues(String ingredientName, String quantity, String measure){
        ContentValues ingredientValues = new ContentValues();
        ingredientValues.put(BakingAppWidgetContract.IngredientDetailEntry.INGREDIENT_NAME,ingredientName);
        ingredientValues.put(BakingAppWidgetContract.IngredientDetailEntry.INGREDIENT_QUANTITY,quantity);
        ingredientValues.put(BakingAppWidgetContract.IngredientDetailEntry.INGREDIENT_MEASURE,measure);
        return ingredientValues;
    }

    public  void insertIngredientData(Context context){
        List<ContentValues> ingredientDetailValues = new ArrayList<ContentValues>();
        for(int i=0; i<ingredientName.length; i++){

            Log.v(RecipeDetailFragment.class.getSimpleName(),"length:------------"+ingredientName.length);

            ingredientDetailValues.add(RecipeDetailFragment.createContentValues(ingredientName[i],ingredientQuantity[i],ingredientMeasure[i]));

            //Log.v(RecipeDetailFragment.class.getSimpleName(),"name:---------"+ingredientDetailValues.get);

        }
        // Bulk Insert
        int a = context.getContentResolver().bulkInsert(
                BakingAppWidgetContract.IngredientDetailEntry.CONTENT_URI,
                ingredientDetailValues.toArray(new ContentValues[ingredientName.length]));
        Log.v(RecipeDetailFragment.class.getSimpleName(),"no of rows:-------------"+a);
    }

    @Override
    public void onClick(String description, String videoUrl, String thumbnail) {
        mCallback.onRecipeStepSelected(description,videoUrl,thumbnail);
    }
}
