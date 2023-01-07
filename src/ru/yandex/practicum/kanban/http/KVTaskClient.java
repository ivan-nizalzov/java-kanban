package ru.yandex.practicum.kanban.http;

import ru.yandex.practicum.kanban.exception.ManagerSaveException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class KVTaskClient {
    private final String url;
    protected String API_TOKEN;
    private HttpClient client = HttpClient.newHttpClient();

    public KVTaskClient(int port) {
        url = "http://localhost:" + port + "/";
        API_TOKEN = register(url);
    }

    private String register(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "register"))
                    .GET()
                    .header("Content-Type", "application/json")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("Can't do '/register' request. Status code: " + response.statusCode());
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Can't do '/register' request. " + e.getMessage());
        }
    }

    public void put(String key, String value) {
        if (API_TOKEN == null) {
            System.out.println("API_TOKEN не присвоен, '/put' в KVTaskClient не может быть выполнен.");
            ;
        }
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "save/" + key + "?API_TOKEN=" + API_TOKEN))
                    .POST(HttpRequest.BodyPublishers.ofString(value, StandardCharsets.UTF_8))
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Can't do '/put' request. " + e.getMessage());
        }
    }

    public String load(String key) {
        if (API_TOKEN == null) {
            System.out.println("API_TOKEN не присвоен, '/load' в KVTaskClient не может быть выполнен.");
        }
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "load/" + key + "?API_TOKEN=" + API_TOKEN))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("Can't do '/load' request. Status code: " + response.statusCode());
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Can't do '/load' request. " + e.getMessage());
        }
    }
}