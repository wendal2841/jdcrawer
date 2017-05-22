package com.crawer.jd;

import org.apache.commons.io.IOUtils;
import sun.org.mozilla.javascript.internal.NativeObject;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author     : zh_zhou@Ctrip.com
 * Copyright  : Ctrip Copyright (c) 2017
 * Company    : Ctrip
 * Create at  : 2017/5/22 10:49
 * Description:
 */
public class EncryptScript {
    static Map<String, Invocable> engineMap = new ConcurrentHashMap<>();
    static final String defaultencryptKey = "23IL<N01c7KvwZO56RSTAfghiFyzWJqVabGH4PQdopUrsCuX*xeBjkltDEmn89.-";


    public static void main(String[] args) throws ScriptException, NoSuchMethodException, IOException {
        Object c = getFingure("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.109 Safari/537.36");
        NativeObject obj = (NativeObject) c;

        for (Map.Entry<Object, Object> entry : obj.entrySet()) {
            String key = (String) entry.getKey();
            Object value = entry.getValue();
            System.out.println(key);
        }


    }

    public static String encrypt(String json, String encryptKey) throws ScriptException, NoSuchMethodException, IOException {
        if (!engineMap.containsKey(encryptKey)) {
            engineMap.put(encryptKey, createEncryptFunc(encryptKey));
        }
        return (String) engineMap.get(encryptKey).invokeFunction("tdencrypt", json);
    }

    public static String encrypt(String json) throws ScriptException, NoSuchMethodException, IOException {
        return encrypt(json, defaultencryptKey);
    }

    private static Invocable createEncryptFunc(String key) throws ScriptException, IOException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        String script = loadResource("tdencrypt.js");
        script = script.replaceAll(Pattern.quote(defaultencryptKey), key);
        engine.eval(script);
        Invocable invoke = (Invocable) engine;
        return invoke;

    }

    private static String loadResource(String name) throws IOException {
        InputStream inputStream = EncryptScript.class.getResourceAsStream(name);
        List<String> lines = IOUtils.readLines(inputStream);
        inputStream.close();
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line);
        }
        String script = sb.toString();
        return script;
    }

    private static String getResult(String script) throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        return (String) engine.eval(script);
    }

    public static String parseEncryptKey(String script) throws ScriptException {
        if (script.startsWith("eval")) {
            script = script.substring(4);
        }
        String runningScript = (getResult(script));
        Pattern p = Pattern.compile("tdencrypt=function\\(.+?\\)\\{.+?\\\"(.+?)\\\"\\.charAt.+?\\\"(.+?)\\\"\\.charAt.+?\\\"(.+?)\\\"\\.charAt.+?\\\"(.+?)\\\"\\.charAt.+?return.+?\\}");
        Matcher m = p.matcher(runningScript);
        String key = null;
        while (m.find()) {
            key = m.group(2);
            break;
        }
        return key;
    }

    static Invocable FingerInvoke = null;


    static Object getFingure(String userAgent) throws ScriptException, IOException, NoSuchMethodException {
        Invocable invoke = loadFingureEngin();
        return invoke.invokeFunction("invokeItem", userAgent, "zh-CN");
    }

    private static Invocable loadFingureEngin() throws IOException, ScriptException {
        if (FingerInvoke == null) {
            String fingerScript = loadResource("JdJrTdRiskFinger.js");
            String invokeScript = loadResource("invokeRiskFingerScript.js");
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("javascript");
            engine.eval(fingerScript);
            engine.eval(invokeScript);
            FingerInvoke = (Invocable) engine;
            ;
        }
        return FingerInvoke;
    }


}
