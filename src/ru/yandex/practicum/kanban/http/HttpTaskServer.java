package ru.yandex.practicum.kanban.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.kanban.manager.Managers;
import ru.yandex.practicum.kanban.manager.TaskManager;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;
import ru.yandex.practicum.kanban.tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static Gson gson;
    static HttpServer httpServer;
    private final TaskManager taskManager;

    public HttpTaskServer() throws IOException {
        this(Managers.getDefault());
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        gson = Managers.getGson();
        httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/tasks", this::handler);
    }

    public static void main(String[] args) throws IOException {
        final HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        //httpTaskServer.stop();
    }

    private void handler(HttpExchange h) {
        try (h) {
            System.out.println("\n/tasks: " + h.getRequestURI());
            final String path = h.getRequestURI().getPath().substring(7);
            switch (path) {
                case "" -> {
                    if (!h.getRequestMethod().equals("GET")) {
                        System.out.println("/Ждет GET-запрос, а получил: " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                    }
                    final String response = gson.toJson(taskManager.getPrioritizedTasks());
                    sendText(h, response);
                }
                case "history" -> {
                    if (!h.getRequestMethod().equals("GET")) {
                        System.out.println("/history ждет GET-запрос, а получил: " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                    }
                    final String response = gson.toJson(taskManager.getHistory());
                    sendText(h, response);
                }
                case "task" -> handleTask(h);
                case "subtask" -> handleSubtask(h);
                case "epic" -> handleEpic(h);
                default -> {
                    System.out.println("Неизвестный запрос: " + h.getRequestMethod());
                    h.sendResponseHeaders(404, 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleTask(HttpExchange h) throws IOException {
        final String query = h.getRequestURI().getQuery();
        switch (h.getRequestMethod()) {
            case "GET" -> {
                if (query == null) {
                    final Map<Integer, Task> tasks = taskManager.getTasks();
                    final String response = gson.toJson(tasks);
                    System.out.println("Получены все задачи.");
                    sendText(h, response);
                    return;
                }
                String idParam = query.substring(3);
                final int id = Integer.parseInt(idParam);
                final Task task = taskManager.getTaskById(id);
                final String response = gson.toJson(task);
                System.out.println("Получена задача с id=" + id);
                sendText(h, response);
            }
            case "DELETE" -> {
                if (query == null) {
                    taskManager.removeAllTasks();
                    System.out.println("Удалены все задачи.");
                    h.sendResponseHeaders(200, 0);
                    return;
                }
                String idParam = query.substring(3);
                final int id = Integer.parseInt(idParam);
                taskManager.removeTaskById(id);
                System.out.println("Удалена задача с id=" + id);
                h.sendResponseHeaders(200, 0);
            }
            case "POST" -> {
                String json = readText(h);
                if (json.isEmpty()) {
                    System.out.println("Body с задачей пустой. Указывается в теле запроса.");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                final Task task = gson.fromJson(json, Task.class);
                final Integer id = task.getId();
                if (id != null) {
                    taskManager.updateTask(task);
                    System.out.println("Обновлена задача с id=" + id);
                    h.sendResponseHeaders(200, 0);
                } else {
                    taskManager.addTask(task);
                    System.out.println("Создана задача с id=" + id);
                    final String response = gson.toJson(task);
                    sendText(h, response);
                }
            }
        }
    }

    private void handleEpic(HttpExchange h) throws IOException {
        final String query = h.getRequestURI().getQuery();
        switch (h.getRequestMethod()) {
            case "GET" -> {
                if (query == null) {
                    final Map<Integer, Epic> epics = taskManager.getEpics();
                    final String response = gson.toJson(epics);
                    System.out.println("Получены все эпики.");
                    sendText(h, response);
                    return;
                }
                String idParam = query.substring(3);
                final int id = Integer.parseInt(idParam);
                final Epic epic = taskManager.getEpicById(id);
                final String response = gson.toJson(epic);
                System.out.println("Получен эпик с id=" + id);
                sendText(h, response);
            }
            case "DELETE" -> {
                if (query == null) {
                    taskManager.removeAllEpics();
                    System.out.println("Удалены все эпики.");
                    h.sendResponseHeaders(200, 0);
                    return;
                }
                String idParam = query.substring(3);
                final int id = Integer.parseInt(idParam);
                taskManager.removeEpicById(id);
                System.out.println("Удален эпик с id=" + id);
                h.sendResponseHeaders(200, 0);
            }
            case "POST" -> {
                String json = readText(h);
                if (json.isEmpty()) {
                    System.out.println("Body с задачей пустой. Указывается в теле запроса.");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                final Epic epic = gson.fromJson(json, Epic.class);
                final Integer id = epic.getId();
                if (id != null) {
                    taskManager.updateEpic(epic);
                    System.out.println("Обновлен эпик с id=" + id);
                    h.sendResponseHeaders(200, 0);
                } else {
                    taskManager.addEpic(epic);
                    System.out.println("Создан эпик с id=" + id);
                    final String response = gson.toJson(epic);
                    sendText(h, response);
                }
            }
        }
    }

    private void handleSubtask(HttpExchange h) throws IOException {
        final String query = h.getRequestURI().getQuery();
        switch (h.getRequestMethod()) {
            case "GET" -> {
                if (query == null) {
                    final Map<Integer, Subtask> subtasks = taskManager.getSubtasks();
                    final String response = gson.toJson(subtasks);
                    System.out.println("Получены все подзадачи.");
                    sendText(h, response);
                    return;
                }
                String idParam = query.substring(3);
                final int id = Integer.parseInt(idParam);
                final Subtask subtask = taskManager.getSubtaskById(id);
                final String response = gson.toJson(subtask);
                System.out.println("Получена подзадача с id=" + id);
                sendText(h, response);
            }
            case "DELETE" -> {
                if (query == null) {
                    taskManager.removeAllSubtasks();
                    System.out.println("Удалены все подзадачи.");
                    h.sendResponseHeaders(200, 0);
                    return;
                }
                String idParam = query.substring(3);
                final int id = Integer.parseInt(idParam);
                taskManager.removeSubtaskById(id, taskManager.getEpicById(id));
                System.out.println("Удалена подзадача с id=" + id);
                h.sendResponseHeaders(200, 0);
            }
            case "POST" -> {
                String json = readText(h);
                if (json.isEmpty()) {
                    System.out.println("Body с задачей пустой. Указывается в теле запроса.");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                final Subtask subtask = gson.fromJson(json, Subtask.class);
                final Integer id = subtask.getId();
                if (id != null) {
                    taskManager.updateSubtask(subtask, taskManager.getEpicById(subtask.getId()));
                    System.out.println("Обновили подзадачу с id=" + id);
                    h.sendResponseHeaders(200, 0);
                } else {
                    taskManager.addSubtask(subtask);
                    System.out.println("Создали подзадачу с id=" + id);
                    final String response = gson.toJson(subtask);
                    sendText(h, response);
                }
            }
        }
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        httpServer.start();
    }

    public void stop() {
        System.out.println("Сервер остановлен.");
        httpServer.stop(0);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }
}
