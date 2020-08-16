package com.max.timemaster

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith



@RunWith(AndroidJUnit4::class)
@LargeTest
class FragmentNav {
    @Rule
    @JvmField
    var activityActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun favoriteFragmentNav(){
        activityActivityTestRule.launchActivity(Intent())

        onView(withId(R.id.navigation_favorite)).perform(click()).check(matches(isDisplayed()))

        onView(withId(R.id.toolbar_title)).check(matches(withText("喜好")))
        Thread.sleep(3000)
    }

    @Test
    fun costFragmentNav(){
        activityActivityTestRule.launchActivity(Intent())

        onView(withId(R.id.navigation_cost)).perform(click())

        onView(withId(R.id.toolbar_title)).check(matches(withText("花費")))
        Thread.sleep(3000)
    }

    @Test
    fun profileFragmentNav(){
        activityActivityTestRule.launchActivity(Intent())

        onView(withId(R.id.navigation_profile)).perform(click())

        onView(withId(R.id.toolbar_title)).check(matches(withText("個人")))
        Thread.sleep(3000)
    }
    @Test
    fun calendarFragmentNav(){
        activityActivityTestRule.launchActivity(Intent())

        onView(withId(R.id.navigation_calendar)).perform(click())

        onView(withId(R.id.toolbar_title)).check(matches(withText("時間管理大師")))
        Thread.sleep(3000)
    }

    @Test
    fun editDateBirthday(){
        activityActivityTestRule.launchActivity(Intent())

        onView(withId(R.id.navigation_calendar)).perform(click())

    }
}