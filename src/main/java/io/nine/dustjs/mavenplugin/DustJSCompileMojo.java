package io.nine.dustjs.mavenplugin;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     HTML Template pre-compile for DustJS
 * </pre>
 *
 * @author chanwook
 */
@Mojo(name = "dustjsCompiler")
public class DustJSCompileMojo extends AbstractMojo {

    /**
     *
     */
    @Parameter(property = "dustjsCompiler.sourceDirectory", required = false)
    private String sourceDirectory;

    @Parameter(property = "dustjsCompiler.targetDirectory", required = false)
    private String targetDirectory;

    @Parameter(property = "dustjsCompiler.compileScript", required = false)
    private String compileScript;

    @Parameter(property = "dustjsCompiler.dustjsFile", required = true)
    private String dustjsFile;

    private DustCompiler dustCompiler = new DustCompiler();

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("DustJS Pre-Compiler Source Directory: " + sourceDirectory + ", Target Directory: " + targetDirectory);

        // Step 1. Load to compile
        loadScript();

        // Step 2. Find template file and compile, then create compiled file
        Map<String, String> compiledTemplateMap = compileTemplate();
        createCompiledFile(compiledTemplateMap);

        getLog().info("DustJS Pre-Compile OK!");
    }

    protected void createCompiledFile(Map<String, String> compiledMap) {
        // Step 1. Directory cleaning..
        try {
            FileUtils.deleteDirectory(new File(targetDirectory));
            Files.createDirectories(Paths.get(targetDirectory));

            if (getLog().isDebugEnabled()) {
                getLog().debug("Cleansing target directory!(" + targetDirectory + ")");
            }
        } catch (IOException ioe) {
            throw new DustJSCompileException("Error when create compiled template file!", ioe);
        }

        // Step 2. Create file..
        for (Map.Entry<String, String> e : compiledMap.entrySet()) {
            try {
                String fileName = targetDirectory + File.separator + e.getKey() + ".js";
                Files.write(Paths.get(fileName), e.getValue().getBytes());
                if (getLog().isInfoEnabled()) {
                    getLog().info("Create compiled success!(path: " + fileName + ")");
                }
            } catch (IOException ioe) {
                throw new DustJSCompileException("Error when create compiled template file!", ioe);
            }
        }
    }

    protected Map<String, String> compileTemplate() {
        final File dir = new File(sourceDirectory);
        if (dir == null || !dir.isDirectory()) {
            throw new DustJSCompileException("Not found source direct(source: " + sourceDirectory + ")");
        }

        if (getLog().isInfoEnabled()) {
            getLog().info("Load template source directory: " + sourceDirectory + " directory has " + dir.listFiles().length + " File.");
        }

        Map<String, String> compiledMap = new HashMap<>();
        for (File template : dir.listFiles()) {
            if (getLog().isDebugEnabled()) {
                getLog().debug("Load DustJS template[" + template.getName() + "]");
            }
            final String templateKey = getTemplateKey(template);
            final String compiled = dustCompiler.compile(templateKey, template, getLog());
            compiledMap.put(templateKey, compiled);
        }
        return compiledMap;
    }

    protected void loadScript() {
        if (compileScript != null && compileScript.length() > 0) {
            dustCompiler.loadScript(compileScript);
        } else {
            dustCompiler.loadDefaultScript();
        }

        dustCompiler.loadScript(dustjsFile);
    }

    private String getTemplateKey(File template) {
        final String name = template.getName();
        return name.replaceAll(".html", "");
    }

    public void setCompileScript(String compileScript) {
        this.compileScript = compileScript;
    }

    public void setDustCompiler(DustCompiler dustCompiler) {
        this.dustCompiler = dustCompiler;
    }

    public void setSourceDirectory(String sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }

    public void setTargetDirectory(String targetDirectory) {
        this.targetDirectory = targetDirectory;
    }

    public void setDustjsFile(String dustjsFile) {
        this.dustjsFile = dustjsFile;
    }
}
