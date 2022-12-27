import org.junit.jupiter.api.*;
import ru.yandex.practicum.kanban.http.HttpTaskManager;
import ru.yandex.practicum.kanban.http.KVServer;
import ru.yandex.practicum.kanban.manager.Managers;
import ru.yandex.practicum.kanban.tasks.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest {
    HttpTaskManager httpTaskManager;
    KVServer kvServer;

    @BeforeAll
    static void shouldConstructTasksForTests() {
        var httpTaskManager = Managers.getDefault();

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

        httpTaskManager.addTask(task);
        httpTaskManager.addEpic(epic);
        httpTaskManager.addSubtask(subtask);

        httpTaskManager.getTaskById(1);
        httpTaskManager.getEpicById(2);
        httpTaskManager.getSubtaskById(3);
    }

    @BeforeEach
    void starServer() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskManager = Managers.getDefault();
    }

    @AfterEach
    void stopStart() {
        kvServer.stop();
    }

    @Test
    public void shouldLoadFromHttpServer() {
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
        assertEquals(3, subtasks.size(), "Возвращает непустой список истории.");
    }
}