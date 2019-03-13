package com.kr.lhm;


import java.util.*;

/**
 * Special double-linked list for Hash table with separate chaining.
 * Contains pairs of strings with unique first elements.
 */
public class SimpleList<E> extends AbstractList<E> {

    private class ListIterator implements java.util.ListIterator<E> {

        private Node currentNode;
        private int index;
        private boolean isRemoved;

        @Override
        public boolean hasNext() {
            return currentNode.next != head;
        }

        @Override
        public E next() {
            var value = currentNode.next.value;
            currentNode = currentNode.next;
            index++;
            isRemoved = false;
            return value;

        }

        @Override
        public boolean hasPrevious() {
            return  (currentNode.previous != head);
        }

        @Override
        public E previous() {
            var value = currentNode.previous.value;
            currentNode = currentNode.previous;
            index--;
            isRemoved = false;
            return value;

        }

        @Override
        public int nextIndex() {
            return index;
        }

        @Override
        public int previousIndex() {
            return index - 1;
        }

        @Override
        public void remove() {
            if (isRemoved)
                throw new IllegalStateException();

            if (currentNode == head)
                throw new IndexOutOfBoundsException();
            currentNode.delete();
            isRemoved = true;
        }

        @Override
        public void set(E e) {
            if (currentNode.next == head)
                throw new IndexOutOfBoundsException();
            currentNode.value = e;
        }

        @Override
        public void add(E e) {
            var node = new Node(e, currentNode, currentNode.next);
            currentNode.next = node;
            currentNode.next.previous = node;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E get(int index) {
        var current = findByIndex(index);
        if (index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return current.value;
    }

    @Override
    public E set(int index, E element) {
        var current = findByIndex(index);
        if (index >= size) {
            throw new IndexOutOfBoundsException();
        }
        E oldValue = current.value;
        current.value = element;
        return oldValue;
    }

    @Override
    public void add(int index, E element) {
        var elem = findByIndex(index);
        var newNode = new Node(element, elem.previous, elem);

        if (index > size())
            throw new IndexOutOfBoundsException();

        elem.previous.next = newNode;
        size++;
        elem.previous = newNode;
    }

    @Override
    public E remove(int index) {
        var current = findByIndex(index);
        if (index >= size) {
            throw new IndexOutOfBoundsException();
        }
        current.delete();
        size--;
        return current.value;
    }

    private Node findByIndex(int index) {
        var current = head.next;
        while(index != 0){
            index--;
            if (current != null)
                current = current.next;
        }
        return current;
    }


    private class Node {
        private E value;
        private Node next;
        private Node previous;

        private Node(E value, Node previous, Node next) {
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

    private Node head;
    private int size;

    public SimpleList(){
        head = new Node(null, null, null);
        head.next = head;
        head.previous = head;
    }


}