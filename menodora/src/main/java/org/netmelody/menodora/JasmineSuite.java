package org.netmelody.menodora;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.netmelody.menodora.core.Context;
import org.netmelody.menodora.core.ExecutionEnvironmentPreparation;
import org.netmelody.menodora.core.JasmineSuiteDescriber;
import org.netmelody.menodora.core.JavaScriptEnvironment;
import org.netmelody.menodora.jasmine.JasmineTestRunner;

public final class JasmineSuite extends Runner {

    private final Context context;
    private final JasmineSuiteDescriber suiteDescriber;

    public JasmineSuite(Class<?> suiteClass) {
        try {
            context = new Context(suiteClass);
            suiteDescriber = new JasmineSuiteDescriber(context);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Description getDescription() {
        return suiteDescriber.getDescription();
    }

    @Override
    public void run(RunNotifier notifier) {
        JavaScriptEnvironment environment = new JavaScriptEnvironment();
        new ExecutionEnvironmentPreparation(context.withSimulatedDom()).prepare(environment);
        new JasmineTestRunner(context, environment).executeTests(notifier);
    }
}
