package org.netmelody.menodora.core.test;

import java.net.URL;
import java.net.URLClassLoader;

import org.junit.Test;
import org.netmelody.menodora.core.Context;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasItems;
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

        final Iterable<String> locate = context.reflectionsExample();
        assertThat(locate, hasItems(
                "dummyjs/Song.js",
                "dummyjs/Player.js",
                "dummyjstests/SpecHelper.js",
                "dummyjstests/PlayerSpec.js"));
    }

    private static ClassLoader createClassLoader() throws Exception {
        return new URLClassLoader(new URL[] {
                ContextTest.class.getResource("/dummyproj2.jar")
        });
    }
}
