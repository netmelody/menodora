package org.netmelody.menodora.jasmine;

import org.junit.runner.Describable;
import org.junit.runner.Description;
import org.netmelody.menodora.core.Context;

public final class JasmineSuiteDescriber implements Describable {

    private final Context context;

    public JasmineSuiteDescriber(Context context) {
        this.context = context;
    }

    @Override
    public Description getDescription() {
        final Description description = Description.createSuiteDescription(context.getSuiteClass());

        for (String testResource : context.allTestResources()) {
            description.addChild(new JasmineSpecFileDescriber(testResource, context.getSuiteClass()).getDescription());
        }

        return description;
    }

}
