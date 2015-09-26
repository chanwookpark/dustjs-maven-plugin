package io.nine;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * <pre>
 *     HTML Template pre-compile for DustJS
 * </pre>
 *
 * @author chanwook
 */
@Mojo(name = "dustjsCompiler")
public class DustJSCompileMojo extends AbstractMojo {
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Hello~");
    }
}
