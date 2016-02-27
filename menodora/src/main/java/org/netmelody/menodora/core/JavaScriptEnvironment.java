package org.netmelody.menodora.core;

import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.tools.shell.Global;

public class JavaScriptEnvironment {
    private final Context context = ContextFactory.getGlobal().enterContext();
    private final Global global = new Global();

    {
        context.setOptimizationLevel(-1);
        context.setLanguageVersion(Context.VERSION_1_5);
        global.init(context);
    }

    public void eval(String script) {
        context.compileString(script, "local.js", 1, null).exec(context, global);
    }

    public void setGlobal(String name, Object value) {
        global.put(name, global, value);
    }

    public void loadResource(String resource) {
        URL url = getClass().getResource(resource);
        String scriptSource;
        try {
            scriptSource = IOUtils.toString(url.openStream());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        String path = url.toExternalForm();
        context.compileString(scriptSource, path, 1, null).exec(context, global);
    }
}
