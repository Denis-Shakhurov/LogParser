package code;

import java.util.Set;

public class Parser {

    public static String executor(String pathDir, String query) {
        Data data = new Data(pathDir);
        Set<Object> setObject = data.getDataForQuery(query);

        StringBuilder sb = new StringBuilder("\n");
        setObject.forEach(o -> {
            if (o != null) {
                sb.append(o.toString() + "\n");
            } else {
                System.out.println("Query not correct!");
            }
        });
        return sb.toString();
    }
}
