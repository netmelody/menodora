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

        final List<String> testResources = context.allTestResources();
        for (String testResource : testResources) {
            description.addChild(new JasmineSpecFileDescriber(testResource, context.getSuiteClass()).getDescription());
        }

        return description;
    }

}
