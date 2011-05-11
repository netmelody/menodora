package dummyproj.src.test.java;

import org.junit.runner.RunWith;
import org.netmelody.menodora.JasmineSuite;
import org.netmelody.menodora.JasmineSuite.JasmineHelpers;
import org.netmelody.menodora.JasmineSuite.JasmineSpecs;
import org.netmelody.menodora.JasmineSuite.Source;

@RunWith(JasmineSuite.class)
@Source("src/main/js/*.js")
@JasmineHelpers("src/test/js/*.js")
@JasmineSpecs("src/test/js/*Spec.js")
public class JsTests {

}
