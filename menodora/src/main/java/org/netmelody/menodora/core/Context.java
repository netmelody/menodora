package org.netmelody.menodora.core;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

import org.netmelody.menodora.JasmineJavascriptContext;
import org.netmelody.menodora.core.locator.ClasspathLocator;
import org.netmelody.menodora.core.locator.CompositeLocator;
import org.netmelody.menodora.core.locator.Locator;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

public final class Context {

    @JasmineJavascriptContext private static final class DEFAULT { }
    
    private final Class<?> suiteClass;
    private final Reflections reflections;

    public Context(Class<?> suiteClass) {
        this.suiteClass = suiteClass;
        this.reflections = new Reflections(new ConfigurationBuilder()
                                   .filterInputsBy(new FilterBuilder.Exclude(".*\\.class"))
                                   .setUrls(ClasspathHelper.forClassLoader(suiteClass.getClassLoader()))
                                   .setScanners(new ResourcesScanner()));
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
    
    public Iterable<String> reflectionsExample() {
        return reflections.getResources(Pattern.compile(".*\\.js"));
    }
    
    public File root() {
        try {
            final ClassLoader classLoader = suiteClass.getClassLoader();
            final String classResource = suiteClass.getName().replaceAll("\\.", "/") + ".class";
            final Enumeration<URL> urls = classLoader.getResources(classResource);
            //String decode = URLDecoder.decode(urls.nextElement().getFile(), "UTF-8");
            final String rootPath = urls.nextElement().toString().replace("file:", "").replace(classResource, "");
            final File root = new File(rootPath);
            if (!root.isDirectory()) {
//                throw new IllegalStateException(String.format("Unrecognised root path, %s", rootPath));
            }
            return root;
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
    
    public Locator jasmineSpecLocator() {
        return classpathLocatorFor(getJasmineSpecFileMatchers());
    }
    
    public Locator javascriptLocator() {
        return new CompositeLocator(new Locator[] {classpathLocatorFor(getSourceFileMatchers()),
                                                   classpathLocatorFor(getJasmineHelperFileMatchers()),
                                                   jasmineSpecLocator()});
    }
    
    private Locator classpathLocatorFor(String... patterns) {
        final List<Locator> locators = new ArrayList<Locator>();
        for (String pattern : patterns) {
            locators.add(new ClasspathLocator(reflections, Pattern.compile(".*" + pattern.replaceAll("\\*", ".*"))));
        }
        return new CompositeLocator(locators.toArray(new Locator[locators.size()]));
    }
}