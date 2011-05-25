package org.netmelody.menodora;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;
import org.netmelody.menodora.core.Context;
import org.netmelody.menodora.core.JasmineExecutionEnvironment;
import org.netmelody.menodora.core.JasmineJUnitReporter;
import org.netmelody.menodora.core.JasmineSuiteDescriber;

public final class JasmineSuite extends Runner {
    
    private final Context context;
    private final JasmineSuiteDescriber suiteDescriber;
    
    public JasmineSuite(Class<?> suiteClass, RunnerBuilder builder) throws InitializationError {
        try {
            context = new Context(suiteClass);
            suiteDescriber = new JasmineSuiteDescriber(context);
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Description getDescription() {
        return suiteDescriber.getDescription();
    }

    @Override
    public void run(RunNotifier notifier) {
        final JasmineExecutionEnvironment environment = new JasmineExecutionEnvironment(context.withSimulatedDom());
        environment.executeJasmineTests(context.javascriptLocator(), new JasmineJUnitReporter(context.getSuiteClass(), notifier));
    }
}
