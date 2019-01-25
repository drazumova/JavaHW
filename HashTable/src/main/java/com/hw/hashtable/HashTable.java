package com.hw.hashtable;

import java.security.InvalidParameterException;

import static java.lang.Math.abs;

/**
 * Structure contains key-value pairs with unique keys.
 * Elements are divided into baskets according to the first elements hash.
 * If number of elements becomes more than half of it's size, the size will double.
 */
public class HashTable {

    private List[] table;
    private int size;
    private int counter;
    private int oldSize;

    /**
     * Creating hash table
     * @param size size of new table
     */
    public HashTable(int size) {
        oldSize = size;
        this.size = size;
        table = new List[size];

        for(int i = 0; i < size; i++) {
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
        List[] oldTable = table;
        size *= 2;
        counter = 0;
        table = new List[size];

        for(int i = 0; i < size; i++) {
            table[i] = new List();
        }



        for(int i = 0; i < size / 2; i++) {
            String currentKey, currentValue;
            List currentList = oldTable[i];

            while((currentKey = currentList.getHeadKey()) != null) {
                currentValue = currentList.getHeadValue();
                put(currentKey, currentValue);
                currentList.delete(currentKey);
            }
        }

    }

    private int getIndex(String key) {
        return abs(key.hashCode()) % size;
    }


    /**
     * Tells whether there is an element with such a key in the table
     * @param key key on which value is searched
     */
    public boolean contains(String key) {
        if (key == null) {
            throw new InvalidParameterException("Null parameter found");
        }

        return (table[getIndex(key)].findValueByKey(key) != null);
    }

    /**
     * Returns value by given key
     * If there is no such key returns null
     */
    public String get(String key) {
        if (key == null) {
            throw new InvalidParameterException("Null parameter found");
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
            throw new InvalidParameterException("Null parameter found");
        }

        List curList = table[getIndex(key)];
        String oldValue = curList.findValueByKey(key);
        curList.replace(key, value);

        if (oldValue == null) {
            counter++;
        }

        if (2 * counter >= size) {
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
            throw new InvalidParameterException("Null parameter found");
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
        table = new List[oldSize];
        for(int i = 0; i < oldSize; i++) {
            table[i] = new List();
        }

        size = oldSize;
        counter = 0;
    }
}
