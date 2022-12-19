import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.http.HttpTaskManager;
import ru.yandex.practicum.kanban.http.KVServer;
import ru.yandex.practicum.kanban.manager.Managers;
import ru.yandex.practicum.kanban.tasks.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class KVTaskClientTest {
    HttpTaskManager httpTaskManager;

    @BeforeAll
    static void shouldAddTask() {
        var manager = Managers.getDefault();

        Task task = new Task(
                1,
                TaskType.TASK,
                "Task",
                TaskStatus.NEW,
                "Task description",
                30,
                LocalDateTime.of(2022, Month.NOVEMBER, 21, 18, 00));

        Epic epic = new Epic(
                2,
                TaskType.EPIC,
                "Epic",
                TaskStatus.NEW,
                "Epic description");

        Subtask subtask = new Subtask(
                3,
                TaskType.SUBTASK,
                "Subtask",
                TaskStatus.NEW,
                "Subtask description",
                30,
                LocalDateTime.of(2022, Month.NOVEMBER, 22, 18, 00),
                2);

        manager.addTask(task);
        manager.addEpic(epic);
        manager.addSubtask(subtask);

        manager.getTaskById(1);
        manager.getEpicById(2);
    }

    @Test
    void shouldSaveAndLoadTasksKVServer() throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        httpTaskManager = Managers.getDefault();
        httpTaskManager.getToken();
        httpTaskManager.saveTasks();
        HttpTaskManager loadedFromServerManager = new HttpTaskManager("http://localhost:8078");
        loadedFromServerManager.getToken();
        loadedFromServerManager.loadTasks();
        kvServer.stop();
    }
}