package org.netmelody.menodora.core;

import junit.framework.AssertionFailedError;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;

public final class JasmineJUnitReporter implements JavaScriptReporter {

    private final RunNotifier notifier;
    private final Class<?> suiteClass;

    public JasmineJUnitReporter(Class<?> suiteClass, RunNotifier notifier) {
        this.suiteClass = suiteClass;
        this.notifier = notifier;
    }

    @Override
    public void reportRunnerResults(NativeObject runner) {
//        System.out.println("Jasmine Runner Finished.");
    }

    @Override
    public void reportRunnerStarting(NativeObject runner) {
//        System.out.println("Jasmine Runner Started.");
    }

    @Override
    public void reportSpecResults(NativeObject spec) {
        String specDesc = (String)NativeObject.getProperty(spec, "description");
//        NativeObject suite = (NativeObject)NativeObject.getProperty(spec, "suite");
//        String suiteDesc = (String)NativeObject.getProperty(suite, "description");
//        NativeObject parentSuite = (NativeObject)NativeObject.getProperty(suite, "parentSuite");
//        String fullName = (String)NativeObject.callMethod(spec, "getFullName", new Object[0]);

        NativeObject results = (NativeObject)NativeObject.callMethod(spec, "results", new Object[0]);
        Boolean passed = (Boolean)NativeObject.callMethod(results, "passed", new Object[0]);
//        System.out.println(fullName + (passed ? " Passed." : " Failed."));

        if (passed) {
            notifier.fireTestFinished(Description.createTestDescription(suiteClass, specDesc));
            return;
        }

        String message = "";
        NativeArray items = (NativeArray)NativeObject.callMethod(results, "getItems", new Object[0]);
        for (Object item : items) {
            Boolean good = (Boolean)NativeObject.callMethod((NativeObject)item, "passed", new Object[0]);
            if (!good) {
                message = (String)NativeObject.getProperty((NativeObject)item, "message");
                break;
            }
        }
        notifier.fireTestFailure(new Failure(Description.createTestDescription(suiteClass, specDesc), new AssertionFailedError(message)));
    }

    @Override
    public void reportSpecStarting(NativeObject spec) {
        String specDesc = (String)NativeObject.getProperty(spec, "description");
//        NativeObject suite = (NativeObject)NativeObject.getProperty(spec, "suite");
//        String suiteDesc = (String)NativeObject.getProperty(suite, "description");
//        String fullName = (String)NativeObject.callMethod(spec, "getFullName", new Object[0]);
//        System.out.println(fullName + "...");

        notifier.fireTestStarted(Description.createTestDescription(suiteClass, specDesc));
    }

    @Override
    public void reportSuiteResults(NativeObject suite) {
//        Object suiteDesc = NativeObject.getProperty(suite, "description");
//        NativeObject results = (NativeObject)NativeObject.callMethod(suite, "results", new Object[0]);
//        Double passedCount = (Double)NativeObject.getProperty(results, "passedCount");
//        Double totalCount = (Double)NativeObject.getProperty(results, "totalCount");
//        System.out.println(suiteDesc + ": " + passedCount.intValue() + " of " + totalCount.intValue() + " passed.");
    }
}
