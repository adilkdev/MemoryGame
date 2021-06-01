package adil.app.memorygame.ui.game_screen

import adil.app.memorygame.R
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.hamcrest.CoreMatchers.not
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class GameActivityTest {

    private val smallDelay = 500L
    private val largeDelay = 1000L

    private val firstPosition = 0
    private val secondPosition = 1

    @Test
    fun test_isActivityInView() {
        val activityScenario = ActivityScenario.launch(GameActivity::class.java)
        onView(withId(R.id.board)).check(matches(isDisplayed()))
    }

    @Test
    fun test_navToHighScoreActivity() {
        val activityScenario = ActivityScenario.launch(GameActivity::class.java)
        onView(withId(R.id.textViewScoresBtn)).perform(click())
        onView(withId(R.id.recyclerviewScore)).check(matches(isDisplayed()))
    }

    @Test
    fun test_navToScoreActivityAndBackToGameActivity() {
        val activityScenario = ActivityScenario.launch(GameActivity::class.java)
        onView(withId(R.id.textViewScoresBtn)).perform(click())
        onView(withId(R.id.recyclerviewScore)).check(matches(isDisplayed()))

        onView(withId(R.id.imageViewBack)).perform(click())
        onView(isRoot()).perform(waitFor(largeDelay))
        onView(withId(R.id.board)).check(matches(isDisplayed()))
    }

    @Test
    fun test_showScoreOnChoosingTwoCards() {
        val activityScenario = ActivityScenario.launch(GameActivity::class.java)
        onView(isRoot()).perform(waitFor(largeDelay))
        onView(withId(R.id.recyclerviewGame)).perform(
            actionOnItemAtPosition<GameCardAdapter.GameItemViewHolder>(
                firstPosition,
                click()
            )
        )
        onView(isRoot()).perform(waitFor(smallDelay))
        onView(withId(R.id.recyclerviewGame)).perform(
            actionOnItemAtPosition<GameCardAdapter.GameItemViewHolder>(
                secondPosition,
                click()
            )
        )
        onView(isRoot()).perform(waitFor(smallDelay))
        onView(withId(R.id.textViewScoreOnMove)).check(matches(isDisplayed()))
    }

    @Test
    fun test_doNotShowScoreWhenCardsNotChosen() {
        val activityScenario = ActivityScenario.launch(GameActivity::class.java)
        onView(withId(R.id.textViewScoreOnMove)).check(matches(not(isDisplayed())))
    }

}