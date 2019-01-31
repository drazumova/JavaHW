package com.hw.hashtable;


/**
 * Special double-linked list for Hash table with separate chaining.
 * Contains pairs of strings with unique first elements.
 */
public class List {

    private class PairStrStr {
        private String key;
        private String value;

        private PairStrStr(String key, String value) {
            this.key = key;
            this.value = value;
        }

        private boolean equalKey(String other) {
            return key.equals(other);
        }
    }

    private class Node {
        private PairStrStr value;
        private Node next;
        private Node previous;

        private Node(PairStrStr value, Node next, Node previous) {
            this.value = value;
            this.next = next;
            this.previous = previous;
        }

        private void setNext(Node next) {
            this.next = next;

            if (next != null) {
                next.previous = this;
            }
        }

        private void setPrev(Node previous) {
            this.previous = previous;
            if (previous != null) {
                previous.next = this;
            }
        }

        private void delete() {
            if (next != null) {
                next.setPrev(previous);
            }
            if (previous != null) {
                previous.setNext(next);
            }
        }

    }

    private Node head, tail;


    /**
     * Adding a pair of two strings to the list
     * @param key first element of added pair
     * @param value second element of added pair
     */
    public void add(String key, String value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Null parameter found");
        }

        var newNode = new Node(new PairStrStr (key, value), null, null);

        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.setNext(newNode);
            tail = newNode;
        }
    }

    /**
     * Returns key of first element in list
     * If list is empty returns null
     */
    public String getHeadKey() {
        if (head == null) {
            return null;
        }
        return head.value.key;
    }

    /**
     * Returns value of first element in list
     * If list is empty returns null
     */
    public String getHeadValue() {
        if (head == null) {
            return null;
        }
        return head.value.value;
    }

    private Node findByKey(String key) {

        Node current = head;
        while (current != null && !current.value.equalKey(key)) {
            current = current.next;
        }

        return current;
    }

    /**
     * Finds a pair having the given key as the first element
     * @return second element of found pair
     */
    public String findValueByKey(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Null parameter found");
        }

        Node current = findByKey(key);
        if (current == null) {
            return null;
        }
        return current.value.value;
    }

    /**
     * Adds a pair to the list
     * If the list already has a pair with similar first element, сhanges the second
     * @param key first element of added pair
     * @param value second element
     */
    public void replace(String key, String value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Null parameter found");
        }

        Node current = findByKey(key);
        if (current == null) {
            add(key, value);
        } else {
            current.value.value = value;
        }
    }

    /**
     * Removes a pair having given key as a first element
     * If there is no such pair does nothing
     */
    public void delete(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Null parameter found");
        }

        if (head == null) {
            return;
        }

        Node current = findByKey(key);

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

}
