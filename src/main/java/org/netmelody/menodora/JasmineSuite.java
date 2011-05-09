package org.netmelody.menodora;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

public final class JasmineSuite extends Runner {
    
    private final Class<?> klass;
    private final List<JasmineSpecFileDescriber> specs = new ArrayList<JasmineSpecFileDescriber>();
    private final List<File> scriptFiles = new ArrayList<File>();

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Inherited
    public @interface JasmineSpecs {
        public String[] value();
    }
    
    private static String[] getJasmineSpecFileMatchers(Class<?> klass) throws InitializationError {
        JasmineSpecs annotation = klass.getAnnotation(JasmineSpecs.class);
        if (annotation == null) {
            return new String[] {"*Spec.js"};
        }
        return annotation.value();
    }
    
    public JasmineSuite(Class<?> klass, RunnerBuilder builder) throws InitializationError {
        this.klass = klass;
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            final String classResource = klass.getName().replaceAll("\\.", "/")+".class";
            Enumeration<URL> urls = cl.getResources(classResource);
            final File root = new File(urls.nextElement().toString().replace("file:", "").replace(classResource, ""));

            final Collection<File> specFiles = new ArrayList<File>();
            
            final String[] matchers = getJasmineSpecFileMatchers(klass);
            for (String matcher : matchers) {
                int separatorIndex = matcher.lastIndexOf(File.separatorChar);
                if (-1 == separatorIndex) {
                    specFiles.addAll(FileUtils.listFiles(root, new WildcardFileFilter(matcher), TrueFileFilter.INSTANCE));
                }
                
                specFiles.addAll(FileUtils.listFiles(root,
                                                     new WildcardFileFilter(matcher.substring(separatorIndex + 1)),
                                                     new NameFileFilter(matcher.substring(0, separatorIndex))));
            }
            
            for (File file : specFiles) {
                if (isSpecFile(file)) {
                    this.specs.add(new JasmineSpecFileDescriber(file));
                }
                this.scriptFiles.add(file);
            }
            
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Description getDescription() {
        final Description description = Description.createSuiteDescription(klass);
        
        for (JasmineSpecFileDescriber spec : this.specs) {
            description.addChild(spec.getDescription());
        }
        
        return description;
    }

    @Override
    public void run(RunNotifier notifier) {
        final JasmineExecutionEnvironment environment = new JasmineExecutionEnvironment();
        environment.executeJUnitTests(scriptFiles, notifier);
    }
    
    private boolean isSpecFile(File specFile) {
        try {
            return FileUtils.readFileToString(specFile).startsWith("describe");
        } catch (IOException e) {
            return false;
        }
    }
}
