package com.kr.lhm;

import com.kr.lhm.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class List<K, V> {

    public static class Node<K, V> {
        private MyEntry<K, V> value;
        private Node<K, V> next;
        private Node<K, V> previous;

        public Node(MyEntry<K, V> value, Node<K, V> next, Node<K, V> previous) {
            this.value = value;
            this.next = next;
            this.previous = previous;
        }

        public MyEntry<K, V> getValue() {
            return value;
        }

        public Node<K,V> getNext() {
            return next;
        }


        protected void setNext(Node<K, V> next) {
            this.next = next;

            if (next != null) {
                next.previous = this;
            }
        }

        protected void setPrev(Node<K, V> previous) {
            this.previous = previous;
            if (previous != null) {
                previous.next = this;
            }
        }

        protected void delete() {
            if (next != null) {
                next.setPrev(previous);
            }
            if (previous != null) {
                previous.setNext(next);
            }
        }

    }

    private Node<K, V> head, tail;


    /**
     * Adding a pair of two strings to the list
     * @param key first element of added pair
     * @param value second element of added pair
     */
    public void add(@NotNull MyEntry<K, V> value) {

        var newNode = new Node<>(value, null, null);

        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.setNext(newNode);
            tail = newNode;
        }
    }

    protected Node<K, V> getHead() {
        return  head;
    }

    /**
     * Returns key of first element in list
     * If list is empty returns null
     */
    public K getHeadKey() {
        if (head == null) {
            return null;
        }
        return head.value.getKey();
    }

    /**
     * Returns value of first element in list
     * If list is empty returns null
     */
    public V getHeadValue() {
        if (head == null) {
            return null;
        }
        return head.value.getValue();
    }

    protected Node<K, V> findByKey(K key) {

        Node<K, V> current = head;
        while (current != null && !current.value.getKey().equals(key)) {
            current = current.next;
        }

        return current;
    }

    /**
     * Finds a pair having the given key as the first element
     * @return second element of found pair
     */
    public MyEntry<K, V> findValueByKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Null parameter found");
        }

        Node<K, V> current = findByKey(key);
        if (current == null) {
            return null;
        }
        return current.value;
    }

    /**
     * Removes a pair having given key as a first element
     * If there is no such pair does nothing
     */
    public void delete(@NotNull MyEntry<K, V> value) {
        if (head == null) {
            return;
        }

        Node<K, V> current = findByKey(value.getKey());

        if (current == null) {
            return;
        }

        if (current == head) {
            head = head.next;
        }

        if (current == tail) {
            tail = tail.previous;
        }

        current.delete();
    }

    /**
     * Deletes pair with given key from the list
     * @return old value
     */
    public V deleteByKey(@NotNull K key) {
        if (head == null) {
            return null;
        }

        Node<K, V> current = findByKey(key);

        if (current == null) {
            return null;
        }

        if (current == head) {
            head = head.next;
        }

        if (current == tail) {
            tail = tail.previous;
        }

        current.delete();
        return current.value.getValue();
    }

    /**
     * Tells if list contains given key
     */
    public boolean containsKey(K key) {
        var current = findByKey(key);
        return current != null;
    }


    /**
     * Tells if list contains given value
     */
    public boolean containsValue(V value) {
        Node<K, V> current = head;
        while (current != null && !current.value.getValue().equals(value)) {
            current = current.next;
        }

        return current != null;
    }

}
