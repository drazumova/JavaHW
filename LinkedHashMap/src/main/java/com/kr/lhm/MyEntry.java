package com.kr.lhm;

import org.jetbrains.annotations.*;

import java.util.*;

public class MyEntry<K, V> implements Map.Entry<K, V>{

    private @NotNull K key;
    private @NotNull V value;

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        var other = (MyEntry<K, V>) o;
        return other.getValue().equals(value) && other.getKey().equals(key);
    }

    public MyEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    private boolean equalKey(String other) {
            return key.equals(other);
        }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V oldValue = this.value;
        this.value = value;
        return oldValue;
    }
}
