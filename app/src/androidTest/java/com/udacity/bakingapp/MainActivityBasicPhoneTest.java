package com.udacity.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
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
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

@RunWith(AndroidJUnit4.class)
public class MainActivityBasicPhoneTest {

    private RecipeAdapterResource mRecipeAdapterResource;

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        RecipeRepository recipeRepository = (RecipeRepository) mainActivityActivityTestRule
                .getActivity()
                .mainViewModel
                .getmRecipeRepository();

        mRecipeAdapterResource = new RecipeAdapterResource(recipeRepository);

        Espresso.registerIdlingResources(mRecipeAdapterResource);
    }

    @Test
    public void isListRecyclerViewPresent() {
        onView(withContentDescription(R.string.recipe_menu))
                .check(matches(isDisplayed()));
    }

    @Test
    public void clickRecipeListItem_openRecipeActivity() {
        onView(withContentDescription(R.string.recipe_menu))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));

        onView(withId(R.id.steps_fragment))
                .check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        if (mRecipeAdapterResource != null) {
            Espresso.unregisterIdlingResources(mRecipeAdapterResource);
        }
    }
}
