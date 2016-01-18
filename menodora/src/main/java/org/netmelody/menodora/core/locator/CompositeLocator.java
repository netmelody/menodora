package org.netmelody.menodora.core.locator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class CompositeLocator implements Locator {

    private final List<Locator> locators = new ArrayList<Locator>();

    public CompositeLocator(Locator... locators) {
        if (null == locators) {
            return;
        }
        
        this.locators.addAll(Arrays.asList(locators));
        Collections.reverse(this.locators);
    }
    
    @Override
    public List<String> locate() {
        final Set<String> targetSet = new LinkedHashSet<String>();
        
        for (Locator locator : locators) {
            final List<String> targets = locator.locate();
            Collections.reverse(targets);
            targetSet.addAll(targets);
        }
        
        List<String> result = new ArrayList<String>(targetSet);
        Collections.reverse(result);
        return result;
    }

}
