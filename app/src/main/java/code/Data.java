package code;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Data {
    private static Path logDir;

    public Data(Path logDir) {
        this.logDir = logDir;
    }

    public static List<Path> getPaths() {
        List<Path> pathsFiles = new ArrayList<>();
        try {
            DirectoryStream<Path> paths = Files.newDirectoryStream(logDir, "*.{log}");
            paths.forEach(pathsFiles::add);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return pathsFiles;
    }

    public List<LogEntity> getLogEntityList() {
        List<Path> paths = getPaths();
        List<LogEntity> result = new ArrayList<>();
        for (Path path : paths) {
            String line = "";
            try {
                BufferedReader reader = new BufferedReader(new FileReader(path.toFile()));
                while (reader.ready()) {
                    line = reader.readLine();
                    String[] parameters = line.split("\t");
                    String ip = parameters[0];
                    String user = parameters[1];
                    Date date = Utils.stringToDate(parameters[2]);
                    Event event = null;
                    int taskId = 0;
                    String[] eventAndNumber = parameters[3].split(" ");
                    if (eventAndNumber.length == 2) {
                        event = Event.valueOf(eventAndNumber[0]);
                        taskId = Integer.parseInt(eventAndNumber[1]);
                    } else {
                        event = Event.valueOf(eventAndNumber[0]);
                    }
                    Status status = Status.valueOf(parameters[4]);
                    LogEntity logEntity = new LogEntity(ip, user, date, event, taskId, status);
                    result.add(logEntity);
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }
}
