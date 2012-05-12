Menodora
=====
Menodora is a JUnit test runner for Jasmine JavaScript tests, which you can [download] and use.

#### Usage

Start by creating a new Java class in your test directory and applying the Menodora annotations:
```Java
@RunWith(JasmineSuite.class)
@JasmineJavascriptContext(
        source="source/main/js/*.js",
        jasmineHelpers="src/test/js/*Helper.js",
        jasmineSpecs="src/test/js/*Spec.js",
        withSimulatedDom=false)
public class JsTests { }
```
Now you can run this class like you would any other JUnit test.

[download]: https://github.com/netmelody/panto/downloads
