package org.netmelody.menodora.core.test;

import org.junit.Test;
import org.junit.runner.Description;
import org.netmelody.dummy.JsTests;
import org.netmelody.menodora.core.JasmineSpecFileDescriber;

public class JasmineSpecFileDescriberTest {

    @Test public void
    loadsASpec() {
        JasmineSpecFileDescriber describer = new JasmineSpecFileDescriber(
                "/org/netmelody/dummy/test/PlayerSpec.js", JsTests.class);

        Description description = describer.getDescription();

        out(description, "");
    }

    private void out(Description description, String prefix) {
        System.out.println(prefix + description.toString());
        for (Description child : description.getChildren()) {
            out(child, prefix + "=");
        }
    }
}
