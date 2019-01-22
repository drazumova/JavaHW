package com.hw.hashtable;

public class List{

    private class PairStrStr{
        private String key, value;

        private PairStrStr(String key, String value){
            this.key = key;
            this.value = value;
        }

        private boolean equalKey(String other){
            return (key.equals(other));
        }


    }

    private class Node{
        private PairStrStr value;
        private Node next, prev;

        private Node(PairStrStr value, Node next, Node prev){
            this.value = value;
            this.next = next;
            this.prev = prev;
        }

        private void setNext(Node next){
            this.next = next;
            next.prev = this;
        }

        private void setPrev(Node prev){
            this.prev = prev;
            prev.next = this;
        }

        private void delete(){
            if (next != null)
                next.setPrev(prev);
            if (prev != null)
                prev.setNext(next);
        }

    }

    private Node head, tail;

    public List(){
        head = tail = null;
    }

    /**
     * Adding a pair of two strings to the list
     * @param key first element of added pair
     * @param value second element of added pair
     */
    public void add(String key, String value){
        Node newNode = new Node (new PairStrStr (key, value), null, null);
        if (head == null)
            head = tail = newNode;
        else{
            tail.setNext(newNode);
            tail = newNode;
        }
    }

    private Node findByKey(String key){
        Node cur = head;
        while(cur != null && !cur.value.equalKey(key))
            cur = cur.next;

        return cur;
    }

    /**
     * Finds a pair having the given key as the first element
     * @return second element of found pair
     */
    public String findValueByKey(String key){
        Node cur = findByKey(key);
        if (cur == null)
            return null;
        return cur.value.value;
    }

    /**
     * Adds a pair to the list
     * If the list already has a pair with similar first element, сhanges the second
     * @param key first element of added pair
     * @param value second element
     */
    public void replace(String key, String value){
        Node cur = findByKey(key);
        if (cur == null)
            add(key, value);
        else
            cur.value.value = value;
    }


    /**
     * Removes a pair having given key as a first element
     * If there is no such pair does nothing
     */
    public void delete(String key){
        if (head == null)
            return;

        Node cur = findByKey(key);

        if (cur == null)
            return;

        if (cur == head)
            head = head.next;

        if (cur == tail)
            tail = tail.prev;

        cur.delete();
    }

}
