package org.netmelody.menodora.core.test;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.netmelody.menodora.core.Context;

public final class ContextTest {

    @Test public void
    retrievesTheRootFromALooseClassFile() {
        System.out.println(new Context(org.netmelody.dummy.JsTests.class).root());
    }
    
    @Test public void
    retrievesTheRootFromAClassFileInAJar() throws Exception {
        final Class<?> suiteClass = Class.forName("org.netmelody.dummy2.JsTests", true, createClassLoader());
        final Context context = new Context(suiteClass);
        
        System.out.println(context.root());
    }
    
    private static ClassLoader createClassLoader() {
        try {
            final List<URL> urls = new ArrayList<URL>();
            urls.add(ContextTest.class.getResource("/dummyproj2.jar").toURI().toURL());
            return new URLClassLoader(urls.toArray(new URL[urls.size()]));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
