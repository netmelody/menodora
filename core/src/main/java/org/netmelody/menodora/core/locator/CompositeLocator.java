package org.netmelody.menodora.core.locator;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class CompositeLocator implements Locator {

    private final List<Locator> locators;

    public CompositeLocator(Locator... locators) {
        this(Arrays.asList(locators));
    }

    public CompositeLocator(List<Locator> locators) {
        this.locators = locators;
    }

    @Override
    public Collection<String> locate() {
        Set<String> results = new LinkedHashSet<String>();
        for (Locator locator : locators) {
            results.addAll(locator.locate());
        }
        return results;
    }
}
