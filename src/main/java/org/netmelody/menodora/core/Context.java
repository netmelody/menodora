package org.netmelody.menodora.core;

import java.io.File;
import java.net.URL;
import java.util.Enumeration;

import org.netmelody.menodora.JasmineJavascriptContext;
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
    
    public String[] getJasmineSpecFileMatchers() {
        JasmineJavascriptContext annotation = this.suiteClass.getAnnotation(JasmineJavascriptContext.class);
        if (annotation == null) {
            annotation = DEFAULT.class.getAnnotation(JasmineJavascriptContext.class);
        }
        return annotation.jasmineSpecs();
    }
    
    public String[] getJasmineHelperFileMatchers() {
        JasmineJavascriptContext annotation = this.suiteClass.getAnnotation(JasmineJavascriptContext.class);
        if (annotation == null) {
            annotation = DEFAULT.class.getAnnotation(JasmineJavascriptContext.class);
        }
        return annotation.jasmineHelpers();
    }
    
    public String[] getSourceFileMatchers() {
        JasmineJavascriptContext annotation = this.suiteClass.getAnnotation(JasmineJavascriptContext.class);
        if (annotation == null) {
            annotation = DEFAULT.class.getAnnotation(JasmineJavascriptContext.class);
        }
        return annotation.source();
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
        final String[] patterns = getJasmineHelperFileMatchers();
        for (String pattern : patterns) {
            return new FileSystemLocator(root(), pattern);
        }
        return null;
    }
    
    public Locator javascriptLocator() {
        return null;
    }
}
