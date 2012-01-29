package org.netmelody.menodora.core.test;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.netmelody.menodora.core.Context;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;

public final class ContextTest {

    @Test public void
    retrievesTheRootFromALooseClassFile() {
        assertThat(new Context(org.netmelody.dummy.JsTests.class).root().getPath(), is(not("")));
    }
    
    @Test public void
    retrievesTheRootFromAClassFileInAJar() throws Exception {
        final Class<?> suiteClass = Class.forName("org.netmelody.dummy2.JsTests", true, createClassLoader());
        final Context context = new Context(suiteClass);
        
        final String path = context.root().getPath();
        assertThat(path, startsWith("jar:"));
        assertThat(path, endsWith("dummyproj2.jar!"));
    }

    @Test public void
    canScanInAJar() throws Exception {
        final Class<?> suiteClass = Class.forName("org.netmelody.dummy2.JsTests", true, createClassLoader());
        final Context context = new Context(suiteClass);
        
        Iterable<String> locate = context.reflectionsExample();
        assertThat(locate, Matchers.containsInAnyOrder("jasmine-1.0.2/jasmine.js",
                                                       "env.js-1.2/env.rhino.1.2.js",
                                                       "menodora-js/fake.scheduler.js",
                                                       "dummyjs/Song.js",
                                                       "dummyjs/Player.js",
                                                       "dummyjstests/SpecHelper.js",
                                                       "dummyjstests/PlayerSpec.js"));
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
