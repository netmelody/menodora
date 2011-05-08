package org.netmelody.menodora;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.mozilla.javascript.NativeObject;

public class Reporter {

    private final RunNotifier notifier;

    public Reporter(RunNotifier notifier) {
        this.notifier = notifier;
    }

    public void reportRunnerResults(NativeObject runner) {
        System.out.println("Jasmine Runner Finished.");
    }
    
    public void reportRunnerStarting(NativeObject runner) {
        System.out.println("Jasmine Runner Started.");
    }
    
    public void reportSpecResults(NativeObject spec) {
        Object specDesc = NativeObject.getProperty(spec, "description");
        NativeObject suite = (NativeObject)NativeObject.getProperty(spec, "suite");
        Object suiteDesc = NativeObject.getProperty(suite, "description");
        
        NativeObject results = (NativeObject)NativeObject.callMethod(spec, "results", new Object[0]);
        Boolean passed = (Boolean)NativeObject.callMethod(results, "passed", new Object[0]);
        System.out.println(passed ? "Passed." : "Failed.");
        
        if (passed) {
            notifier.fireTestFinished(Description.createTestDescription(Object.class, suiteDesc + " : " + specDesc));
            return;
        }
        
        notifier.fireTestFailure(new Failure(Description.createTestDescription(Object.class, suiteDesc + " : " + specDesc), new Exception()));
    }
    
    public void reportSpecStarting(NativeObject spec) {
        Object specDesc = NativeObject.getProperty(spec, "description");
        NativeObject suite = (NativeObject)NativeObject.getProperty(spec, "suite");
        Object suiteDesc = NativeObject.getProperty(suite, "description");
        System.out.println(suiteDesc + " : " + specDesc + " ... ");
        
        notifier.fireTestStarted(Description.createTestDescription(Object.class, suiteDesc + " : " + specDesc));
    }
    
    public void reportSuiteResults(NativeObject suite) {
        Object suiteDesc = NativeObject.getProperty(suite, "description");
        NativeObject results = (NativeObject)NativeObject.callMethod(suite, "results", new Object[0]);
        Double passedCount = (Double)NativeObject.getProperty(results, "passedCount");
        Double totalCount = (Double)NativeObject.getProperty(results, "totalCount");
        System.out.println(suiteDesc + ": " + passedCount.intValue() + " of " + totalCount.intValue() + " passed.");
    }
}