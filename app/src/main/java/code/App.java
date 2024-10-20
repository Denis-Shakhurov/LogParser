package code;

import picocli.CommandLine;

import java.nio.file.Paths;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "LogParses", mixinStandardHelpOptions = true, version = "logParser v 1.0",
        description = "Compares two configuration files and shows a difference.")
public final class App implements Callable<String> {

    @CommandLine.Parameters(paramLabel = "pathDir", description = "path to directory")
    private String pathDir;

    @CommandLine.Parameters(paramLabel = "query", description = "string query")
    private String query;

    public String call() {
        System.out.println(Parser.executor(Paths.get(pathDir), query));
        return null;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }
}
