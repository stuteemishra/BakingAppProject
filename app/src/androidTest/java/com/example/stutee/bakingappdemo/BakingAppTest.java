package com.example.stutee.bakingappdemo;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class BakingAppTest {
    private IdlingResource mIdlingResource;

    //random number between 0-3 that we will test with,to not test always the same number.
    private int randPosition;

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    public BakingAppTest()

    {
        Random rand = new Random();
        randPosition = rand.nextInt(3);
    }


    @Before
    public void registerIdlingResource()

    {
        mIdlingResource = mMainActivityTestRule.getActivity().getIdlingResource();

        // To prove that the test fails, omit this call:
        IdlingRegistry.getInstance().register(mIdlingResource);

    }


    /**
     * This test will check if we retrieved the network data and the Recycler view  at a random possible position is clickable
     */

    @Test
    public void mainActivityBasicTest() {
        onView(withId(R.id.rv_recipe_list)).perform(RecyclerViewActions.actionOnItemAtPosition(randPosition, click()));

    }

    @After
    public void unregisterIdlingResource()

    {

        if (mIdlingResource != null)

        {

            IdlingRegistry.getInstance().unregister(mIdlingResource);

        }

    }
}