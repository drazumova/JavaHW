package com.kr.smartlist;

import java.util.*;

public class SmartList<E> extends AbstractList<E> implements List<E> {
    private int size;
    private Object list;

    public SmartList(){}

    public SmartList(Collection<E> all) {
        if (all.size() == 1) {
            list = all.toArray()[0];
        }
        if (all.size() < 6 && all.size() > 1) {
           list = all.toArray();
        }

        if (all.size() >= 6) {
            list = new ArrayList<E>(all);
        }
    }

    @Override
    public boolean add(E element) {
        if (size == 0) {
            list = element;
        }
        if (size == 1) {
            var newList = new Object[5];
            newList[0] = (E) list;
            newList[1] = element;
            list = newList;

        }
        if (size < 5 && size > 1) {
            var oldList = (Object[]) list;
            oldList[0] = (E) list;
            oldList[size] = element;
            list = oldList;
        }
        if (size == 5) {
            var newList = new ArrayList<E>();
            var oldList = (Object[]) list;
            for (int i = 0; i < 5; i++) {
                newList.add((E) oldList[i]);
            }
            newList.add(element);
            list = newList;
        }

        if (size >= 6) {
            var oldList = (ArrayList<E>) list;
            oldList.add(element);
        }
        size++;
        return true;
    }


    @Override
    public E get(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException();
        }

        if (size == 1) {
            return (E) list;
        }

        if (size < 6) {
            var oldList = (Object[]) list;
            return (E) oldList[index];
        }
        var oldList = (ArrayList<E>) list;

        return oldList.get(index);
    }

    @Override
    public E set(int index, E value) {
        E oldValue = get(index);
        if (index >= size) {
            throw new IndexOutOfBoundsException();
        }

        if (size == 1) {
            list = value;
        }
        if (size > 1 && size < 6) {
            var oldList = (Object[]) list;
            oldList[index] = value;
            list = oldList;
        }

        if (size >= 6) {
            var oldList = (ArrayList<E>) list;
            oldList.set(index, value);
        }
        return oldValue;
    }

    @Override
    public E remove(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException();
        }
        E oldValue = get(index);
        if (size == 1) {
            list = null;
        }

        if (size == 2) {
            var oldList = (Object[]) list;
            list = oldList[(index + 1)%2];
        }

        if (size > 2 && size < 6){
            var oldList = (Object[]) list;
            if (5 - index >= 0) {
                System.arraycopy(oldList, index + 1, oldList, index, 5 - index);
            }
            oldList[4] = null;
            list = oldList;
        }

        if (size == 6) {
            var oldList = (ArrayList<E>) list;
            oldList.remove(index);
            list = oldList.toArray();
        }

        if (size > 6) {
            var oldList = (ArrayList<E>) list;
            oldList.remove(index);
        }
        size--;
        return oldValue;
    }

    @Override
    public int size() {
        return size;
    }
}