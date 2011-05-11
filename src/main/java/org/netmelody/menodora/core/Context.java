package org.netmelody.menodora.core;

import java.io.File;
import java.net.URL;
import java.util.Enumeration;

import org.netmelody.menodora.JasmineJavascriptContext;


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
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            final String classResource = suiteClass.getName().replaceAll("\\.", "/")+".class";
            Enumeration<URL> urls = cl.getResources(classResource);
            final File root = new File(urls.nextElement().toString().replace("file:", "").replace(classResource, ""));
            return root;
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
