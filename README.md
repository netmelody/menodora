Menodora
========

Menodora is a JUnit test runner for Jasmine JavaScript tests, which you can [download][] and use.

[download]: https://github.com/netmelody/menodora/downloads

#### Usage

Start by creating a new Java class in your test directory and applying the Menodora annotations:

```Java
@RunWith(MenodoraRunner.class)
@JavaScriptContext(
        style = JasmineStyle.class,
        source = "src/main/js/*.js",
        helpers = "src/test/js/*.js",
        tests = "src/test/js/*Spec.js",
        withSimulatedDom = false)
public class JsTests { }
```

Now you can run this class like you would any other JUnit test, and see the test output in your IDE or build tool.
