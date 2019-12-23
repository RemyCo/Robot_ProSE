package fr.eseo.i2.prose.ea1.whereisrob.gui;


import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.whereisrob.whereisrob.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CartographerFragmentTest {

    private String ip = "172.23.2.151";
    private String port = "23456";
    private String validate = "VALIDER";

    @Rule
    public ActivityTestRule<GUIActivity> mActivityTestRule = new ActivityTestRule<>(GUIActivity.class);

    @Test
    public void cartographerFragmentTest() {

        /*
         * Remplissage du champ d'IP
         */
        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.ip_address), withText(ip),
                        childAtPosition(
                                allOf(withId(R.id.connexion),
                                        childAtPosition(
                                                withClassName(is("android.widget.FrameLayout")),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatEditText4.perform(closeSoftKeyboard());

        /*
         * Remplissage du champ de port
         */
        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.port_number), withText(port),
                        childAtPosition(
                                allOf(withId(R.id.connexion),
                                        childAtPosition(
                                                withClassName(is("android.widget.FrameLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatEditText6.perform(closeSoftKeyboard());

        pressBack();

        /*
         * Validation de la connexion
         */
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.connect), withText(validate),
                        childAtPosition(
                                allOf(withId(R.id.connexion),
                                        childAtPosition(
                                                withClassName(is("android.widget.FrameLayout")),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatButton.perform(click());

        /*
         * Check si le fragment Cartographer est présent
         */
        ViewInteraction view = onView(
                allOf(withId(R.id.cartographerview),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.frame_layout_cartographer),
                                        0),
                                0),
                        isDisplayed()));
        view.check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
