package org.netmelody.dummy;

import org.junit.runner.RunWith;
import org.netmelody.menodora.JasmineJavascriptContext;
import org.netmelody.menodora.JasmineSuite;

@RunWith(JasmineSuite.class)
@JasmineJavascriptContext(
        source="org/netmelody/dummy/main/*.js",
        jasmineHelpers="org/netmelody/dummy/test/*.js",
        jasmineSpecs="org/netmelody/dummy/test/*Spec.js",
        withSimulatedDom=false)
public class JsTests {

}
