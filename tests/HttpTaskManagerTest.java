import org.junit.jupiter.api.*;
import ru.yandex.practicum.kanban.http.HttpTaskManager;
import ru.yandex.practicum.kanban.http.HttpTaskServer;
import ru.yandex.practicum.kanban.http.KVServer;
import ru.yandex.practicum.kanban.manager.Managers;
import ru.yandex.practicum.kanban.tasks.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest {
    public static HttpTaskManager manager;
    private KVServer kvServer;
    private HttpTaskServer httpTaskServer;

    @Test
    public void shouldLoadFromKVServer() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        manager = Managers.getDefault();
        httpTaskServer = new HttpTaskServer(manager);
        httpTaskServer.start();

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
        manager.getSubtaskById(3);

        manager.saveTasksToKVServer();

        HttpTaskManager taskManager = new HttpTaskManager(KVServer.PORT, true);
        final List<Task> tasks = taskManager.getTasks().values().stream().toList();
        assertNotNull(tasks, "Возвращает непустой список задач.");
        assertEquals(1, tasks.size(), "Возвращает непустой список задач.");

        final List<Epic> epics = taskManager.getEpics().values().stream().toList();
        assertNotNull(epics, "Возвращает непустой список задач.");
        assertEquals(1, epics.size(), "Возвращает непустой список задач.");

        final List<Subtask> subtasks = taskManager.getSubtasks().values().stream().toList();
        assertNotNull(subtasks, "Возвращает непустой список задач.");
        assertEquals(1, subtasks.size(), "Возвращает непустой список задач.");

        final List<Task> history= taskManager.getHistory();
        assertNotNull(history, "Возвращает непустой список истории.");
        //assertEquals(3, history.size(), "Возвращает непустой список истории.");

        kvServer.stop();
        httpTaskServer.stop();
    }
}