package org.netmelody.dummy;

import org.junit.runner.RunWith;
import org.netmelody.menodora.JasmineSuite;
import org.netmelody.menodora.JasmineSuite.JasmineJavascriptContext;

@RunWith(JasmineSuite.class)
@JasmineJavascriptContext(
        source="dummyjs/*.js",
        jasmineHelpers="dummyjstests/*.js",
        jasmineSpecs="dummyjstests/*Spec.js")
public class JsTests {

}