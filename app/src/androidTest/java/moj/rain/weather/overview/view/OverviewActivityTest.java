package moj.rain.weather.overview.view;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import moj.rain.R;
import moj.rain.weather.overview.view.OverviewActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

@RunWith(AndroidJUnit4.class)
public class OverviewActivityTest {

    @Rule
    public ActivityTestRule<OverviewActivity> activityRule = new ActivityTestRule<>(OverviewActivity.class);

    private OverviewActivity activity;
    private Instrumentation instrumentation;

    @Before
    public void setUp() throws Exception {
        activity = activityRule.getActivity();
        instrumentation = InstrumentationRegistry.getInstrumentation();
    }

    @Test
    // WHEN a weather network error is shown THEN show this error as a snackbar
    public void showWeatherNetworkError() throws Exception {
        whenAWeatherNetworkErrorIsShown();
        thenShowTheErrorAsASnackbar();
    }

    private void whenAWeatherNetworkErrorIsShown() {
        activity.showWeatherNetworkError();
    }

    private void thenShowTheErrorAsASnackbar() {
        onView(CoreMatchers.allOf(withId(android.support.design.R.id.snackbar_text),
                ViewMatchers.withText(R.string.network_error_message)))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }
}