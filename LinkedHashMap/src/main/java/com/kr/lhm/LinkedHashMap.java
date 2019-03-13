package com.kr.lhm;

import java.util.*;

import static java.lang.Math.abs;

/**
 * Structure contains values by unique keys. Values sorted in bucket due to key code.
 * @param <K> type of the key
 * @param <V> type of the value
 */
public class LinkedHashMap<K, V> extends AbstractMap<K, V> {

    private SimpleList<List<K, List.Node<K,V>>> table;
    private List<K, V> order;
    private int size;

    private class MapIterator implements Iterator<Entry<K,V>> {

        private List.Node<K, V> node;

        private MapIterator() {
            node = order.getHead();
        }

        @Override
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public Entry<K, V> next() {
            var value = node.getValue();
            node = node.getNext();
            return value;
        }
    }

    private class MyEntySet extends AbstractSet<Entry<K,V>> {

        @Override
        public Iterator<Entry<K, V>> iterator() {
            return new MapIterator();
        }

        @Override
        public int size() {
            return size;
        }
    }

    /**
     * Doublts table's size.
     */
    @SuppressWarnings("unchecked")
    public void resize() {
        int newSize = 2 * table.size();
        size = 0;
        var oldOrder = order;
        table = new SimpleList<>();
        order = new List<>();

        for (int i = 0; i < newSize; i++) {
            table.add(new List<>());
        }

        var node = oldOrder.getHead();
        while (node != null) {
            put(node.getValue().getKey(), node.getValue().getValue());
            node = node.getNext();
        }
    }

    /**
     * Creates new Linked Hask Map
     * @param capacity inital number of buckets
     */
    public LinkedHashMap(int capacity) {
        size = 0;
        table = new SimpleList<>();
        order = new List<>();
        for (int i = 0; i < capacity; i++) {
            table.add(new List<>());
        }
    }

    /**
     * Return current size of the table
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Tells is size of the table equals to 0
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Tells if the table contains given key
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean containsKey(Object key) {
        return table.get(bucketNumber(key)).containsKey((K) key);
    }

    /**
     * Tells if the table contains given value
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean containsValue(Object value) {
        return order.containsValue((V) value);
    }

    /**
     * Returns value by given key
     * If where no such key in the table, returns null
     */
    @SuppressWarnings("unchecked")
    @Override
    public V get(Object key) {
        var node = table.get(bucketNumber(key)).findValueByKey((K) key);
        if (node == null) {
            return null;
        }
        return node.getValue().getValue().getValue();
    }

    private int bucketNumber(Object key) {
        return abs(key.hashCode()) % table.size();
    }

    /**
     * Puts new pair (key, value) to the table
     * Returns old value by this key in the table
     * Or null if it was't in the table
     * If size will became bigger than half of capacity table will be resized
     */
    @Override
    public V put(K key, V value) {
        int number = bucketNumber(key);
        V result = null;
        var oldValue = table.get(number).findValueByKey(key);
        if (oldValue != null) {
            result = oldValue.getValue().getValue().getValue();
        }
        var entryNode = new MyEntry<>(key, value);
        order.add(entryNode);
        var entryTable = new MyEntry<>(key, order.findByKey(key));
        table.get(number).add(entryTable);
        if (result == null) {
            size++;
        }
        if (2 * size >= table.size()) {
            resize();
        }
        return result;
    }

    /**
     * Removes given key from the table
     * @return value by given key
     */
    @SuppressWarnings("unchecked")
    @Override
    public V remove(Object key) {
        int number = bucketNumber(key);
        List.Node<K, V> node = table.get(number).deleteByKey((K) key);
        V result = null;
        if (node != null) {
            result = node.getValue().getValue();
            node.delete();
            size--;
        }

        return result;
    }

    /**
     * Removes all key from the table
     */
    @Override
    public void clear() {
        size = 0;
        order = new List<>();
        for (int i = 0; i < table.size(); i++) {
            table.add(new List<>());
        }
    }

    /**
     * Returns set of all (key, value) pairs in the table
     */
    @Override
    public Set<Entry<K, V>> entrySet() {
        return new MyEntySet();
    }


}
