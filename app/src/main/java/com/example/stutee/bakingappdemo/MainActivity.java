package com.example.stutee.bakingappdemo;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.stutee.bakingappdemo.IdlingResource.IdlingResourceForTest;
import com.example.stutee.bakingappdemo.Utils.NetworkUtils;
import com.example.stutee.bakingappdemo.Utils.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler {

    private static final int LOADER_ID = 10;

    @BindView(R.id.rv_recipe_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_error_message_display)
    TextView mErrorMessageDisplayTv;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mProgressBar;

    public RecipeAdapter mAdapter;
    public String[] urls;
    String[] parsedRecipeName;
    String[] parsedRecipeImage;

    String[] eachRecipeIngredientQuantity;
    String[] eachRecipeIngredientMeasure;
    String[] eachRecipeIngredientsIngredient;
    String[] eachRecipeStepsShortDescription;
    String[] eachRecipeStepsDescription;
    String[] eachRecipeStepsVideoUrl;
    String[] eachRecipeStepThumbnail;

    String[][] parsedRecipeIngredientQuantity;
    String[][] parsedRecipeIngredientMeasure;
    String[][] parsedRecipeIngredientsIngredient;
    String[][] parsedRecipeStepsShortDescription;
    String[][] parsedRecipeStepsDescription;
    String[][] parsedRecipeStepsVideoUrl;
    String[][] parsedRecipeStepThumbnail;

    @Nullable

    private IdlingResourceForTest mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link IdlingResourceForTest}.
     */

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource()
    {
        if (mIdlingResource == null)
        {

            mIdlingResource = new IdlingResourceForTest();

        }

        return mIdlingResource;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        urls = new String[4];
        urls[0] = "http://assets.epicurious.com/photos/54ac95e819925f464b3ac37e/master/pass/51229210_nutella-pie_1x1.jpg";
        urls[1] = "https://fthmb.tqn.com/1Qnl78FM1cUtOhd5we7vYajzhLE=/2111x1345/filters:fill(auto,1)/1-browniegiant-56b815083df78c0b1364ee87.jpg";
        urls[2] = "http://assets.epicurious.com/photos/57c5b45184c001120f616523/master/pass/moist-yellow-cake.jpg";
        urls[3] = "https://crustabakes.files.wordpress.com/2011/04/99-ny-cheesecake.jpg";

        mAdapter = new RecipeAdapter(this,this);

        int mNoOfColumns = Utilities.calculateNoOfColumns(getApplicationContext());

        GridLayoutManager layoutManager

                = new GridLayoutManager(this,mNoOfColumns);



        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        Bundle bundleForLoader = new Bundle();

        getSupportLoaderManager().initLoader(LOADER_ID, bundleForLoader,loaderListener);
    }

    private LoaderManager.LoaderCallbacks<String> loaderListener = new LoaderManager.LoaderCallbacks<String>() {

        @NonNull

        @Override

        public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
            return new AsyncTaskLoader<String>(MainActivity.this) {

                String mRecipeJsonResult;

                @Override
                protected void onStartLoading() {
                    if (mRecipeJsonResult != null) {
                        deliverResult(mRecipeJsonResult);
                    } else {

                        mProgressBar.setVisibility(View.VISIBLE);
                        forceLoad();
                    }

                }


                @Override

                public String loadInBackground() {

                    String s = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
                    URL recipeRequestUrl = NetworkUtils.buildUrl(s);

                    try {

                        String jsonRecipeResponse = NetworkUtils
                                .getResponseFromHttpUrl(recipeRequestUrl);

                        return jsonRecipeResponse;
                    } catch (Exception e) {

                        e.printStackTrace();

                        return null;
                    }

                }

                public void deliverResult(String data) {

                    mRecipeJsonResult = data;
                    super.deliverResult(data);
                }

            };
        }


        @Override
        public void onLoadFinished(@NonNull Loader<String> loader, String data) {

            if (data == null) {

                mErrorMessageDisplayTv.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.INVISIBLE);

            } else {

                mProgressBar.setVisibility(View.INVISIBLE);
                mErrorMessageDisplayTv.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);

                final String SHOW_NAME = "name";
                final String STEPS = "steps";

                try {

                    JSONArray jsonArray = new JSONArray(data);

                    parsedRecipeName = new String[jsonArray.length()];
                    parsedRecipeImage = new String[jsonArray.length()];
                    parsedRecipeIngredientQuantity = new String[jsonArray.length()][];
                    parsedRecipeIngredientMeasure = new String[jsonArray.length()][];
                    parsedRecipeIngredientsIngredient = new String[jsonArray.length()][];
                    parsedRecipeStepsShortDescription = new String[jsonArray.length()][];
                    parsedRecipeStepsDescription = new String[jsonArray.length()][];
                    parsedRecipeStepsVideoUrl = new String[jsonArray.length()][];
                    parsedRecipeStepThumbnail = new String[jsonArray.length()][];

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String recipeNameString = jsonObject.getString(SHOW_NAME);
                        String recipeImageUrl = jsonObject.getString("image");

                        JSONArray ingredientArray = jsonObject.optJSONArray("ingredients");
                        eachRecipeIngredientQuantity = new String[ingredientArray.length()];
                        eachRecipeIngredientMeasure = new String[ingredientArray.length()];
                        eachRecipeIngredientsIngredient = new String[ingredientArray.length()];

                        for (int j = 0; j < ingredientArray.length(); j++) {
                            JSONObject ingredientObject = ingredientArray.getJSONObject(j);
                            String quantity = ingredientObject.getString("quantity");
                            String measure = ingredientObject.getString("measure");
                            String ingredient = ingredientObject.getString("ingredient");

                            eachRecipeIngredientQuantity[j] = quantity;
                            eachRecipeIngredientMeasure[j] = measure;
                            eachRecipeIngredientsIngredient[j] = ingredient;
                        }

                        JSONArray stepsArray = jsonObject.optJSONArray(STEPS);
                        eachRecipeStepsShortDescription = new String[stepsArray.length()];
                        eachRecipeStepsDescription = new String[stepsArray.length()];
                        eachRecipeStepsVideoUrl = new String[stepsArray.length()];
                        eachRecipeStepThumbnail = new String[stepsArray.length()];

                        for (int k = 0; k < stepsArray.length(); k++) {
                            JSONObject stepsObject = stepsArray.getJSONObject(k);
                            String shortDescription = stepsObject.getString("shortDescription");
                            String description = stepsObject.getString("description");
                            String videoUrl = stepsObject.getString("videoURL");
                            String stepThumbnail = stepsObject.getString("thumbnailURL");
                            eachRecipeStepsShortDescription[k] = shortDescription;
                            eachRecipeStepsDescription[k] = description;
                            eachRecipeStepsVideoUrl[k] = videoUrl;
                            eachRecipeStepThumbnail[k] = stepThumbnail;
                        }

                        parsedRecipeName[i] = recipeNameString;
                        parsedRecipeImage[i] = recipeImageUrl;
                        parsedRecipeIngredientQuantity[i] = eachRecipeIngredientQuantity;
                        parsedRecipeIngredientMeasure[i] = eachRecipeIngredientMeasure;
                        parsedRecipeIngredientsIngredient[i] = eachRecipeIngredientsIngredient;
                        parsedRecipeStepsShortDescription[i] = eachRecipeStepsShortDescription;
                        parsedRecipeStepsDescription[i] = eachRecipeStepsDescription;
                        parsedRecipeStepsVideoUrl[i] = eachRecipeStepsVideoUrl;
                        parsedRecipeStepThumbnail[i] = eachRecipeStepThumbnail;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mAdapter.setRecipeName(parsedRecipeName);
                mAdapter.setRecipeImageUrl(parsedRecipeImage);
                mAdapter.setRecipeIngredientQuantity(parsedRecipeIngredientQuantity);
                mAdapter.setRecipeIngredientMeasure(parsedRecipeIngredientMeasure);
                mAdapter.setRecipeIngredientsIngredient(parsedRecipeIngredientsIngredient);
                mAdapter.setRecipeStepsShortDescription(parsedRecipeStepsShortDescription);
                mAdapter.setRecipeStepsDescription(parsedRecipeStepsDescription);
                mAdapter.setRecipeStepsVideoUrl(parsedRecipeStepsVideoUrl);
                mAdapter.setRecipeStepsThumbnail(parsedRecipeStepThumbnail);
                mAdapter.setUrl(urls);

            }
        }
        @Override
        public void onLoaderReset(@NonNull Loader<String> loader) {

        }
    };

    @Override
    public void onClick(String[] quantity,String[] measure,String[] ingredient,String[] shortDescription,String[] description,String[] videoUrl,String[] thumbnail) {

        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        Bundle extras = new Bundle();

        extras.putStringArray("quantity",quantity);
        extras.putStringArray("measure",measure);
        extras.putStringArray("ingredient",ingredient);
        extras.putStringArray("shortDescription",shortDescription);
        extras.putStringArray("description",description);
        extras.putStringArray("videoUrl",videoUrl);
        extras.putStringArray("thumbnail",thumbnail);


        intentToStartDetailActivity.putExtras(extras);

        startActivity(intentToStartDetailActivity);

    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.main,menu);

        return true;

    }



    @Override

    public boolean onOptionsItemSelected(MenuItem item){

        int itemThatWasClicked = item.getItemId();

        if(itemThatWasClicked == R.id.action_refresh){

            Bundle bundleForLoader = new Bundle();
            getSupportLoaderManager().restartLoader(LOADER_ID, bundleForLoader,loaderListener);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }




}
