package org.netmelody.menodora;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface JasmineJavascriptContext {
    public String[] source() default "*.js";
    public String[] jasmineHelpers() default "";
    public String[] jasmineSpecs() default "*Spec.js";
    public boolean withSimulatedDom() default false;
}