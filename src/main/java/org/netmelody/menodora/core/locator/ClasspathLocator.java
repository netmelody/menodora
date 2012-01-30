package org.netmelody.menodora.core.locator;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.reflections.Reflections;

import com.google.common.base.Function;

import static com.google.common.base.Predicates.contains;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;

public final class ClasspathLocator implements Locator {

    private static final Function<String, String> PREPEND_SLASH = new Function<String, String>() {
        @Override
        public String apply(String input) {
            return "/" + input;
        }
    };

    private final Reflections reflections;
    private final Pattern pattern;

    public ClasspathLocator(Reflections reflections, Pattern pattern) {
        this.reflections = reflections;
        this.pattern = pattern;
    }

    @Override
    public List<String> locate() {
        final Set<String> resources = reflections.getResources(Pattern.compile(".*\\.js"));
        return newArrayList(transform(filter(resources, contains(pattern)), PREPEND_SLASH));
    }
}