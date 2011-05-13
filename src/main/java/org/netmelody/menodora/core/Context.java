package org.netmelody.menodora.core;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.netmelody.menodora.JasmineJavascriptContext;
import org.netmelody.menodora.core.locator.CompositeLocator;
import org.netmelody.menodora.core.locator.FileSystemLocator;
import org.netmelody.menodora.core.locator.Locator;

public final class Context {

    @JasmineJavascriptContext private static final class DEFAULT { }
    
    private final Class<?> suiteClass;

    public Context(Class<?> suiteClass) {
        this.suiteClass = suiteClass;
    }
    
    public Class<?> getSuiteClass() {
        return this.suiteClass;
    }
    
    private String[] getJasmineSpecFileMatchers() {
        JasmineJavascriptContext annotation = this.suiteClass.getAnnotation(JasmineJavascriptContext.class);
        if (annotation == null) {
            annotation = DEFAULT.class.getAnnotation(JasmineJavascriptContext.class);
        }
        return annotation.jasmineSpecs();
    }
    
    private String[] getJasmineHelperFileMatchers() {
        JasmineJavascriptContext annotation = this.suiteClass.getAnnotation(JasmineJavascriptContext.class);
        if (annotation == null) {
            annotation = DEFAULT.class.getAnnotation(JasmineJavascriptContext.class);
        }
        return annotation.jasmineHelpers();
    }
    
    private String[] getSourceFileMatchers() {
        JasmineJavascriptContext annotation = this.suiteClass.getAnnotation(JasmineJavascriptContext.class);
        if (annotation == null) {
            annotation = DEFAULT.class.getAnnotation(JasmineJavascriptContext.class);
        }
        return annotation.source();
    }
    
    public boolean withSimulatedDom() {
        JasmineJavascriptContext annotation = this.suiteClass.getAnnotation(JasmineJavascriptContext.class);
        if (annotation == null) {
            annotation = DEFAULT.class.getAnnotation(JasmineJavascriptContext.class);
        }
        return annotation.withSimulatedDom();
    }
    
    public File root() {
        try {
            final ClassLoader classLoader = suiteClass.getClassLoader();
            final String classResource = suiteClass.getName().replaceAll("\\.", "/") + ".class";
            final Enumeration<URL> urls = classLoader.getResources(classResource);
            final File root = new File(urls.nextElement().toString().replace("file:", "").replace(classResource, ""));
            return root;
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
    
    public Locator jasmineSpecLocator() {
        return fileLocatorFor(getJasmineSpecFileMatchers());
    }
    
    public Locator javascriptLocator() {
        return new CompositeLocator(new Locator[] {fileLocatorFor(getSourceFileMatchers()),
                                                   fileLocatorFor(getJasmineHelperFileMatchers()),
                                                   jasmineSpecLocator()});
    }
    
    private Locator fileLocatorFor(String... patterns) {
        final List<FileSystemLocator> locators = new ArrayList<FileSystemLocator>();
        for (String pattern : patterns) {
            locators.add(new FileSystemLocator(root(), pattern));
        }
        return new CompositeLocator(locators.toArray(new FileSystemLocator[locators.size()]));
    }
}
