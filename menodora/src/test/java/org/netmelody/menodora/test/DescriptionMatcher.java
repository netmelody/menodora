package org.netmelody.menodora.test;

import java.util.Collections;
import java.util.List;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.runner.Description;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.contains;

public class DescriptionMatcher extends TypeSafeDiagnosingMatcher<Description> {
    private static final List<Matcher<? super Description>> NO_CHILDREN = Collections.emptyList();

    private final String text;
    private final Class<?> suiteClass;
    private final List<Matcher<? super Description>> expectedChildren;

    public DescriptionMatcher(String text, Class<?> suiteClass, List<Matcher<? super Description>> children) {
        this.text = text;
        this.suiteClass = suiteClass;
        this.expectedChildren = children;
    }

    public static DescriptionMatcher aDescription(String text) {
        return new DescriptionMatcher(text, null,  NO_CHILDREN);
    }

    public static DescriptionMatcher aDescription(String text, Class<?> suiteClass) {
        return new DescriptionMatcher(text, suiteClass, NO_CHILDREN);
    }

    public DescriptionMatcher withChildren(Matcher<? super Description>... children) {
        return new DescriptionMatcher(text, suiteClass, asList(children));
    }

    @Override
    public void describeTo(org.hamcrest.Description description) {
        description.appendText("the description ").appendValue(expectedDisplayName());
        if (!expectedChildren.isEmpty()) {
            description.appendText(" with children ").appendList("[", ", ", "]", expectedChildren);
        }
    }

    @Override
    protected boolean matchesSafely(Description actual, org.hamcrest.Description mismatchDescription) {
        mismatchDescription.appendText("was ");
        describeValue(actual, mismatchDescription);
        List<Description> actualChildren = actual.getChildren();
        return expectedDisplayName().equals(actual.getDisplayName())
                && (expectedChildren.isEmpty() ? actualChildren.isEmpty() : contains(expectedChildren).matches(actualChildren));
    }

    private void describeValue(Description value, org.hamcrest.Description description) {
        description.appendText("the description ").appendValue(value.getDisplayName());
        List<Description> children = value.getChildren();
        if (!children.isEmpty()) {
            description.appendText(" with children [");
            boolean first = true;
            for (Description child : children) {
                if (first) {
                    first = false;
                } else {
                    description.appendText(", ");
                }
                describeValue(child, description);
            }
            description.appendText("]");
        }
    }

    private String expectedDisplayName() {
        return text + (suiteClass == null ? "" : ("(" + suiteClass.getName() + ")"));
    }
}
