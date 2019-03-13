package com.kr.lhm;

import java.util.*;

import static java.lang.Math.abs;

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

    private void resize() {
        int newSize = 2 * table.size();
        var oldTable = table;
        size = 0;
        table = new SimpleList<>();
        order = new List<>();

        for (int i = 0; i < newSize; i++) {
            table.add(new List<>());
        }

        for (int i = 0; i < newSize / 2; i++) {
            K currentKey;
            var currentList = oldTable.get(i);

            while ((currentKey = (K) currentList.getHeadKey()) != null) {
                var currentValue = (V) currentList.getHeadValue();
                put(currentKey, currentValue);
                currentList.deleteByKey(currentKey);
            }
        }
    }


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
     * @return
     */
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return table.get(bucketNumber(key)).containsKey((K) key);
    }

    @Override
    public boolean containsValue(Object value) {
        return order.containsValue((V) value);
    }

    @Override
    public V get(Object key) {
        var node = table.get(bucketNumber(key)).findValueByKey((K) key);
        return node.getValue().getValue().getValue();
    }

    private int bucketNumber(Object key) {
        return abs(key.hashCode()) % table.size();
    }

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

        return result;
    }

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

    @Override
    public void clear() {
        size = 0;
        order = new List<>();
        for (int i = 0; i < table.size(); i++) {
            table.add(new List<>());
        }
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new MyEntySet();
    }


}
