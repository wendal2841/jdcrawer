package com.crawler.common;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

/**
 * Author     : zh_zhou@Ctrip.com
 * Copyright  : Ctrip Copyright (c) 2017
 * Company    : Ctrip
 * Create at  : 2017/5/31 13:26
 * Description:
 */
public abstract class JsCallback implements Function {

    @Override
    public Scriptable construct(Context cx, Scriptable scope, Object[] args) {
        return null;
    }

    @Override
    public String getClassName() {
        return null;
    }

    @Override
    public Object get(String name, Scriptable start) {
        return null;
    }

    @Override
    public Object get(int index, Scriptable start) {
        return null;
    }

    @Override
    public boolean has(String name, Scriptable start) {
        return false;
    }

    @Override
    public boolean has(int index, Scriptable start) {
        return false;
    }

    @Override
    public void put(String name, Scriptable start, Object value) {

    }

    @Override
    public void put(int index, Scriptable start, Object value) {

    }

    @Override
    public void delete(String name) {

    }

    @Override
    public void delete(int index) {

    }

    @Override
    public Scriptable getPrototype() {
        return null;
    }

    @Override
    public void setPrototype(Scriptable prototype) {

    }

    @Override
    public Scriptable getParentScope() {
        return null;
    }

    @Override
    public void setParentScope(Scriptable parent) {

    }

    @Override
    public Object[] getIds() {
        return new Object[0];
    }

    @Override
    public Object getDefaultValue(Class<?> hint) {
        return null;
    }

    @Override
    public boolean hasInstance(Scriptable instance) {
        return false;
    }
}
