package org.netmelody.menodora;


public final class Context {

    @JasmineJavascriptContext private static final class DEFAULT { }
    
    private final Class<?> suiteClass;

    public Context(Class<?> suiteClass) {
        this.suiteClass = suiteClass;
    }
    
    public String[] getJasmineSpecFileMatchers() {
        JasmineJavascriptContext annotation = suiteClass.getAnnotation(JasmineJavascriptContext.class);
        if (annotation == null) {
            annotation = DEFAULT.class.getAnnotation(JasmineJavascriptContext.class);
        }
        return annotation.jasmineSpecs();
    }
    
    public String[] getJasmineHelperFileMatchers() {
        JasmineJavascriptContext annotation = suiteClass.getAnnotation(JasmineJavascriptContext.class);
        if (annotation == null) {
            annotation = DEFAULT.class.getAnnotation(JasmineJavascriptContext.class);
        }
        return annotation.jasmineHelpers();
    }
    
    public String[] getSourceFileMatchers() {
        JasmineJavascriptContext annotation = suiteClass.getAnnotation(JasmineJavascriptContext.class);
        if (annotation == null) {
            annotation = DEFAULT.class.getAnnotation(JasmineJavascriptContext.class);
        }
        return annotation.source();
    }
}
