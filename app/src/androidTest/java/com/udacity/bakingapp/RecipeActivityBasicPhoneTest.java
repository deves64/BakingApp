package com.udacity.bakingapp;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.udacity.bakingapp.repository.RecipeRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RecipeActivityBasicPhoneTest {

    private RecipeAdapterResource mRecipeAdapterResource;

    @Rule
    public ActivityTestRule<RecipeActivity> recipeActivityActivityTestRule
            = new ActivityTestRule<>(RecipeActivity.class, true, false);

    @Before
    public void registerIdlingResource() {
        Intent intent = new Intent();
        intent.putExtra(RecipeActivity.EXTRA_RECIPE_ID, 2);
        recipeActivityActivityTestRule.launchActivity(intent);

        RecipeRepository recipeRepository = (RecipeRepository) recipeActivityActivityTestRule
                .getActivity()
                .detailViewModel
                .getmRecipeRepository();

        mRecipeAdapterResource = new RecipeAdapterResource(recipeRepository);

        Espresso.registerIdlingResources(mRecipeAdapterResource);
    }

    @Test
    public void clickStepListItem_openStepActivity() {
        onView(withId(R.id.steps_fragment))
                .check(matches(isDisplayed()));/*

        onView(withId(R.id.steps_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));*/
    }

    @After
    public void unregisterIdlingResource() {
        if (mRecipeAdapterResource != null) {
            Espresso.unregisterIdlingResources(mRecipeAdapterResource);
        }
    }
}
