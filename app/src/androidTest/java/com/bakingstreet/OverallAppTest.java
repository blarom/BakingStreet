package com.bakingstreet;


import android.content.pm.ActivityInfo;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.test.suitebuilder.annotation.LargeTest;
import android.widget.TextView;

import com.bakingstreet.IdlingResource.TimeDelayIdlingResource;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class OverallAppTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {

        mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        testBasicFunctionality();

        mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        testBasicFunctionality();

    }
    private void testBasicFunctionality() {

        //Click on the recyclerview item at position 1 in MainActivity
        onView(ViewMatchers.withId(R.id.recipes_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
        try {
            //In case click was not performed on recyclerview element, try again
            onView(ViewMatchers.withId(R.id.recipes_recycler_view))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Check that the displayed name is of the correct Recipe (https://stackoverflow.com/questions/36329978/how-to-check-toolbar-title-in-android-instrumental-test?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa)
        String toolbarTitile = getInstrumentation().getTargetContext().getString(R.string.test_title_text1);
        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(toolbarTitile)));

        //Click on the steps recyclerview item at position 4 in MainActivity
        onView(ViewMatchers.withId(R.id.recipe_steps_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(4, click()));

        //Click on Previous Step
        onView(withId(R.id.action_previous_step)).perform(click());

        //Click on Next Step
        onView(withId(R.id.action_next_step)).perform(click());

        //Click on Next Step through the video player (https://www.programcreek.com/java-api-examples/index.php?api=android.support.test.espresso.IdlingRegistry)
        long waitingTime = 3000;
        IdlingPolicies.setMasterPolicyTimeout(waitingTime * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(waitingTime * 2, TimeUnit.MILLISECONDS);
        IdlingResource idlingResource = new TimeDelayIdlingResource(waitingTime);
        IdlingRegistry.getInstance().register(idlingResource);
        try {
            onView(withId(R.id.exo_ffwd)).perform(click());
        } catch (Exception e) {
            e.printStackTrace();
        }
        IdlingRegistry.getInstance().unregister(idlingResource);

        //Click on Back to go to the Recipe Details
        if (onView(withId(R.id.action_back)) != null) onView(withId(R.id.action_back)).perform(click());

        //Flip screen orientations to check if the fragment reloads correctly
        flipOrientation();
        flipOrientation();

        //Go back to recipes list
        onView(withContentDescription("Navigate up")).perform(click());

        //Flip screen orientations to check if the recyclerview reloads at the right position
        flipOrientation();
        flipOrientation();

        //Click on the recyclerview item at position 0 in MainActivity
        onView(ViewMatchers.withId(R.id.recipes_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //Check that the displayed name is of the correct Recipe (https://stackoverflow.com/questions/36329978/how-to-check-toolbar-title-in-android-instrumental-test?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa)
        toolbarTitile = getInstrumentation().getTargetContext().getString(R.string.test_title_text2);
        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(toolbarTitile)));

        //Go back to recipes list
        onView(withContentDescription("Navigate up")).perform(click());

    }
    private void flipOrientation() {
        if (mActivityTestRule.getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
}
