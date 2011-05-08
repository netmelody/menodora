package org.netmelody.menodora;

import java.io.File;
import java.util.Collection;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

public class JasmineSpecRunner extends Runner {

    private final Class<?> klass;
    private final Collection<File> specFiles;

    public JasmineSpecRunner(Class<?> klass, Collection<File> specFiles) {
        this.klass = klass;
        this.specFiles = specFiles;
    }

    @Override
    public Description getDescription() {
        final Description description = Description.createSuiteDescription(this.klass.getName(),
                                                                           this.klass.getAnnotations());
        
        for (File specFile : specFiles) {
            description.addChild(Description.createSuiteDescription(specFile.getPath()));
        }
        return description;
    }

    @Override
    public void run(RunNotifier notifier) {
        final JasmineExecutionEnvironment environment = new JasmineExecutionEnvironment();
        environment.executeJUnitTests(specFiles, notifier);
    }
}
