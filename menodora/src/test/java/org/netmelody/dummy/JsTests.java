package org.netmelody.dummy;

import org.junit.runner.RunWith;
import org.netmelody.menodora.JavaScriptContext;
import org.netmelody.menodora.JasmineSuite;

@RunWith(JasmineSuite.class)
@JavaScriptContext(
        source="org/netmelody/dummy/main/*.js",
        helpers ="org/netmelody/dummy/test/*.js",
        tests ="org/netmelody/dummy/test/*Spec.js",
        withSimulatedDom=false)
public class JsTests {

}
