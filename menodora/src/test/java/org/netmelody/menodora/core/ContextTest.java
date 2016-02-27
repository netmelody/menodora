package org.netmelody.menodora.core;

import java.net.URL;
import java.net.URLClassLoader;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;

public final class ContextTest {
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

    private static ClassLoader createClassLoader() {
        return new URLClassLoader(new URL[] {
                ContextTest.class.getResource("/dummyproj2.jar")
        });
    }
}
