package org.netmelody.menodora;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
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
import org.netmelody.menodora.core.Context;
import org.netmelody.menodora.core.JasmineExecutionEnvironment;
import org.netmelody.menodora.core.JasmineJunitReporter;
import org.netmelody.menodora.core.JasmineSpecFileDescriber;

public final class JasmineSuite extends Runner {
    
    private final Class<?> suiteClass;
    private final List<JasmineSpecFileDescriber> specs = new ArrayList<JasmineSpecFileDescriber>();
    private final List<File> scriptFiles = new ArrayList<File>();
    
    private static Collection<File> getFilesFrom(String[] matchers, File root) {
        final Collection<File> files = new ArrayList<File>();
        
        for (String matcher : matchers) {
            int separatorIndex = matcher.lastIndexOf("/");
            if (-1 == separatorIndex) {
                files.addAll(FileUtils.listFiles(root, new WildcardFileFilter(matcher), TrueFileFilter.INSTANCE));
            }
            
            files.addAll(FileUtils.listFiles(root,
                                             new WildcardFileFilter(matcher.substring(separatorIndex + 1)),
                                             new NameFileFilter(matcher.substring(0, separatorIndex))));
        }
        
        return files;
    }
    
    public JasmineSuite(Class<?> suiteClass, RunnerBuilder builder) throws InitializationError {
        this.suiteClass = suiteClass;
        
        try {
            final Context context = new Context(this.suiteClass);
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            final String classResource = suiteClass.getName().replaceAll("\\.", "/")+".class";
            Enumeration<URL> urls = cl.getResources(classResource);
            final File root = new File(urls.nextElement().toString().replace("file:", "").replace(classResource, ""));

            final Collection<File> specFiles = getFilesFrom(context.getJasmineSpecFileMatchers(), root);
            
            for (File spec : specFiles) {
                this.specs.add(new JasmineSpecFileDescriber(spec, suiteClass));
            }
            
            final LinkedHashSet<File> files = new LinkedHashSet<File>();
            files.addAll(specFiles);
            files.addAll(getFilesFrom(context.getJasmineHelperFileMatchers(), root));
            files.addAll(getFilesFrom(context.getSourceFileMatchers(), root));
            
            this.scriptFiles.addAll(files);
            Collections.reverse(this.scriptFiles);
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Description getDescription() {
        final Description description = Description.createSuiteDescription(suiteClass);
        
        for (JasmineSpecFileDescriber spec : this.specs) {
            description.addChild(spec.getDescription());
        }
        
        return description;
    }

    @Override
    public void run(RunNotifier notifier) {
        final JasmineExecutionEnvironment environment = new JasmineExecutionEnvironment();
        environment.executeJasmineTests(scriptFiles, new JasmineJunitReporter(suiteClass, notifier));
    }
}
