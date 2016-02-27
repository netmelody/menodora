package org.netmelody.menodora.core;

import org.junit.runner.Description;

public final class JasmineSuiteDescriber {

    private final Context context;

    public JasmineSuiteDescriber(Context context) {
        this.context = context;
    }

    public Description getDescription() {
        final Description description = Description.createSuiteDescription(context.getSuiteClass());

        for (String testResource : context.allTestResources()) {
            description.addChild(new JasmineSpecFileDescriber(testResource, context.getSuiteClass()).getDescription());
        }

        return description;
    }

}
