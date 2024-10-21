package code;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DataTest {
    private String logDir = "/home/denis/IdeaProjects/LogParser/app/src/test/resources";
    private Data data = new Data(logDir);

    @Test
    public void getLogEntityListTest() {
        List<LogEntity> expected = data.getLogEntityList();

        assertTrue(!expected.isEmpty());

        assertEquals(expected.stream()
                .filter(entity -> entity.getStatus().equals(Status.ERROR))
                .map(LogEntity::getUser)
                .collect(Collectors.joining()), "Vasya Pupkin"
        );
    }

    @Test
    public void getDataForQueryEventTest() {
        Set<Object> expected = data.getDataForQuery("get event");

        Set<Event> actual = Set.of(
                Event.WRITE_MESSAGE,
                Event.DONE_TASK,
                Event.LOGIN,
                Event.DOWNLOAD_PLUGIN,
                Event.SOLVE_TASK
        );

        assertEquals(expected, actual);
    }

    @Test
    public void getDataForQueryUserStatusTest() {
        Set<Object> expected = data.getDataForQuery("get status for user = \"Amigo\"");

        Set<Status> actual = Set.of(Status.OK);

        assertEquals(expected, actual);
    }
}
