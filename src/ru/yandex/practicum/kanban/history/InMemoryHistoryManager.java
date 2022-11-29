package ru.yandex.practicum.kanban.history;

import ru.yandex.practicum.kanban.tasks.Task;
import java.util.*;
import static java.util.stream.Collectors.joining;

public class InMemoryHistoryManager implements HistoryManager {

    private Node<Task> head;
    private Node<Task> tail;
    private Node<Task> oldTail;
    private int sizeOfCustomLinkedList = 0;
    private final HashMap<Integer, Node> history = new HashMap<>();

    @Override
    public void addHistory(Task task) {
        Node<Task> node = linkLast(task);
        if (task == null) {
            return;
        } else if (history.containsKey(task.getId())) {
            removeNode(history.get(task.getId()));
        }
        history.put(task.getId(), node);
    }

    @Override
    public void removeHistoryById(int id) {
        history.remove(id);
        removeNode(history.get(id));
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public String historyToString() {
        List<Task> tempArrayList = getHistory();

        return tempArrayList.stream()
                .map(task -> Integer.toString(task.getId()))
                .collect(joining(","));
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> arrayListOfTasks = new ArrayList<>();

        for (Node<Task> node : history.values()) {
            if (node == null) {
                throw new NoSuchElementException();
            } else {
                arrayListOfTasks.add(node.data);
            }
        }
        return arrayListOfTasks;
    }

    private Node<Task> linkLast(Task element) {
        final Node<Task> newNode = new Node<>(null, element, null);

        if (head == null) {
            head = newNode;
            tail = head;
        } else {
            oldTail = tail;
            tail = newNode;
            oldTail.next = tail;
            tail.prev = oldTail;
        }
        ++sizeOfCustomLinkedList;
        return newNode;
    }

    private void removeNode(Node<Task> node) {
        if (node != null) {
            if (node.next == null) {
                node.prev.next = null;
                tail = node.prev;
            } else if (node.prev == null) {
                node.next.prev = null;
                head = node.next;
            } else {
                node.prev.next = node.next;
                node.next.prev = node.prev;
            }
        }
        --sizeOfCustomLinkedList;
    }
}
