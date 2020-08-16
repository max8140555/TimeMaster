package com.max.timemaster


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AddDateTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"
        )

    @Test
    fun addDateTest() {

        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.navigation_profile),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomNavView),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        Thread.sleep(5000)
        bottomNavigationItemView.perform(click())

        val appCompatButton = onView(
            allOf(
                withId(R.id.btn_add_date), withText("新增對象"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.FrameLayout")),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        Thread.sleep(3000)
        appCompatButton.perform(click())

        val appCompatEditText = onView(
            allOf(
                withId(R.id.editText),
                childAtPosition(
                    allOf(
                        withId(R.id.layout_publish),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        Thread.sleep(3000)
        appCompatEditText.perform(replaceText("test"), closeSoftKeyboard())

        val appCompatButton2 = onView(
            allOf(
                withId(R.id.edit_birthday), withText("輸入生日"),
                childAtPosition(
                    allOf(
                        withId(R.id.layout_publish),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        Thread.sleep(3000)
        appCompatButton2.perform(click())

        val appCompatButton3 = onView(
            allOf(
                withId(android.R.id.button1), withText("確定"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        appCompatButton3.perform(scrollTo(), click())

        val constraintLayout = onView(
            allOf(
                withId(R.id.layout_detail_color),
                childAtPosition(
                    allOf(
                        withId(R.id.recycler_profile_color),
                        childAtPosition(
                            withId(R.id.layout_publish),
                            5
                        )
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        Thread.sleep(3000)
        constraintLayout.perform(click())

        val appCompatButton4 = onView(
            allOf(
                withId(R.id.button_publish), withText("Save"),
                childAtPosition(
                    allOf(
                        withId(R.id.layout_publish),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        Thread.sleep(3000)
        appCompatButton4.perform(click())

    }


    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
