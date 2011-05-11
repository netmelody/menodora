package org.netmelody.dummy;

import org.junit.runner.RunWith;
import org.netmelody.menodora.JasmineSuite;
import org.netmelody.menodora.JasmineSuite.JasmineHelpers;
import org.netmelody.menodora.JasmineSuite.JasmineJavascriptContext;
import org.netmelody.menodora.JasmineSuite.JasmineSpecs;
import org.netmelody.menodora.JasmineSuite.Source;

@RunWith(JasmineSuite.class)
@Source("dummyjs/*.js")
@JasmineHelpers("dummyjstests/*.js")
@JasmineSpecs("dummyjstests/*Spec.js")
@JasmineJavascriptContext(source="")
public class JsTests {

}
