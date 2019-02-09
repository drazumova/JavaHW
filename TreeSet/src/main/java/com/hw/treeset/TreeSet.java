package com.hw.treeset;

import org.jetbrains.annotations.*;

import java.util.*;

/**
 * Structure contains unique objects via unbalanced binary search tree.
 */
public class TreeSet<E> extends AbstractSet<E> implements MyTreeSet<E> {

    private Node<E> root;
    private int size;
    private Comparator<E> comparator;
    private int currentVersion;

    private static final class Node<E> {
        @NotNull private E value;
        private Node<E> parent;
        private Node<E> left;
        private Node<E> right;
        private Node(E value, Node<E> parent, Node<E> left, Node<E> right) {
            this.value = value;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }

        private Node<E> minimum() {
            Node<E> currentNode = this;

            while (currentNode.left != null) {
                currentNode = currentNode.left;
            }

            return currentNode;
        }

        private Node<E> maximum() {
            Node<E> currentNode = this;

            while (currentNode.right != null) {
                currentNode = currentNode.right;
            }

            return currentNode;
        }

        private Node<E> next() {
            if (right != null) {
                return right.minimum();
            }

            Node<E> currentNode = this;
            Node<E> parentNode = parent;

            while (parentNode != null && currentNode == parentNode.right) {
                currentNode = parentNode;
                parentNode = parentNode.parent;
            }

            return parentNode;
        }

        private Node<E> previous() {
            if (left != null) {
                return left.maximum();
            }

            Node<E> currentNode = this;
            Node<E> parentNode = parent;

            while (parentNode != null && currentNode == parentNode.left) {
                currentNode = parentNode;
                parentNode = parentNode.parent;
            }

            return parentNode;
        }


    }

    private final class TreeSetIterator implements Iterator<E> {
        private Node<E> last;
        private final int version;

        private TreeSetIterator(Node<E> start, int version) {
            last = start;
            this.version = version;
        }

        private void checkVersion() throws ConcurrentModificationException{
            if (currentVersion > version) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public boolean hasNext() {
            checkVersion();
            return last != null;
        }

        @Override
        public E next() {
            checkVersion();
            E value = last.value;
            if (last == null) {
                throw new NoSuchElementException();
            }
            last = last.next();
            return value;
        }

    }

    private final class TreeSetReversedIterator implements Iterator<E> {
        private Node<E> next;
        private final int version;

        private void checkVersion() throws ConcurrentModificationException{
            if (currentVersion > version) {
                throw new ConcurrentModificationException();
            }
        }

        private TreeSetReversedIterator(Node<E> start, int version) {
            next = start;
            this.version = version;
        }

        @Override
        public boolean hasNext() {
            checkVersion();
            return next != null;
        }

        @Override
        public E next() {
            checkVersion();
            E value = next.value;
            if (next == null) {
                throw new NoSuchElementException();
            }
            next = next.previous();
            return value;
        }

    }

    private final class ReversedTreeSet extends AbstractSet<E> implements MyTreeSet<E>{
        @NotNull private final TreeSet<E> initalSet;

        ReversedTreeSet(TreeSet<E> set) {
            initalSet = set;
        }

        @Override
        public Iterator<E> descendingIterator() {
            return initalSet.iterator();
        }

        @Override
        public MyTreeSet<E> descendingSet() {
            return initalSet;
        }

        @Override
        public E first() {
            return initalSet.last();
        }

        @Override
        public E last() {
            return initalSet.first();
        }

        @Override
        public E lower(E e) {
            return initalSet.higher(e);
        }

        @Override
        public E floor(E e) {
            return initalSet.ceiling(e);
        }

        @Override
        public E ceiling(E e) {
            return initalSet.floor(e);
        }

        @Override
        public E higher(E e) {
            return initalSet.lower(e);
        }

        @Override
        public Iterator<E> iterator() {
            return initalSet.descendingIterator();
        }

        @Override
        public int size() {
            return initalSet.size();
        }

        @Override
        public boolean add(E element) {
            return initalSet.add(element);
        }

        @Override
        public boolean remove(Object element) {
            return initalSet.remove(element);
        }

        @Override
        public boolean contains(Object element) {
            return initalSet.contains(element);
        }
    }

    /**
     * Create new set using Comparator.naturalOrder.
     */
    TreeSet() {
//        comparator = (Comparator<E>) Comparator.naturalOrder();
    }

    /**
     * Create new set using given comparator.
     */
    TreeSet(@NotNull Comparator<E> comparator) {
        this.comparator = comparator;
    }

    private int compare(@NotNull E first, E second) {
        if (comparator == null) {
            var newFirst = (Comparable<E>) first;
            return newFirst.compareTo(second);
        }
        return comparator.compare(first, second);
    }

    @Nullable
    private Node<E> findCloseNode(Node<E> node, E element) {
        if (node == null) {
            return null;
        }
        if (compare(node.value, element) == 0) {
            return node;
        }

        if (compare(node.value, element) < 0) {
            if (node.right == null) {
                return node;
            } else {
                return findCloseNode(node.right, element);
            }
        } else {
            if (node.left == null) {
                return node;
            } else {
                return findCloseNode(node.left, element);
            }
        }
    }

    private Node<E> removeNode(@NotNull Node<E> node, E value) {
        if (compare(node.value, value) == 0) {
            if (node.right == null && node.left == null) {
                return null;
            }
            if (node.right == null) {
                node.left.parent = node.parent;
                if (node == node.parent.left) {
                    node.parent.left = node.left;
                } else {
                    node.parent.right = node.left;
                }
            } else if (node.left == null) {
                node.right.parent = node.parent;
                if (node == node.parent.left) {
                    node.parent.left = node.right;
                } else {
                    node.parent.right = node.right;
                }
            } else {
                node.value = node.left.maximum().value;
                node.left = removeNode(node.left, node.value);
                node.left.parent = node;
            }
        } else if (compare(node.value, value) > 0){
            node.left = removeNode(node.left, value);
            node.left.parent = node;
        } else {
            node.right = removeNode(node.right, value);
            node.right.parent = node;
        }
        return node;
    }

    /**
     * Removes given element.
     * If where no such element is the set, do nothing.
     * @return did set change after removing
     */
    @Override
    public boolean remove(@NotNull Object element) {
        if (!contains(element)) {
            return false;
        }
        root = removeNode(root, (E) element);
        size--;
        currentVersion++;
        return true;
    }

    /**
     * Tells is this element is the set or not
     */
    @Override
    public boolean contains(@NotNull Object element) {
        return element.equals(getValue(findCloseNode(root, (E) element)));
    }

    private Node<E> addNode(Node<E> node, E value) {
        if (node == null) {
            return new Node<E>(value, null, null, null);
        }
        if (compare(node.value, value) > 0) {
            node.left = addNode(node.left, value);
            node.left.parent = node;
        } else {
            node.right = addNode(node.right, value);
            node.right.parent = node;
        }
        return node;
    }

    /**
     * Adds given elements to set.
     * If set already contains such element do nothing.
     * @return did set change after adding
     */
    @Override
    public boolean add(@NotNull E element) {
        if (contains(element)) {
            return false;
        }
        root = addNode(root, element);
        size++;
        currentVersion++;
        return true;
    }

    /**
     * Returns count of elements in the set.
     */
    @Override
    public int size() {
        return size;
    }

    private E getValue(Node<E> node) {
        if (node == null) {
            return null;
        }

        return node.value;
    }

    /**
     * Returns an iterator over the elements in the set, in ascending order.
     * After set changing iterator becomes invalid
     * and throws ConcurrentModificationException when calling methods.
     */
    @Nullable
    @Override
    public Iterator<E> iterator() {
        if (root == null) {
            return new TreeSetIterator(null, currentVersion);
        }
        return new TreeSetIterator(root.minimum(), currentVersion);
    }

    /**
     * Returns an iterator over the elements in the set, in descending order.
     * After set changing iterator becomes invalid
     * and throws ConcurrentModificationException when calling methods.
     */
    @Nullable
    @Override
    public Iterator<E> descendingIterator() {
        if (root == null) {
            return new TreeSetReversedIterator(null, currentVersion);
        }
        return new TreeSetReversedIterator(root.maximum(), currentVersion);
    }

    /**
     * Returns set with inversed elements order.
     * Dont copy elements, will change in case of inital set changing.
     * And in case of descendind set changing inital set will change.
     */
    @Override
    public MyTreeSet<E> descendingSet() {
        return new ReversedTreeSet(this);
    }

    /**
     * Returns the least element in the set.
     */
    @Nullable
    @Override
    public E first() {
        if (root == null) {
            return null;
        }
        return getValue(root.minimum());
    }

    /**
     * Returns the greatest elements in the set.
     */
    @Nullable
    @Override
    public E last() {
        if (root == null) {
            return null;
        }
        return getValue(root.maximum());
    }

    /**
     * Returns the greatest element in the set, that is lower than given.
     * If where no such elements returns null.
     */
    @Nullable
    @Override
    public E lower(@NotNull E e) {
        Node<E> node = findCloseNode(root, e);
        if (node == null) {
            return null;
        }
        if (compare(node.value, e) >= 0) {
            return getValue(node.previous());
        }
        return node.value;

    }

    /**
     * Returns the greatest element in the set, that is lower than given or equal.
     * If where no such elements returns null.
     */
    @Nullable
    @Override
    public E floor(@NotNull E e) {
        Node<E> node = findCloseNode(root, e);
        if (node == null) {
            return null;
        }
        if (compare(node.value, e) <= 0) {
            return node.value;
        }
        return getValue(node.previous());
    }

    /**
     * Returns the least element in the set, that greater than given or equal.
     * If where no such elements returns null.
     */
    @Nullable
    @Override
    public E ceiling(@NotNull E e) {
        Node<E> node = findCloseNode(root, e);
        if (node == null) {
            return null;
        }
        if (compare(node.value, e) >= 0) {
            return node.value;
        }
        return getValue(node.next());
    }

    /**
     * Returns the least element in the set, that greater than given.
     * If where no such elements returns null.
     */
    @Nullable
    @Override
    public E higher(@NotNull E e) {
        Node<E> node = findCloseNode(root, e);
        if (node == null) {
            return null;
        }
        if (compare(node.value, e) > 0) {
            return node.value;
        }
        return getValue(node.next());
    }
}