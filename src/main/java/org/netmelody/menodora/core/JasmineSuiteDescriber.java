package org.netmelody.menodora.core;

import java.util.List;

import org.junit.runner.Description;

public final class JasmineSuiteDescriber {

    private final Context context;

    public JasmineSuiteDescriber(Context context) {
        this.context = context;
    }
    
    public Description getDescription() {
        final Description description = Description.createSuiteDescription(context.getSuiteClass());
        
        final List<String> specs = context.jasmineSpecLocator().locate();
        for (String spec : specs) {
            description.addChild(new JasmineSpecFileDescriber(spec, context.getSuiteClass()).getDescription());
        }
        
        return description;
    }
}
