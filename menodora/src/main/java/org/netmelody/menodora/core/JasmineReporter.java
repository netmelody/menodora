package org.netmelody.menodora.core;

import org.mozilla.javascript.NativeObject;

public interface JasmineReporter {

    void reportRunnerResults(NativeObject runner);

    void reportRunnerStarting(NativeObject runner);

    void reportSpecResults(NativeObject spec);

    void reportSpecStarting(NativeObject spec);

    void reportSuiteResults(NativeObject suite);

}
