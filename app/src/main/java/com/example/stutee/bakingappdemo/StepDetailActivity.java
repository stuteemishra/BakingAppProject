package com.example.stutee.bakingappdemo;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class StepDetailActivity extends AppCompatActivity {

    int a;
    public String[] ingredientQuantity;
    public String[] ingredientMeasure;
    public String[] ingredientName;
    public String stepDescription;
    public String stepVideoURL;
    public String stepThumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            Bundle extras = intentThatStartedThisActivity.getExtras();
            a = extras.getInt("flag");
            ingredientName = extras.getStringArray("ingredient");
            ingredientQuantity = extras.getStringArray("quantity");
            ingredientMeasure = extras.getStringArray("measure");
            stepDescription = extras.getString("fullDescription");
            stepVideoURL = extras.getString("videoUrl");
            stepThumbnail = extras.getString("thumbnail");
        }



        if(a == 0){
            IngredientDetailFragment ingredientDetailFragment = new IngredientDetailFragment();
            ingredientDetailFragment.setIngredientName(ingredientName);
            ingredientDetailFragment.setIngredientQuantity(ingredientQuantity);
            ingredientDetailFragment.setIngredientMeasure(ingredientMeasure);
            FragmentManager fragmentManagerIngredient = getSupportFragmentManager();
            fragmentManagerIngredient.beginTransaction()
                    .add(R.id.ingredient_detail_container, ingredientDetailFragment)
                    .commit();
        }



        if(a == 1){
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setmStepDescription(stepDescription);
            stepDetailFragment.setmStepVideoURL(stepVideoURL);
            Bundle bundle = new Bundle();
            bundle.putString("thumbnail",stepThumbnail);
            stepDetailFragment.setArguments(bundle);
            FragmentManager fragmentManagerSteps = getSupportFragmentManager();
            fragmentManagerSteps.beginTransaction()
                    .add(R.id.step_detail_container, stepDetailFragment)
                    .commit();
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
}
