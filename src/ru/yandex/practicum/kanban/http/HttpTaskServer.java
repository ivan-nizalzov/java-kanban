package ru.yandex.practicum.kanban.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.kanban.manager.FileBackedTaskManager;
import ru.yandex.practicum.kanban.manager.Managers;
import ru.yandex.practicum.kanban.tasks.Epic;
import ru.yandex.practicum.kanban.tasks.Subtask;
import ru.yandex.practicum.kanban.tasks.Task;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static Gson gson = Managers.getGson();
    static HttpServer httpServer;

    public HttpTaskServer() throws IOException {
        this.httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
    }

    public void start() {
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("HTTP-сервер остановлен!");
    }

    public static class TasksHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            /*
            Здесь необходимо создать для каждого метода интерфейса TaskManager соответствующий ему эндпоинт.
             */
            FileBackedTaskManager manager = Managers.getDefaultFileBackedManager();
            FileBackedTaskManager.loadFromLife(new File("resources/back-up.csv"));

            String path = httpExchange.getRequestURI().getPath();
            String method = httpExchange.getRequestMethod();

            String response = "";
            int rCode = 000;

            switch (method) {

                case "GET": // Обработка эндпоинта на получение задачи
                    if (Pattern.matches("^/tasks$", path)) {
                        httpExchange.sendResponseHeaders(200, 0);
                        response = gson.toJson(manager.getEpics().values());
                        rCode = 200;

                    } else if (Pattern.matches("^/tasks/task$", path)
                            && httpExchange.getRequestURI().getQuery() != null) {
                        int id = parserId(httpExchange.getRequestURI().getQuery());
                        response = gson.toJson(manager.getTaskById(id));
                        rCode = 200;

                    } else if (Pattern.matches("^/tasks/task$", path)) {
                        response = gson.toJson(manager.getTasks().values());
                        rCode = 200;

                    } else if (Pattern.matches("^/tasks/subtask/epic$", path)
                            && httpExchange.getRequestURI().getQuery() != null) {
                        response =  gson.toJson(manager.getEpics().values());
                        rCode = 200;

                    } else if (Pattern.matches("^/tasks/epic$", path)
                            && httpExchange.getRequestURI().getQuery() != null) {
                        response = gson.toJson(manager.getEpics().values());
                        rCode = 200;

                    } else if (Pattern.matches("^/tasks/epic$", path)) {
                        response = gson.toJson(manager.getEpics().values());
                        rCode = 200;

                    } else if (Pattern.matches("^/tasks/subtask$", path)
                            && httpExchange.getRequestURI().getQuery() != null) {
                        response = gson.toJson(manager.getEpics().values());
                        rCode = 200;

                    } else if(Pattern.matches("^/tasks/subtask$", path)) {
                        response = gson.toJson(manager.getSubtasks().values());
                        rCode = 200;

                    } else if (Pattern.matches("^/tasks/history$", path)) {
                        response = gson.toJson(manager.getHistory());
                        rCode = 200;

                    } else if (Pattern.matches("^/tasks/priority$", path)) {
                        response = gson.toJson(manager.getPrioritizedTasks());
                        rCode = 200;
                    }
                    break;

                case "POST": // Обработка эндпоинта на создание и изменение задачи
                    if (Pattern.matches("^/tasks/task$", path)) {
                        InputStream inputStream = httpExchange.getRequestBody();
                        String taskBody = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        Task task = gson.fromJson(taskBody, Task.class);
                        manager.addTask(task);
                        rCode = 201;

                    } else if (Pattern.matches("^/tasks/epic$", path)) {
                        InputStream inputStream = httpExchange.getRequestBody();
                        String taskBody = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        Epic epic = gson.fromJson(taskBody, Epic.class);
                        manager.addEpic(epic);
                        rCode = 201;

                    } else if (Pattern.matches("^/tasks/subtask$", path)) {
                        InputStream inputStream = httpExchange.getRequestBody();
                        String taskBody = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        Subtask subtask = gson.fromJson(taskBody, Subtask.class);
                        manager.addSubtask(subtask);
                        rCode = 201;
                    }
                    break;

                case "DELETE": // Обработка эндпоинта на удаление задачи
                    if (Pattern.matches("^/tasks/task$", path)
                            && httpExchange.getRequestURI().getQuery() != null) {

                        int id = parserId(httpExchange.getRequestURI().getQuery());;
                        manager.removeTaskById(id);
                        response = gson.toJson("Таск c id:" + id + " удален.");
                        rCode = 200;

                    } else if (Pattern.matches("^/tasks/task$", path)) {
                        manager.removeAllTasks();
                        response = gson.toJson("Все такси удалены.");
                        rCode = 200;

                    } else if (Pattern.matches("^/tasks/epic$", path)
                            && httpExchange.getRequestURI().getQuery() != null) {

                        int id = parserId(httpExchange.getRequestURI().getQuery());
                        manager.removeEpicById(id);
                        response = gson.toJson("Эпик c id:" + id + " удален.");
                        rCode = 200;

                    } else if (Pattern.matches("^/tasks/epic$", path)) {
                        manager.removeAllEpics();
                        response = gson.toJson("Все эпики с сабтасками удалены.");
                        rCode = 200;

                    } else if (Pattern.matches("^/tasks/subtask$", path)
                            && httpExchange.getRequestURI().getQuery() != null) {

                        int id = parserId(httpExchange.getRequestURI().getQuery());
                        manager.removeSubtaskById(id, manager.getEpicById(id)); //
                        response = gson.toJson("Сабтаск c id:" + id + " удален.");
                        rCode = 200;
                    }
                    break;

                case "PUT":
                    if (Pattern.matches("^/tasks/task$", path)) {
                        InputStream inputStream = httpExchange.getRequestBody();
                        String taskBody = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        Task task = gson.fromJson(taskBody, Task.class);
                        manager.updateTask(task);
                        rCode = 202;

                    } else if (Pattern.matches("^/tasks/epic$", path)) {
                        InputStream inputStream = httpExchange.getRequestBody();
                        String taskBody = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        Epic epic = gson.fromJson(taskBody, Epic.class);
                        manager.updateEpic(epic);
                        rCode = 202;

                    } else if (Pattern.matches("^/tasks/subtask$", path)) {
                        InputStream inputStream = httpExchange.getRequestBody();
                        String taskBody = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        Subtask subtask = gson.fromJson(taskBody, Subtask.class);
                        Epic epic = gson.fromJson(taskBody, Epic.class);
                        manager.updateSubtask(subtask, epic);
                        rCode = 202;

                    } else {
                        rCode = 404;
                        response = gson.toJson("Такой команды нет.");
                    }
                    break;
            }

            httpExchange.sendResponseHeaders(rCode, response.getBytes().length);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    private static int parserId(String query) {
        String[] queryArray = query.split("=");
        int id = Integer.parseInt(queryArray[1]);
        return id;
    }
}
