package org.netmelody.menodora;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.netmelody.menodora.core.TestStyle;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface JavaScriptContext {
    Class<? extends TestStyle> style();

    String[] source() default "*.js";

    String[] helpers() default "";

    String[] tests() default "*Spec.js";

    boolean withSimulatedDom() default false;
}
