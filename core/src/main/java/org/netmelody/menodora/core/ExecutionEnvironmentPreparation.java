package org.netmelody.menodora.core;

import org.mozilla.javascript.Context;

public final class ExecutionEnvironmentPreparation {
    private final boolean withDom;

    public ExecutionEnvironmentPreparation(boolean withDom) {
        this.withDom = withDom;
    }

    public void prepare(JavaScriptEnvironment environment) {
        if (this.withDom) {
            prepareWithSimulatedDom(environment);
        } else {
            prepareWithoutSimulatedDom(environment);
        }
    }

    private void prepareWithoutSimulatedDom(JavaScriptEnvironment environment) {
        final Timer timer = new Timer();
        environment.setGlobal("__timer__", timer);
        environment.loadResource("menodora-js/fake.scheduler.js");
    }

    private void prepareWithSimulatedDom(JavaScriptEnvironment environment) {
        environment.eval("var __rhino__ = Packages." + Context.class.getName().replace(".Context", ";"));
        environment.eval("Packages." + Context.class.getName() + ".getCurrentContext().setOptimizationLevel(-1);");
        environment.loadResource("env.js-1.2/env.rhino.1.2.js");
        environment.eval("Envjs.scriptTypes['text/javascript'] = true;");
    }
}
