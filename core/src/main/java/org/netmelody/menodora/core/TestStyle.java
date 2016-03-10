package org.netmelody.menodora.core;

import org.junit.runner.Describable;

public interface TestStyle {

    Describable createDescriber(Context context);

    JavaScriptTestRunner createRunner(Context context, JavaScriptEnvironment environment);
}
