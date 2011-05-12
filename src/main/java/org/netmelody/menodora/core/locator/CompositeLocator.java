package org.netmelody.menodora.core.locator;

import java.util.ArrayList;
import java.util.List;

public final class CompositeLocator implements Locator {

    private final Locator[] locators;

    public CompositeLocator(Locator... locators) {
        this.locators = locators;
    }
    
    @Override
    public List<String> locate() {
        final List<String> result = new ArrayList<String>();
        
        for (Locator locator : locators) {
            result.addAll(locator.locate());
        }
        
        return result;
    }

}
