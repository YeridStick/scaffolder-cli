package com.yerid.scaffolder;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

@Command(name = "scaffolder", mixinStandardHelpOptions = true, version = "1.0",
        description = "Generates Spring WebFlux Hexagonal projects.")
public class Scaffolder implements Callable<Integer> {

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Scaffolder())
                .addSubcommand("init", new CommandInit())
                .addSubcommand("add-entity", new CommandAddEntity())
                .execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() {
        CommandLine.usage(this, System.out);
        return 0;
    }
}

@Command(name = "init", description = "Initializes a new project.")
class CommandInit implements Callable<Integer> {

    @Option(names = {"-g", "--group"}, description = "Group ID (e.g., com.yerid)", required = true)
    private String groupId;

    @Option(names = {"-n", "--name"}, description = "Artifact ID / Project Name", required = true)
    private String artifactId;

    @Override
    public Integer call() throws Exception {
        System.out.println("Initializing project: " + artifactId + " [" + groupId + "]");
        
        Path baseDir = Paths.get(artifactId);
        Files.createDirectories(baseDir);

        // build.gradle
        writeFile(baseDir.resolve("build.gradle"), Templates.getBuildGradle(groupId, artifactId));

        // application.yml
        Path resourceDir = baseDir.resolve("src/main/resources");
        Files.createDirectories(resourceDir);
        writeFile(resourceDir.resolve("application.yml"), Templates.getApplicationYml(artifactId));

        // Main Class
        String packagePath = groupId.replace(".", "/") + "/" + artifactId.replace("-", "");
        Path javaDir = baseDir.resolve("src/main/java/" + packagePath);
        Files.createDirectories(javaDir);
        
        String className = capitalize(artifactId.replace("-", "")) + "Application";
        writeFile(javaDir.resolve(className + ".java"), 
            Templates.getMainApplication(groupId, artifactId.replace("-", ""), className));

        System.out.println("Project initialized successfully in ./" + artifactId);
        return 0;
    }

    private void writeFile(Path path, String content) throws IOException {
        Files.writeString(path, content);
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}

@Command(name = "add-entity", description = "Adds a new CRUD entity.")
class CommandAddEntity implements Callable<Integer> {

    @Option(names = {"-g", "--group"}, description = "Group ID", required = true)
    private String groupId;

    @Option(names = {"-p", "--project"}, description = "Project Name", required = true)
    private String artifactId;

    @Option(names = {"-n", "--name"}, description = "Entity Name", required = true)
    private String entityName;

    @Override
    public Integer call() throws Exception {
        System.out.println("Adding entity: " + entityName + " to project: " + artifactId);

        String packagePath = groupId.replace(".", "/") + "/" + artifactId.replace("-", "");
        Path baseJavaDir = Paths.get(artifactId, "src", "main", "java", packagePath);

        // Domain
        writeFile(baseJavaDir.resolve("domain/model/" + entityName + ".java"), 
            Templates.getDomainModel(groupId, artifactId.replace("-", ""), entityName));
        writeFile(baseJavaDir.resolve("domain/port/in/Find" + entityName + "UseCase.java"), 
            Templates.getPortIn(groupId, artifactId.replace("-", ""), entityName));
        writeFile(baseJavaDir.resolve("domain/port/out/" + entityName + "Repository.java"), 
            Templates.getPortOut(groupId, artifactId.replace("-", ""), entityName));
        writeFile(baseJavaDir.resolve("domain/service/" + entityName + "Service.java"), 
            Templates.getDomainService(groupId, artifactId.replace("-", ""), entityName));

        // Adapter
        writeFile(baseJavaDir.resolve("adapter/in/web/" + entityName + "Handler.java"), 
            Templates.getWebHandler(groupId, artifactId.replace("-", ""), entityName));
        writeFile(baseJavaDir.resolve("adapter/in/web/" + entityName + "Router.java"), 
            Templates.getRouter(groupId, artifactId.replace("-", ""), entityName));
        writeFile(baseJavaDir.resolve("adapter/out/persistence/" + entityName + "PersistenceAdapter.java"), 
            Templates.getPersistenceAdapter(groupId, artifactId.replace("-", ""), entityName));

        System.out.println("Entity " + entityName + " generated successfully.");
        return 0;
    }

    private void writeFile(Path path, String content) throws IOException {
        Files.createDirectories(path.getParent());
        Files.writeString(path, content);
    }
}
