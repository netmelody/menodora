package org.netmelody.menodora;

import org.junit.runner.RunWith;
import org.netmelody.menodora.JasmineSuite.JasmineHelpers;
import org.netmelody.menodora.JasmineSuite.JasmineSpecs;
import org.netmelody.menodora.JasmineSuite.Source;

@RunWith(JasmineSuite.class)
@Source("dummysrc/*.js")
@JasmineHelpers("dummytests/*.js")
@JasmineSpecs("dummytests/*Spec.js")
public class JsTests {

}
