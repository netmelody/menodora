package org.netmelody.dummy;

import org.junit.runner.RunWith;
import org.netmelody.menodora.JavaScriptContext;
import org.netmelody.menodora.MenodoraRunner;
import org.netmelody.menodora.jasmine.JasmineStyle;

@RunWith(MenodoraRunner.class)
@JavaScriptContext(
        style = JasmineStyle.class,
        source = "org/netmelody/dummy/main/*.js",
        helpers = "org/netmelody/dummy/test/*.js",
        tests = "org/netmelody/dummy/test/*Spec.js",
        withSimulatedDom = true)
public class JsTestsWithDom {

}
