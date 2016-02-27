package org.netmelody.menodora.core;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.netmelody.menodora.JavaScriptContext;
import org.netmelody.menodora.core.locator.ClasspathLocator;
import org.netmelody.menodora.core.locator.CompositeLocator;
import org.netmelody.menodora.core.locator.Locator;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

public final class Context {
    private final Class<?> suiteClass;

    private final Reflections reflections;
    public Context(Class<?> suiteClass) {
        this.suiteClass = suiteClass;
        this.reflections = new Reflections(new ConfigurationBuilder()
                                   .filterInputsBy(new FilterBuilder.Exclude(".*\\.class"))
                                   .setUrls(ClasspathHelper.forClassLoader(suiteClass.getClassLoader()))
                                   .setScanners(new ResourcesScanner()));
    }

    public JavaScriptTestRunner constructRunner(JavaScriptEnvironment environment) {
        try {
            return annotation().runner().getConstructor(Context.class, JavaScriptEnvironment.class)
                    .newInstance(this, environment);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Class<?> getSuiteClass() {
        return this.suiteClass;
    }

    public boolean withSimulatedDom() {
        return annotation().withSimulatedDom();
    }

    public Iterable<String> reflectionsExample() {
        return reflections.getResources(Pattern.compile(".*\\.js"));
    }

    public Iterable<String> allJavaScriptResources() {
        return javascriptLocator().locate();
    }

    public Iterable<String> allTestResources() {
        return testsLocator().locate();
    }

    private Locator javascriptLocator() {
        return new CompositeLocator(
                classpathLocatorFor(annotation().source()),
                classpathLocatorFor(annotation().helpers()),
                testsLocator());
    }

    private Locator testsLocator() {
        return classpathLocatorFor(annotation().tests());
    }

    private Locator classpathLocatorFor(String... patterns) {
        final List<Locator> locators = new ArrayList<Locator>();
        for (String pattern : patterns) {
            locators.add(new ClasspathLocator(reflections, Pattern.compile(".*" + pattern.replaceAll("\\*", ".*"))));
        }
        return new CompositeLocator(locators);
    }

    private JavaScriptContext annotation() {
        return suiteClass.getAnnotation(JavaScriptContext.class);
    }
}
