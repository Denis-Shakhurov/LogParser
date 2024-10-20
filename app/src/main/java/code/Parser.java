package code;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static code.Utils.dateBetweenDates;
import static code.Utils.normalizeWord;
import static code.Utils.stringToDate;

public class Parser {

    public static String executor(String pathDir, String query) {
        Data data = new Data(Paths.get(pathDir));
        Set<Object> result = new HashSet<>();
        List<LogEntity> logEntityList = data.getLogEntityList();
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
        StringBuilder sb = new StringBuilder("\n");
        result.forEach(o -> {
            if (o != null) {
                sb.append(o.toString() + "\n");
            } else {
                System.out.println("Query not correct!");
            }
        });
        return sb.toString();
    }

    private static Map<String, String> getFieldAndValue(String query) {
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

    private static Object getObject(LogEntity logEntity, String field) {
        Object object = null;
        switch (field) {
            case "ip" : object = logEntity.getIp(); break;
            case "date" : object = logEntity.getDate(); break;
            case "user" : object = logEntity.getUser(); break;
            case "event" : object = logEntity.getEvent(); break;
            case "taskId" : object = logEntity.getTaskId(); break;
            case "status" : object = logEntity.getStatus(); break;
        }
        return object;
    }
}
