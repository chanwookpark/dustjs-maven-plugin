package io.nine.dustjs.mavenplugin;

import org.apache.maven.plugin.logging.Log;

import javax.script.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author chanwook
 */
public class DustCompiler {

    private static final String SCRIPT_LOAD = "function compile(key, template) {\n" +
            "    var compiled = dust.compile(template, key); // compile\n" +
            "    return compiled;\n" +
            "}";

    private final ScriptEngine engine;

    public DustCompiler() {
        final ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine engine = sem.getEngineByName("nashorn");
        engine.setBindings(sem.getBindings(), ScriptContext.GLOBAL_SCOPE);
        this.engine = engine;
    }

    public String compile(String key, File templateFile, Log log) {
        try {
            String template = getTemplate(templateFile);

            final String compiled = (String) ((Invocable) engine).invokeFunction("compile", key, template);

            if (log.isDebugEnabled()) {
                log.debug("LOAD TEMPLATE-" + key + ":::" + compiled);
            }
            return compiled;
        } catch (ScriptException | NoSuchMethodException | IOException e) {
            throw new DustJSCompileException("Thrown exception when load template file(" + templateFile.getAbsolutePath() + ")", e);
        }
    }

    private String getTemplate(File file) throws IOException {
        final byte[] bytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        String template = new String(bytes);
        return template;
    }

    public void loadScript(String script) {
        try {
            final InputStream inputStream = getClass().getClassLoader().getResourceAsStream(script);
            loadScript(inputStream);
        } catch (Throwable te) {
            throw new DustJSCompileException("Template load script is null!(request path: " + script + ")", te);
        }
    }

    public void loadDefaultScript() {
        loadScript(new ByteArrayInputStream(SCRIPT_LOAD.getBytes()));
    }

    private void loadScript(InputStream inputStream) {
        try {
            engine.eval(new InputStreamReader(inputStream));
        } catch (ScriptException e) {
            throw new DustJSCompileException("Thrown exception when compiling template file!)", e);
        }
    }

}
