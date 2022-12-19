package ru.yandex.practicum.kanban.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    protected URI url;
    protected String API_TOKEN;
    HttpClient client = HttpClient.newHttpClient();

    public KVTaskClient(String path) {
        this.url = URI.create(path);
    }

    public String register() {
        URI url = URI.create(this.url + "/register");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return API_TOKEN = response.body();
            } else {
                System.out.println("Не удалось получить API_TOKEN");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
        return "Некорректный API_TOKEN";
    }


    public void put(String key, String json)  {
        if (API_TOKEN == null) {
            System.out.println("API_TOKEN не присвоен.");
            return;
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/save" + key + "?API_TOKEN=" + API_TOKEN))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    public String load(String key) {
        if (API_TOKEN == null) {
            return "API_TOKEN не присвоен.";
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/load" + key + "?API_TOKEN=" + API_TOKEN))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());
            return response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
        return "Произошла ошибка при получении запроса.";
    }

}
