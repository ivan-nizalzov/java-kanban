import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.*;
import ru.yandex.practicum.kanban.http.HttpTaskManager;
import ru.yandex.practicum.kanban.http.HttpTaskServer;
import ru.yandex.practicum.kanban.http.KVServer;
import ru.yandex.practicum.kanban.manager.Managers;
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

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    HttpTaskServer httpTaskServer;
    HttpTaskManager httpTaskManager;
    HttpClient httpClient = HttpClient.newHttpClient();
    KVServer kvServer;
    String path = "http://localhost:8080";
    private static final Gson gson = Managers.getGson();

    @BeforeAll
    static void shouldCreateFileForeTests() {
        HttpTaskManager manager = Managers.getDefault();

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
    void starServer() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        httpTaskManager = Managers.getDefault();
    }

    @AfterEach
    void stopStart() {
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
            Type type = new TypeToken<ArrayList<Task>>() {
            }.getType();
            List<Task> tasksList = gson.fromJson(json, type);
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
            Type type = new TypeToken<ArrayList<Epic>>() {
            }.getType();
            List<Epic> tasksList = gson.fromJson(json, type);
            List<Epic> expectedList = new ArrayList<>(httpTaskManager.getEpics().values());
            for (int i = 0; i < tasksList.size(); i++) {
                assertEquals(expectedList.get(i).getTaskName(), tasksList.get(i).getTaskName());
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
            Type type = new TypeToken<ArrayList<Subtask>>() {
            }.getType();
            List<Subtask> tasksList = gson.fromJson(json, type);
            List<Subtask> expectedList = new ArrayList<>(httpTaskManager.getSubtasks().values());
            for (int i = 0; i < tasksList.size(); i++) {
                assertEquals(expectedList.get(i).getTaskName(), tasksList.get(i).getTaskName());
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
    void shouldGetPrioritizedList() {
        URI url = URI.create(path + "/tasks/priority");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode());

            String json = response.body();
            Type type = new TypeToken<List<Task>>() {
            }.getType();
            List<Task> prioritizedList = gson.fromJson(json, type);
            List<Task> expectedPrioritizedList = new ArrayList<>(httpTaskManager.getPrioritizedTasks());
            for (int i = 0; i < prioritizedList.size(); i++) {
                assertEquals(expectedPrioritizedList.get(i).getTaskName(), prioritizedList.get(i).getTaskName());
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
        HttpTaskManager rollback = Managers.getDefault();
        URI url = URI.create(path + "/tasks/epic?id=2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            httpTaskManager = Managers.getDefault();
            assertNull(httpTaskManager.getEpicById(2));
            httpTaskManager = rollback;
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