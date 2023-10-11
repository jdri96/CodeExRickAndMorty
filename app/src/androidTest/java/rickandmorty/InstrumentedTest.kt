package rickandmorty

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class InstrumentedTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun imageLogoHasClickInListScreen() {
        val logo = composeRule.onNodeWithContentDescription("Rick And Morty")
        logo.performClick()
        logo.assertHasClickAction()
    }

    @Test
    fun listScreenHasNoItemWithCertainTag() {
        val tagToFind = "CertainTag"
        val item = composeRule.onNode(hasTestTag(tagToFind))
        item.assertDoesNotExist()

        composeRule.onRoot().printToLog(TAG)
    }

    companion object {
        const val TAG = "InstrumentedTest_TAG"
    }
}