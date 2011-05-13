package org.netmelody.dummy;

import org.junit.runner.RunWith;
import org.netmelody.menodora.JasmineJavascriptContext;
import org.netmelody.menodora.JasmineSuite;

@RunWith(JasmineSuite.class)
@JasmineJavascriptContext(
        source="dummyjs/*.js",
        jasmineHelpers="dummyjstests/*.js",
        jasmineSpecs="dummyjstests/*Spec.js",
        withSimulatedDom=false)
public class JsTests {

}