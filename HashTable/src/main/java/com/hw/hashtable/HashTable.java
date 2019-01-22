package com.hw.hashtable;

public class HashTable {

    private List[] table;
    private int size, cnt;

    /**
     * Creating hash table
     * @param size size of new table
     */
    public HashTable(int size){
        this.size = size;
        cnt = 0;
        table = new List[size];
        for(int i = 0; i < size; i++)
            table[i] = new List();
    }

    /**
     * Returns number of elements in the table
     */
    public int size(){
        return cnt;
    }

    private int getIndex(String key){
        return key.hashCode() % size;
    }


    /**
     * Tells whether there is an element with such a key in the table
     * @param key key on which value is searched
     */
    public boolean contains(String key){
        return (table[getIndex(key)].findValueByKey(key) != null);
    }

    /**
     * Returns value by given key
     * If there is no such key returns null
     */
    public String get(String key){
        return table[getIndex(key)].findValueByKey(key);
    }

    /**
     * Adds the value with by given key to table
     * @param key key to add value
     * @param value value added
     * @return value for given key before adding
     */

    public String put(String key, String value){
        List curList = table[getIndex(key)];
        String old_value = curList.findValueByKey(key);
        curList.replace(key, value);
        if (old_value == null)
            cnt++;
        return old_value;
    }

    /**
     * Removes value by given key
     * If there is no such key does nothing
     * @return result of getting value by the key before removing
     */

    public String remove(String key){
        List curList = table[getIndex(key)];
        String old_value = curList.findValueByKey(key);
        curList.delete(key);
        if (old_value != null)
            cnt++;
        return old_value;
    }

    /**
     * Removes all elements from the table
     */
    public void clear(){

        for(int i = 0; i < size; i++)
            table[i] = new List();
        cnt = 0;
    }
}
