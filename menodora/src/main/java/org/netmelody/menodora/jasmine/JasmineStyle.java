package org.netmelody.menodora.jasmine;

import org.junit.runner.Describable;
import org.netmelody.menodora.core.Context;
import org.netmelody.menodora.core.JasmineSuiteDescriber;
import org.netmelody.menodora.core.JavaScriptEnvironment;
import org.netmelody.menodora.core.JavaScriptTestRunner;
import org.netmelody.menodora.core.TestStyle;

public class JasmineStyle implements TestStyle {

    @Override
    public Describable createDescriber(Context context) {
        return new JasmineSuiteDescriber(context);
    }

    @Override
    public JavaScriptTestRunner createRunner(Context context, JavaScriptEnvironment environment) {
        return new JasmineTestRunner(context, environment);
    }
}
