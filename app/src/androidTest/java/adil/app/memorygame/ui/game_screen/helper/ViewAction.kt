package adil.app.memorygame.ui.game_screen

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher

/**
 * This method allows us to wait for the specified amount of time.
 * It helps in cases where animations are used and enables us to wait
 * till the animation is finished and then take further actions.
 * @param delay specifies the amount of time to delay in milliseconds.
 */
fun waitFor(delay: Long): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return ViewMatchers.isRoot()
        }

        override fun getDescription(): String {
            return "wait for " + delay + "milliseconds"
        }

        override fun perform(uiController: UiController, view: View?) {
            uiController.loopMainThreadForAtLeast(delay)
        }
    }
}