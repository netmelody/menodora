package org.netmelody.menodora.core.locator;

import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;
import org.reflections.Reflections;

import static com.google.common.base.Predicates.contains;
import static com.google.common.collect.Collections2.filter;

public final class ClasspathLocator implements Locator {

    private final Reflections reflections;
    private final Pattern pattern;

    public ClasspathLocator(Reflections reflections, Pattern pattern) {
        this.reflections = reflections;
        this.pattern = pattern;
    }

    @Override
    public Collection<String> locate() {
        final Set<String> resources = reflections.getResources(Pattern.compile(".*\\.js"));
        return filter(resources, contains(pattern));
    }
}
