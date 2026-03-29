package com.yerid.scaffolder;

public class Templates {

    public static String getBuildGradle(String groupId, String artifactId) {
        return "plugins {\n" +
                "    id 'org.springframework.boot' version '3.2.4'\n" +
                "    id 'io.spring.dependency-management' version '1.1.4'\n" +
                "    id 'java'\n" +
                "}\n\n" +
                "group = '" + groupId + "'\n" +
                "version = '0.0.1-SNAPSHOT'\n\n" +
                "java {\n" +
                "    sourceCompatibility = '17'\n" +
                "}\n\n" +
                "repositories {\n" +
                "    mavenCentral()\n" +
                "}\n\n" +
                "dependencies {\n" +
                "    implementation 'org.springframework.boot:spring-boot-starter-webflux'\n" +
                "    implementation 'org.springframework.boot:spring-boot-starter-data-r2dbc'\n" +
                "    implementation 'org.springframework.boot:spring-boot-starter-validation'\n" +
                "    runtimeOnly 'org.postgresql:postgresql'\n" +
                "    runtimeOnly 'org.postgresql:r2dbc-postgresql'\n" +
                "    testImplementation 'org.springframework.boot:spring-boot-starter-test'\n" +
                "    testImplementation 'io.projectreactor:reactor-test'\n" +
                "}\n\n" +
                "tasks.named('test') {\n" +
                "    useJUnitPlatform()\n" +
                "}\n";
    }

    public static String getApplicationYml(String artifactId) {
        return "spring:\n" +
                "  application:\n" +
                "    name: " + artifactId + "\n" +
                "  r2dbc:\n" +
                "    url: r2dbc:postgresql://localhost:5432/mydb\n" +
                "    username: user\n" +
                "    password: password\n" +
                "    pool:\n" +
                "      initial-size: 10\n" +
                "      max-size: 20\n";
    }

    public static String getMainApplication(String groupId, String artifactId, String className) {
        return "package " + groupId + "." + artifactId + ";\n\n" +
                "import org.springframework.boot.SpringApplication;\n" +
                "import org.springframework.boot.autoconfigure.SpringBootApplication;\n\n" +
                "@SpringBootApplication\n" +
                "public class " + className + " {\n" +
                "    public static void main(String[] args) {\n" +
                "        SpringApplication.run(" + className + ".class, args);\n" +
                "    }\n" +
                "}\n";
    }

    public static String getDomainModel(String groupId, String artifactId, String entityName) {
        return "package " + groupId + "." + artifactId + ".domain.model;\n\n" +
                "public record " + entityName + "(Long id, String name) {}\n";
    }

    public static String getPortIn(String groupId, String artifactId, String entityName) {
        return "package " + groupId + "." + artifactId + ".domain.port.in;\n\n" +
                "import " + groupId + "." + artifactId + ".domain.model." + entityName + ";\n" +
                "import reactor.core.publisher.Flux;\n" +
                "import reactor.core.publisher.Mono;\n\n" +
                "public interface Find" + entityName + "UseCase {\n" +
                "    Flux<" + entityName + "> findAll();\n" +
                "    Mono<" + entityName + "> findById(Long id);\n" +
                "}\n";
    }

    public static String getWebHandler(String groupId, String artifactId, String entityName) {
        return "package " + groupId + "." + artifactId + ".adapter.in.web;\n\n" +
                "import " + groupId + "." + artifactId + ".domain.port.in.Find" + entityName + "UseCase;\n" +
                "import org.springframework.stereotype.Component;\n" +
                "import org.springframework.web.reactive.function.server.ServerRequest;\n" +
                "import org.springframework.web.reactive.function.server.ServerResponse;\n" +
                "import reactor.core.publisher.Mono;\n\n" +
                "@Component\n" +
                "public class " + entityName + "Handler {\n" +
                "    private final Find" + entityName + "UseCase useCase;\n\n" +
                "    public " + entityName + "Handler(Find" + entityName + "UseCase useCase) {\n" +
                "        this.useCase = useCase;\n" +
                "    }\n\n" +
                "    public Mono<ServerResponse> findAll(ServerRequest request) {\n" +
                "        return ServerResponse.ok().body(useCase.findAll(), " + entityName + ".class);\n" +
                "    }\n" +
                "}\n";
    }

    public static String getPortOut(String groupId, String artifactId, String entityName) {
        return "package " + groupId + "." + artifactId + ".domain.port.out;\n\n" +
                "import " + groupId + "." + artifactId + ".domain.model." + entityName + ";\n" +
                "import reactor.core.publisher.Flux;\n" +
                "import reactor.core.publisher.Mono;\n\n" +
                "public interface " + entityName + "Repository {\n" +
                "    Flux<" + entityName + "> findAll();\n" +
                "    Mono<" + entityName + "> findById(Long id);\n" +
                "    Mono<" + entityName + "> save(" + entityName + " " + entityName.toLowerCase() + ");\n" +
                "}\n";
    }

    public static String getDomainService(String groupId, String artifactId, String entityName) {
        return "package " + groupId + "." + artifactId + ".domain.service;\n\n" +
                "import " + groupId + "." + artifactId + ".domain.model." + entityName + ";\n" +
                "import " + groupId + "." + artifactId + ".domain.port.in.Find" + entityName + "UseCase;\n" +
                "import " + groupId + "." + artifactId + ".domain.port.out." + entityName + "Repository;\n" +
                "import org.springframework.stereotype.Service;\n" +
                "import reactor.core.publisher.Flux;\n" +
                "import reactor.core.publisher.Mono;\n\n" +
                "@Service\n" +
                "public class " + entityName + "Service implements Find" + entityName + "UseCase {\n" +
                "    private final " + entityName + "Repository repository;\n\n" +
                "    public " + entityName + "Service(" + entityName + "Repository repository) {\n" +
                "        this.repository = repository;\n" +
                "    }\n\n" +
                "    @Override\n" +
                "    public Flux<" + entityName + "> findAll() {\n" +
                "        return repository.findAll();\n" +
                "    }\n\n" +
                "    @Override\n" +
                "    public Mono<" + entityName + "> findById(Long id) {\n" +
                "        return repository.findById(id);\n" +
                "    }\n" +
                "}\n";
    }

    public static String getPersistenceAdapter(String groupId, String artifactId, String entityName) {
        return "package " + groupId + "." + artifactId + ".adapter.out.persistence;\n\n" +
                "import " + groupId + "." + artifactId + ".domain.model." + entityName + ";\n" +
                "import " + groupId + "." + artifactId + ".domain.port.out." + entityName + "Repository;\n" +
                "import org.springframework.stereotype.Component;\n" +
                "import reactor.core.publisher.Flux;\n" +
                "import reactor.core.publisher.Mono;\n\n" +
                "@Component\n" +
                "public class " + entityName + "PersistenceAdapter implements " + entityName + "Repository {\n" +
                "    // Aquí iría el R2DBC Repository real\n" +
                "    // private final " + entityName + "R2dbcRepository r2dbcRepository;\n\n" +
                "    @Override\n" +
                "    public Flux<" + entityName + "> findAll() {\n" +
                "        return Flux.empty(); // Mock\n" +
                "    }\n\n" +
                "    @Override\n" +
                "    public Mono<" + entityName + "> findById(Long id) {\n" +
                "        return Mono.empty(); // Mock\n" +
                "    }\n\n" +
                "    @Override\n" +
                "    public Mono<" + entityName + "> save(" + entityName + " " + entityName.toLowerCase() + ") {\n" +
                "        return Mono.just(" + entityName.toLowerCase() + ");\n" +
                "    }\n" +
                "}\n";
    }

    public static String getRouter(String groupId, String artifactId, String entityName) {
        String path = entityName.toLowerCase() + "s";
        return "package " + groupId + "." + artifactId + ".adapter.in.web;\n\n" +
                "import org.springframework.context.annotation.Bean;\n" +
                "import org.springframework.context.annotation.Configuration;\n" +
                "import org.springframework.web.reactive.function.server.RouterFunction;\n" +
                "import org.springframework.web.reactive.function.server.RouterFunctions;\n" +
                "import org.springframework.web.reactive.function.server.ServerResponse;\n\n" +
                "import static org.springframework.web.reactive.function.server.RequestPredicates.GET;\n\n" +
                "@Configuration\n" +
                "public class " + entityName + "Router {\n" +
                "    @Bean\n" +
                "    public RouterFunction<ServerResponse> " + entityName.toLowerCase() + "Route(" + entityName + "Handler handler) {\n" +
                "        return RouterFunctions.route(GET(\"/" + path + "\"), handler::findAll);\n" +
                "    }\n" +
                "}\n";
    }
}
