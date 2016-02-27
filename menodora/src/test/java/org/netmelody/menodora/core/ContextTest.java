package org.netmelody.menodora.core;

import java.net.URL;
import java.net.URLClassLoader;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;

public final class ContextTest {

    private static final String JAVASCRIPT_JAR_FILE = "dummy-project.jar";
    private static final String TEST_CLASS_NAME = "org.netmelody.menodora.dummy.JsTests";

    @Test public void
    canScanInAJar() throws Exception {
        final Class<?> suiteClass = Class.forName(TEST_CLASS_NAME, true, createClassLoader());
        final Context context = new Context(suiteClass);

        final Iterable<String> locate = context.reflectionsExample();
        assertThat(locate, hasItems(
                "org/netmelody/menodora/dummy/main/One.js",
                "org/netmelody/menodora/dummy/main/Two.js",
                "org/netmelody/menodora/dummy/test/OneTest.js",
                "org/netmelody/menodora/dummy/test/TwoTest.js",
                "org/netmelody/menodora/dummy/test/TestHelper.js"));
    }

    private static ClassLoader createClassLoader() {
        return new URLClassLoader(new URL[] {
                ContextTest.class.getClassLoader().getResource(JAVASCRIPT_JAR_FILE)
        });
    }
}
