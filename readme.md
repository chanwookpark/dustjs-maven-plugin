# DustJS Pre-Compile maven plugin project 

Especially, support to pre-compile partial template.

[code]
<plugin>
    <groupId>io.nine</groupId>
    <artifactId>dustjs-maven-plugin</artifactId>
    <version>1.0-SNAPSHOT</version>
    <configuration>
        <sourceDirectory>src/main/resources/dust/partial</sourceDirectory>
        <targetDirectory>target/classes/dust/compiled</targetDirectory>
        <dustjsFile>META-INF/resources/webjars/dustjs-linkedin/2.6.1/dust-full.js</dustjsFile>
    </configuration>
    <executions>
        <execution>
            <phase>compile</phase>
            <goals>
                <goal>dustjsCompiler</goal>
            </goals>
        </execution>
    </executions>
</plugin>
[code]