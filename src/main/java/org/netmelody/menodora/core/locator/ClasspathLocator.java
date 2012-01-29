package org.netmelody.menodora.core.locator;

import java.util.List;
import java.util.regex.Pattern;

import org.reflections.Reflections;

import com.google.common.collect.ImmutableList;

public final class ClasspathLocator implements Locator {

    private final Reflections reflections;
    private final Pattern pattern;

    public ClasspathLocator(Reflections reflections, Pattern pattern) {
        this.reflections = reflections;
        this.pattern = pattern;
    }
    
    @Override
    public List<String> locate() {
        //dummyjstests/PlayerSpec.js
        reflections.getResources(Pattern.compile(".*Spec.js"));
        return ImmutableList.copyOf(reflections.getResources(pattern));
    }
}
