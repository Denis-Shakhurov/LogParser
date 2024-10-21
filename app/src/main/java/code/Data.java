package code;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static code.Utils.dateBetweenDates;
import static code.Utils.normalizeWord;
import static code.Utils.stringToDate;

public class Data {
    private String logDir;

    public Data(String logDir) {
        this.logDir = logDir;
    }

    private List<Path> getPaths() {
        List<Path> pathsFiles = new ArrayList<>();
        try {
            Path path = stringToPath(logDir);
            DirectoryStream<Path> paths = Files.newDirectoryStream(path, "*.{log}");
            paths.forEach(pathsFiles::add);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return pathsFiles;
    }

    private Path stringToPath(String pathDir) throws Exception {
        Path path = Paths.get(pathDir).toAbsolutePath().normalize();
        if (Files.notExists(path)) {
            throw new Exception("File not exists");
        }
        return path;
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

    public Set<Object> getDataForQuery(String query) {
        Set<Object> result = new HashSet<>();
        List<LogEntity> logEntityList = getLogEntityList();
        Map<String, String> fieldAndValue = getFieldAndValue(query);

        if (fieldAndValue.size() == 1) {
            String field = fieldAndValue.get("field1");
            result.addAll(logEntityList.stream()
                    .map(entity -> getObject(entity, field))
                    .collect(Collectors.toSet()));

        } else if (fieldAndValue.size() == 3) {
            String field1 = fieldAndValue.get("field1");
            String field2 = fieldAndValue.get("field2");
            String value = fieldAndValue.get("value");

            if (field2.equals("date")) {
                result.addAll(logEntityList.stream()
                        .filter(entity -> entity.getDate().getTime() == stringToDate(value).getTime())
                        .map(entity -> getObject(entity, field1))
                        .collect(Collectors.toSet()));
            } else {
                result.addAll(logEntityList.stream()
                        .filter(entity -> getObject(entity, field2).toString().equalsIgnoreCase(value))
                        .map(entity -> getObject(entity, field1))
                        .collect(Collectors.toSet()));
            }
        } else if (fieldAndValue.size() == 5) {
            String field1 = fieldAndValue.get("field1");
            String field2 = fieldAndValue.get("field2");
            String value = fieldAndValue.get("value");
            String after = fieldAndValue.get("after");
            String before = fieldAndValue.get("before");

            if (field2.equals("date")) {
                result.addAll(logEntityList.stream()
                        .filter(entity ->
                                dateBetweenDates(entity.getDate(),
                                        stringToDate(after),
                                        stringToDate(before)))
                        .filter(entity -> entity.getDate().getTime() == stringToDate(value).getTime())
                        .map(entity -> getObject(entity, field1))
                        .collect(Collectors.toSet()));
            } else {
                result.addAll(logEntityList.stream()
                        .filter(entity ->
                                dateBetweenDates(entity.getDate(),
                                        stringToDate(after),
                                        stringToDate(before)))
                        .filter(entity -> getObject(entity, field2).toString().equalsIgnoreCase(value))
                        .map(entity -> getObject(entity, field1))
                        .collect(Collectors.toSet()));
            }
        }
        return result;
    }

    private Map<String, String> getFieldAndValue(String query) {
        Map<String, String> result = new HashMap<>();
        String[] queryParam = query.split("=");
        if (queryParam.length == 1) {
            String field = queryParam[0].split(" ")[1];
            result.put("field1", field);
        } else if (queryParam.length > 1) {
            if (!queryParam[1].contains("between")) {
                String[] fields = queryParam[0].split(" ");
                String field1 = fields[1].trim();
                String field2 = fields[3].trim();
                String value = normalizeWord(queryParam[1]);
                result.put("field1", field1);
                result.put("field2", field2);
                result.put("value", value);
            } else {
                String[] fields = queryParam[0].split(" ");
                String field1 = fields[1].trim();
                String field2 = fields[3].trim();
                String[] values = queryParam[1].split("and date between");
                String value = normalizeWord(values[0]);
                String[] dates = values[1].split("and");
                String after = normalizeWord(dates[0]);
                String before = normalizeWord(dates[1]);
                result.put("field1", field1);
                result.put("field2", field2);
                result.put("value", value);
                result.put("after", after);
                result.put("before", before);
            }
        } else {
            System.out.println("Query not correct");
        }
        return result;
    }

    private Object getObject(LogEntity logEntity, String field) {
        Object object = null;
        switch (field) {
            case "ip" : object = logEntity.getIp(); break;
            case "date" : object = logEntity.getDate(); break;
            case "user" : object = logEntity.getUser(); break;
            case "event" : object = logEntity.getEvent(); break;
            case "taskId" : object = logEntity.getTaskId(); break;
            case "status" : object = logEntity.getStatus(); break;
            default:
                throw new IllegalStateException("Unexpected value: " + field);
        }
        return object;
    }
}
