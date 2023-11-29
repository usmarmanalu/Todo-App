package com.dicoding.todoapp.ui.list

import androidx.test.core.app.*

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.*
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.*
import com.dicoding.todoapp.*
import com.dicoding.todoapp.ui.add.*
import io.github.kakaocup.kakao.common.utilities.*
import org.junit.*
import com.dicoding.todoapp.R
import org.junit.runner.*

//TODO 16 : Write UI test to validate when user tap Add Task (+), the AddTaskActivity displayed
@RunWith(AndroidJUnit4ClassRunner::class)
class TaskActivityTest {

    @Before
    fun setUp() {
        ActivityScenario.launch(TaskActivity::class.java)
        Intents.init()
    }

    @After
    fun destroy() {
        Intents.release()
    }

    @Test
    fun testNavigateToAddTaskActivity() {
        onView(withId(R.id.fab)).perform(click())
        intended(hasComponent(AddTaskActivity::class.java.name))
    }
}