package org.netmelody.menodora.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.tools.shell.Global;
import org.netmelody.menodora.core.locator.Locator;

public final class JasmineExecutionEnvironment {
    
    private final Context context = ContextFactory.getGlobal().enterContext();
    private final Global global = new Global();
    private final boolean withDom;
    
    public JasmineExecutionEnvironment(boolean withDom) {
        this.withDom = withDom;
        
        context.setOptimizationLevel(-1);
        context.setLanguageVersion(Context.VERSION_1_5);
        global.init(context);
        
       if (this.withDom) {
            eval("var __rhino__ = Packages." + Context.class.getName().replace(".Context", ";"));
            eval("Packages." + Context.class.getName() + ".getCurrentContext().setOptimizationLevel(-1);");
            loadJavaScript("/env.js-1.2/env.rhino.1.2.js");
            eval("Envjs.scriptTypes['text/javascript'] = true;");
        }
        else {
            final Timer timer = new Timer();
            global.put("__timer__", global, timer);
            loadJavaScript("/menodora-js/fake.scheduler.js");
        }
        
        loadJavaScript("/jasmine-1.0.2/jasmine.js");
    }
    
    public void executeJasmineTests(Locator javascriptResources, JasmineReporter reporter) {
        for (String resource : javascriptResources.locate()) {
            loadJavaScript(resource);
        }
        
        global.put("jUnitReporter", global, reporter);
        eval("jasmine.getEnv().addReporter(jUnitReporter);");
        
        try {
            if (this.withDom) {
                File loader = File.createTempFile("jasmine", ".html");
                FileUtils.writeStringToFile(loader, "<html><script type='text/javascript'>jasmine.getEnv().execute();</script></html>");
                final String uri = loader.toURI().toString().replaceFirst("^file:/([^/])", "file:///$1");
                eval(String.format("window.location = '%s';", uri));
            }
            else {
                eval("jasmine.getEnv().execute();");
                eval("__timer__.done();");
            }
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    private Object loadJavaScript(String resource) {
        try {
            URL url = getClass().getResource(resource);
            String scriptSource = IOUtils.toString(url.openStream());
            String path = url.toExternalForm();
            return context.compileString(scriptSource, path, 1, null).exec(context, global);
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
    
    private Object eval(String script) {
        return context.compileString(script, "local.js", 1, null).exec(context, global);
    }
}
