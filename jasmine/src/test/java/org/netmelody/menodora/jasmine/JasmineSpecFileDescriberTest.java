package org.netmelody.menodora.jasmine;

import org.junit.Test;
import org.junit.runner.Description;
import org.netmelody.dummy.JsTests;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.netmelody.menodora.testing.DescriptionMatcher.aDescription;

public class JasmineSpecFileDescriberTest {

    @Test public void
    loadsASpec() {
        JasmineSpecFileDescriber describer = new JasmineSpecFileDescriber(
                "org/netmelody/dummy/test/PlayerSpec.js", JsTests.class);

        Description description = describer.getDescription();

        assertThat(description,
                is(aDescription("org/netmelody/dummy/test/PlayerSpec.js").withChildren(
                        aDescription("Player").withChildren(
                                aDescription("should be able to play a Song", JsTests.class),
                                aDescription("when song has been paused").withChildren(
                                        aDescription("should indicate that the song is currently paused", JsTests.class),
                                        aDescription("should be possible to resume", JsTests.class)),
                                aDescription("tells the current song if the user has made it a favorite", JsTests.class),
                                aDescription("#resume").withChildren(
                                        aDescription("should throw an exception if song is already playing", JsTests.class))))));
    }
}
