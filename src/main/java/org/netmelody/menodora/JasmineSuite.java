package org.netmelody.menodora;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

public class JasmineSuite extends ParentRunner<JasmineSpecRunner> {
    
    private final List<JasmineSpecRunner> runners = new ArrayList<JasmineSpecRunner>();

    public JasmineSuite(Class<?> klass, RunnerBuilder builder) throws InitializationError {
        this(klass);
    }

    protected JasmineSuite(Class<?> klass) throws InitializationError {
        super(klass);
        discoverJasmineSpecs(klass);
    }
    
    @Override
    protected List<JasmineSpecRunner> getChildren() {
        return runners;
    }
    
    @Override
    protected Description describeChild(JasmineSpecRunner child) {
        return child.getDescription();
    }

    @Override
    protected void runChild(JasmineSpecRunner runner, final RunNotifier notifier) {
        runner.run(notifier);
    }
    
    private void discoverJasmineSpecs(Class<?> klass) {
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            final String classResource = klass.getName().replaceAll("\\.", "/")+".class";
            Enumeration<URL> urls = cl.getResources(classResource);
            final File root = new File(urls.nextElement().toString().replace("file:", "").replace(classResource, ""));

            Collection<File> specFiles = FileUtils.listFiles(root, new String[] {"js"}, true);
            for (File file : specFiles) {
                System.out.println(file);
            }
            
            this.runners.add(new JasmineSpecRunner(klass, specFiles));
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
