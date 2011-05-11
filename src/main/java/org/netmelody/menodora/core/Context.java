package org.netmelody.menodora.core;

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
}
