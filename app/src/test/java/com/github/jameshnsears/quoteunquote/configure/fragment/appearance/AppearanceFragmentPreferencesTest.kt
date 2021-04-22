package com.github.jameshnsears.quoteunquote.configure.fragment.appearance

import android.os.Build
import androidx.fragment.app.testing.launchFragment
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.jameshnsears.quoteunquote.utils.logging.ShadowLoggingHelper
import com.github.jameshnsears.quoteunquote.utils.widget.WidgetIdHelper
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class AppearanceFragmentPreferencesTest : ShadowLoggingHelper() {
    @Test
    fun confirmInitialPreferences() {
        with(launchFragment<AppearanceFragmentDouble>()) {
            onFragment { fragment ->
                fragment.setTransparency()
                assertThat(fragment.appearancePreferences?.appearanceTransparency, equalTo(-1))

                fragment.setBackgroundColour()
                assertEquals("#FFF8FD89", fragment.appearancePreferences?.appearanceColour)

                fragment.setTextSize()
                assertThat(fragment.appearancePreferences?.appearanceTextSize, equalTo(16))

                assertTrue(fragment.appearancePreferences?.appearanceToolbarFirst == false)
                assertTrue(fragment.appearancePreferences?.appearanceToolbarPrevious == true)
                assertTrue(fragment.appearancePreferences?.appearanceToolbarReport == false)
                assertTrue(fragment.appearancePreferences?.appearanceToolbarFavourite == true)
                assertTrue(fragment.appearancePreferences?.appearanceToolbarShare == false)
                assertTrue(fragment.appearancePreferences?.appearanceToolbarRandom == true)
                assertTrue(fragment.appearancePreferences?.appearanceToolbarSequential == false)
            }
        }
    }

    @Test
    fun confirmChangesToPreferences() {
        with(launchFragment<AppearanceFragmentDouble>()) {
            onFragment { fragment ->
                fragment.appearancePreferences?.appearanceTransparency = 5
                fragment.setTransparency()
                assertEquals(5, fragment.appearancePreferences?.appearanceTransparency)

                fragment.appearancePreferences?.appearanceColour = "#FFFFFFFF"
                fragment.setBackgroundColour()
                assertEquals("#FFFFFFFF", fragment.appearancePreferences?.appearanceColour)

                fragment.appearancePreferences?.appearanceTextSize = 32
                fragment.setTextSize()
                assertEquals(32, fragment.appearancePreferences?.appearanceTextSize)

                fragment.fragmentAppearanceBinding?.toolbarSwitchFirst?.isChecked = true
                assertTrue(fragment.appearancePreferences?.appearanceToolbarFirst == true)

                fragment.fragmentAppearanceBinding?.toolbarSwitchPrevious?.isChecked = false
                assertTrue(fragment.appearancePreferences?.appearanceToolbarPrevious == false)

                fragment.fragmentAppearanceBinding?.toolbarSwitchReport?.isChecked = false
                assertTrue(fragment.appearancePreferences?.appearanceToolbarReport == false)

                fragment.fragmentAppearanceBinding?.toolbarSwitchToggleFavourite?.isChecked = false
                assertTrue(fragment.appearancePreferences?.appearanceToolbarFavourite == false)

                fragment.fragmentAppearanceBinding?.toolbarSwitchShare?.isChecked = false
                assertTrue(fragment.appearancePreferences?.appearanceToolbarShare == false)

                fragment.fragmentAppearanceBinding?.toolbarSwitchNextRandom?.isChecked = false
                assertTrue(fragment.appearancePreferences?.appearanceToolbarRandom == false)

                fragment.fragmentAppearanceBinding?.toolbarSwitchNextSequential?.isChecked = true
                assertTrue(fragment.appearancePreferences?.appearanceToolbarSequential == true)
            }
        }
    }

    @Test
    fun emptyDeletedPreferences() {
        with(launchFragment<AppearanceFragmentDouble>()) {
            onFragment { fragment ->
                fragment.appearancePreferences?.appearanceTransparency = 5
                fragment.setTransparency()
                assertEquals(5, fragment.appearancePreferences?.appearanceTransparency)

                AppearancePreferences.delete(getApplicationContext(), WidgetIdHelper.WIDGET_ID_02)
                assertEquals(5, fragment.appearancePreferences?.appearanceTransparency)

                AppearancePreferences.delete(getApplicationContext(), WidgetIdHelper.WIDGET_ID_01)
                assertEquals(-1, fragment.appearancePreferences?.appearanceTransparency)
            }
        }
    }

    @Test
    fun emptyDisabledPreferences() {
        with(launchFragment<AppearanceFragmentDouble>()) {
            onFragment { fragment ->
                fragment.appearancePreferences?.appearanceTransparency = 5
                fragment.setTransparency()
                assertEquals(5, fragment.appearancePreferences?.appearanceTransparency)

                AppearancePreferences.disable(getApplicationContext())
                assertEquals(-1, fragment.appearancePreferences?.appearanceTransparency)
            }
        }
    }
}
