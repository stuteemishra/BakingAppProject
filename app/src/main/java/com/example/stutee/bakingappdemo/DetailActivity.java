package com.example.stutee.bakingappdemo;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

public class DetailActivity extends AppCompatActivity implements RecipeDetailFragment.OnRecipeDetailClickListener,RecipeDetailFragment.OnRecipeIngredientClickListener {

    boolean tabletSize;
    int n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tabletSize = getResources().getBoolean(R.bool.isTablet);

        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);

        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onRecipeStepSelected(String description, String videoUrl, String thumbnail) {

        if(tabletSize){
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setmStepDescription(description);
            stepDetailFragment.setmStepVideoURL(videoUrl);
            Bundle bundle = new Bundle();
            bundle.putString("thumbnail",thumbnail);
            stepDetailFragment.setArguments(bundle);
            FragmentManager fragmentManagerSteps = getSupportFragmentManager();
            fragmentManagerSteps.beginTransaction()
                    .replace(R.id.step_detail_container, stepDetailFragment)
                    .addToBackStack(null).commit();
        }
        else{
            n = 1;
            Context context = this;
            Class destinationClass = StepDetailActivity.class;
            Intent intentToStartDetailActivity = new Intent(context, destinationClass);
            Bundle extras = new Bundle();

            extras.putString("fullDescription",description);
            extras.putString("videoUrl",videoUrl);
            extras.putString("thumbnail",thumbnail);
            extras.putInt("flag",n);

            intentToStartDetailActivity.putExtras(extras);

            startActivity(intentToStartDetailActivity);
        }
        Log.v(DetailActivity.class.getSimpleName(),"Value of mTwoPane: "+tabletSize);
    }

    @Override
    public void onRecipeIngredientSelected(String[] quantity, String[] measure, String[] ingredient) {
        if(tabletSize){
            IngredientDetailFragment ingredientDetailFragment = new IngredientDetailFragment();
            ingredientDetailFragment.setIngredientName(ingredient);
            ingredientDetailFragment.setIngredientQuantity(quantity);
            ingredientDetailFragment.setIngredientMeasure(measure);
            FragmentManager fragmentManagerIngredient = getSupportFragmentManager();
            fragmentManagerIngredient.beginTransaction()
                    .replace(R.id.step_detail_container, ingredientDetailFragment)
                    .addToBackStack(null).commit();
        }
        else{
            n=0;
            Context context = this;
            Class destinationClass = StepDetailActivity.class;
            Intent intentToStartDetailActivity = new Intent(context, destinationClass);
            Bundle extras = new Bundle();

            extras.putStringArray("quantity",quantity);
            extras.putStringArray("measure",measure);
            extras.putStringArray("ingredient",ingredient);
            extras.putInt("flag",n);

            intentToStartDetailActivity.putExtras(extras);

            startActivity(intentToStartDetailActivity);
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checking the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if(getSupportActionBar()!=null) {
                getSupportActionBar().hide();
            }


        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            //To show the action bar
            if(getSupportActionBar()!=null) {
                getSupportActionBar().show();
            }

        }
    }
}
