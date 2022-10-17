package ru.yandex.practicum.history;

public class Node<E> {
    public E data;
    public Node<E> next;
    public Node<E> prev;

    public Node(Node<E> prev, E data, Node<E> next) {
        this.prev = null;
        this.data = data;
        this.next = null;
    }

}
