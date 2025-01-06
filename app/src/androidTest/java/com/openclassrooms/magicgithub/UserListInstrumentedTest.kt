package com.openclassrooms.magicgithub

import android.content.res.Resources
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.openclassrooms.magicgithub.di.Injection.getRepository
import com.openclassrooms.magicgithub.ui.user_list.ListUserActivity
import com.openclassrooms.magicgithub.utils.RecyclerViewUtils.ItemCount
import com.openclassrooms.magicgithub.utils.RecyclerViewUtils.clickChildView
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.GeneralSwipeAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Swipe
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import org.hamcrest.Description
import org.hamcrest.Matcher
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.util.HumanReadables
import androidx.test.espresso.action.MotionEvents
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.Matchers.allOf

/**
 * Instrumented test, which will execute on an Android device.
 * Testing ListUserActivity screen.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class UserListInstrumentedTest {
    @Rule
    @JvmField
    val mActivityRule = ActivityTestRule(ListUserActivity::class.java)

    private var currentUsersSize = -1

    @Before
    fun setup() {
        currentUsersSize = getRepository().getUsers().size
    }

    @Test
    fun checkIfRecyclerViewIsNotEmpty() {
        Espresso.onView(ViewMatchers.withId(R.id.activity_list_user_rv))
            .check(ItemCount(currentUsersSize))
    }

    @Test
    fun checkIfAddingRandomUserIsWorking() {
        Espresso.onView(ViewMatchers.withId(R.id.activity_list_user_fab))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.activity_list_user_rv))
            .check(ItemCount(currentUsersSize + 1))
    }

    @Test
    fun checkIfRemovingUserIsWorking() {
        Espresso.onView(ViewMatchers.withId(R.id.activity_list_user_rv))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    clickChildView(R.id.item_list_user_delete_button)
                )
            )
        Espresso.onView(ViewMatchers.withId(R.id.activity_list_user_rv))
            .check(ItemCount(currentUsersSize - 1))
    }

    @Test
    fun checkIfDragAndDropOrderingIsWorking() {
        // On récupère les noms initiaux
        val users = getRepository().getUsers()
        val firstUserName = users[0].login   // Jake
        val secondUserName = users[1].login  // Paul

        // Action drag and drop
        onView(withId(R.id.activity_list_user_rv))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    ViewActions.swipeDown()
                )
            )

        // Pause pour l'animation
        Thread.sleep(1000)

        // Vérifie que les positions ont changé
        onView(withId(R.id.activity_list_user_rv))
            .check(matches(
                hasDescendant(
                    allOf(
                        withId(R.id.item_list_user_username),
                        withText(secondUserName)
                    )
                )
            ))
    }

    // Helper class pour faciliter le test du RecyclerView
    private fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
        return RecyclerViewMatcher(recyclerViewId)
    }

    class RecyclerViewMatcher(private val recyclerViewId: Int) {
        fun atPosition(position: Int): Matcher<View> {
            return atPositionOnView(position, -1)
        }

        fun atPositionOnView(position: Int, targetViewId: Int): Matcher<View> {
            return object : TypeSafeMatcher<View>() {
                var resources: Resources? = null
                var childView: View? = null

                override fun describeTo(description: Description) {
                    var idDescription = recyclerViewId.toString()
                    if (resources != null) {
                        idDescription = try {
                            resources!!.getResourceName(recyclerViewId)
                        } catch (var4: Resources.NotFoundException) {
                            String.format("%s (resource name not found)", recyclerViewId)
                        }
                    }
                    description.appendText("with id: $idDescription")
                }

                override fun matchesSafely(view: View): Boolean {
                    resources = view.resources
                    if (childView == null) {
                        val recyclerView = view.rootView.findViewById<RecyclerView>(recyclerViewId)
                        if (recyclerView?.id == recyclerViewId) {
                            childView = recyclerView.findViewHolderForAdapterPosition(position)?.itemView
                        } else {
                            return false
                        }
                    }
                    return if (targetViewId == -1) {
                        view === childView
                    } else {
                        val targetView = childView?.findViewById<View>(targetViewId)
                        view === targetView
                    }
                }
            }
        }
    }
    // Helper function pour vérifier la vue à une position spécifique
    private fun atPosition(position: Int, itemMatcher: Matcher<View>): BoundedMatcher<View, RecyclerView> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder = view.findViewHolderForAdapterPosition(position)
                return viewHolder != null && itemMatcher.matches(viewHolder.itemView)
            }
        }
    }

    private fun dragAndDrop(toPosition: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isDisplayed()
            }

            override fun getDescription(): String {
                return "Drag and drop action from position 0 to position $toPosition"
            }

            override fun perform(uiController: UiController, view: View) {
                val startCoord = floatArrayOf(view.width / 2f, view.height / 2f)
                val endCoord = floatArrayOf(view.width / 2f, view.height * (toPosition + 1f))
                val precision = floatArrayOf(1f, 1f)

                // Appui long
                val downResult = MotionEvents.sendDown(uiController, startCoord, precision)
                uiController.loopMainThreadForAtLeast(1000)

                // Mouvement
                val move = MotionEvent.obtain(
                    SystemClock.uptimeMillis(),
                    SystemClock.uptimeMillis(),
                    MotionEvent.ACTION_MOVE,
                    endCoord[0],
                    endCoord[1],
                    0
                )
                uiController.injectMotionEvent(move)
                uiController.loopMainThreadForAtLeast(1000)

                // Relâchement
                MotionEvents.sendUp(uiController, downResult.down, endCoord)
            }
        }
    }

    // Méthodes utilitaires pour les tests
    private fun withBackgroundColor(@ColorInt color: Int): Matcher<View> {
        return object : BoundedMatcher<View, View>(View::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("with background color: ")
                description.appendValue(color)
            }

            override fun matchesSafely(item: View): Boolean {
                val background = item.background
                if (background is ColorDrawable) {
                    return background.color == color
                }
                return false
            }
        }
    }

}