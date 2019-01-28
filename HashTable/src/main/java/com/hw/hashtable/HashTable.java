package com.hw.hashtable;


import static java.lang.Math.abs;

/**
 * Structure contains key-value pairs with unique keys.
 * Elements are divided into baskets according to the first elements hash.
 * If number of elements becomes more than half of it's size, the size will double.
 */
public class HashTable {

    private List[] table;
    private int initialSize;
    private int counter;

    /**
     * Creating hash table
     * @param size initial size of new table
     */
    public HashTable(int size) {
        initialSize = size;
        table = new List[size];

        for (int i = 0; i < size; i++) {
            table[i] = new List();
        }
    }

    /**
     * Returns number of elements in the table
     */
    public int size() {
        return counter;
    }

    private void resize() {
        int newSize = 2 * table.length;
        List[] oldTable = table;
        counter = 0;
        table = new List[newSize];

        for (int i = 0; i < newSize; i++) {
            table[i] = new List();
        }

        for (int i = 0; i < newSize / 2; i++) {
            String currentKey;
            String currentValue;
            List currentList = oldTable[i];

            while ((currentKey = currentList.getHeadKey()) != null) {
                currentValue = currentList.getHeadValue();
                put(currentKey, currentValue);
                currentList.delete(currentKey);
            }
        }
    }

    private int getIndex(String key) {
        return abs(key.hashCode()) % table.length;
    }

    /**
     * Tells whether there is an element with such a key in the table
     * @param key key on which value is searched
     */
    public boolean contains(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Null parameter found");
        }

        return (table[getIndex(key)].findValueByKey(key) != null);
    }

    /**
     * Returns value by given key
     * If there is no such key returns null
     */
    public String get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Null parameter found");
        }
        return table[getIndex(key)].findValueByKey(key);
    }

    /**
     * Adds the value with by given key to table
     * @param key key to add value
     * @param value value added
     * @return value for given key before adding
     */
    public String put(String key, String value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Null parameter found");
        }

        List curList = table[getIndex(key)];
        String oldValue = curList.findValueByKey(key);
        curList.replace(key, value);

        if (oldValue == null) {
            counter++;
        }

        if (2 * counter >= table.length) {
            resize();
        }

        return oldValue;
    }

    /**
     * Removes value by given key
     * If there is no such key does nothing
     * @return result of getting value by the key before removing
     */
    public String remove(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Null parameter found");
        }

        List curList = table[getIndex(key)];
        String oldValue = curList.findValueByKey(key);
        curList.delete(key);

        if (oldValue != null) {
            counter--;
        }

        return oldValue;
    }

    /**
     * Removes all elements from the table
     */
    public void clear() {
        table = new List[initialSize];
        for (int i = 0; i < initialSize; i++) {
            table[i] = new List();
        }

        counter = 0;
    }
}
