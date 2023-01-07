import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.*;
import ru.yandex.practicum.kanban.http.HttpTaskManager;
import ru.yandex.practicum.kanban.http.HttpTaskServer;
import ru.yandex.practicum.kanban.http.KVServer;
import ru.yandex.practicum.kanban.manager.Managers;
import ru.yandex.practicum.kanban.manager.TaskManager;
import ru.yandex.practicum.kanban.tasks.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    private HttpTaskServer httpTaskServer;
    private HttpTaskManager httpTaskManager;
    private HttpClient httpClient = HttpClient.newHttpClient();
    private KVServer kvServer;
    private final String path = "http://localhost:8080";
    private static final Gson gson = Managers.getGson();
    public static TaskManager manager = Managers.getDefaultFileBackedManager();

    @BeforeAll
    static void shouldCreateFileForeTests() {
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

    @BeforeEach
    void startServer() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        manager = Managers.getDefaultFileBackedManager();
        httpTaskServer = new HttpTaskServer(manager);
        httpTaskServer.start();
        httpTaskManager = Managers.getDefault();
    }

    @AfterEach
    void stopStartServer() {
        httpTaskServer.stop();
        kvServer.stop();
    }

    @Test
    void shouldGetTasks() {
        URI url = URI.create(path + "/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            String json = response.body();
            Type type = new TypeToken<Map<Integer, Task>>() {
            }.getType();
            Map<Integer, Task> tasksMap = gson.fromJson(json, type);
            List<Task> tasksList = new ArrayList<>(tasksMap.values());
            List<Task> expectedList = new ArrayList<>(httpTaskManager.getTasks().values());
            for (int i = 0; i < tasksList.size(); i++) {
                assertEquals(expectedList.get(i).getTaskName(), tasksList.get(i).getTaskName());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void shouldGetEpics() {
        URI url = URI.create(path + "/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            String json = response.body();
            Type type = new TypeToken<Map<Integer, Epic>>() {
            }.getType();
            Map<Integer, Epic> epicsMap = gson.fromJson(json, type);
            List<Epic> epicsList = new ArrayList<>(epicsMap.values());
            List<Epic> expectedList = new ArrayList<>(httpTaskManager.getEpics().values());
            for (int i = 0; i < epicsList.size(); i++) {
                assertEquals(expectedList.get(i).getTaskName(), epicsList.get(i).getTaskName());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void shouldGetSubtasks() {
        URI url = URI.create(path + "/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            String json = response.body();
            Type type = new TypeToken<Map<Integer, Subtask>>() {
            }.getType();
            Map<Integer, Subtask> subtasksMap = gson.fromJson(json, type);
            List<Task> subtasksList = new ArrayList<>(subtasksMap.values());
            List<Task> expectedList = new ArrayList<>(httpTaskManager.getSubtasks().values());
            for (int i = 0; i < subtasksList.size(); i++) {
                assertEquals(expectedList.get(i).getTaskName(), subtasksList.get(i).getTaskName());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void shouldGetTaskById() {
        URI url = URI.create(path + "/tasks/task?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            String json = response.body();
            System.out.println(response.body());
            Task task = gson.fromJson(json, Task.class);
            Task expectedTask = httpTaskManager.getTasks().get(1);
            assertEquals(expectedTask.getTaskName(), task.getTaskName());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void shouldGetSubtaskById() {
        URI url = URI.create(path + "/tasks/subtask?id=3");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            String json = response.body();
            Subtask subtask = gson.fromJson(json, Subtask.class);
            Subtask expectedSubtask = httpTaskManager.getSubtasks().get(3);
            assertEquals(expectedSubtask.getTaskName(), subtask.getTaskName());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void shouldGetHistory() {
        URI url = URI.create(path + "/tasks/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            String json = response.body();
            Type type = new TypeToken<ArrayList<Task>>() {
            }.getType();
            List<Task> historyList = gson.fromJson(json, type);
            List<Task> expectedHistoryList = new ArrayList<>(httpTaskManager.getHistory());
            for (int i = 0; i < historyList.size(); i++) {
                assertEquals(expectedHistoryList.get(i).getTaskName(), historyList.get(i).getTaskName());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void shouldDeleteTaskByIdFromServer() {
        HttpTaskManager rollback = Managers.getDefault();
        URI url = URI.create(path + "/tasks/task?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            HttpTaskManager taskManager = Managers.getDefault();
            assertNull(taskManager.getTasks().get(2));
            httpTaskManager = rollback;
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void shouldDeleteEpicByIdAndAllSubtasksByThisEpicFromServer() {
        httpTaskManager = Managers.getDefault();
        URI url = URI.create(path + "/tasks/epic?id=2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals(true, httpTaskManager.getEpics().isEmpty());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void shouldDeleteSubtaskByIdFromServer() {
        HttpTaskManager rollback = Managers.getDefault();
        URI url = URI.create(path + "/tasks/subtask?id=6");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            httpTaskManager = Managers.getDefault();
            assertNull(httpTaskManager.getSubtasks().get(6));
            httpTaskManager = rollback;
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }
}