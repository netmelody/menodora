package org.netmelody.menodora.jasmine;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.junit.runner.notification.RunNotifier;
import org.netmelody.menodora.core.Context;
import org.netmelody.menodora.core.JavaScriptEnvironment;
import org.netmelody.menodora.core.JavaScriptTestRunner;

public class JasmineTestRunner implements JavaScriptTestRunner {
    private final Context context;
    private final JavaScriptEnvironment environment;

    public JasmineTestRunner(Context context, JavaScriptEnvironment environment) {
        this.context = context;
        this.environment = environment;
    }

    @Override
    public void executeTests(RunNotifier notifier) {
        for (String resource : context.allJavaScriptResources()) {
            environment.loadResource(resource);
        }

        environment.setGlobal("jUnitReporter", new JasmineJUnitReporter(context.getSuiteClass(), notifier));
        environment.eval("jasmine.getEnv().addReporter(jUnitReporter);");

        try {
            if (context.withSimulatedDom()) {
                File loader = File.createTempFile("jasmine", ".html");
                FileUtils.writeStringToFile(loader, "<html><script type='text/javascript'>jasmine.getEnv().execute();</script></html>");
                final String uri = loader.toURI().toString().replaceFirst("^file:/([^/])", "file:///$1");
                environment.eval(String.format("window.location = '%s';", uri));
            }
            else {
                environment.eval("jasmine.getEnv().execute();");
                environment.eval("__timer__.done();");
            }
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
