package com.hw.trie;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.HashMap;

/**
 * String storage structure. Adds, removes and finds element for O(|element|).
 */
public class Trie implements Serializable {

    private TrieNode root;

    private class TrieNode implements Serializable {
        private HashMap<Character, TrieNode> edges;
        private int stringsCounter;
        private int endsCounter;
        private static final int listLength = Character.MAX_VALUE + 1;

        private TrieNode() {
            edges = new HashMap<>();
        }

        private TrieNode goByLetter(char character) {
            stringsCounter++;
            if (!edges.containsKey(character)) {
                edges.put(character, new TrieNode());
            }
            return edges.get(character);
        }

        public void serialize(@NotNull OutputStream out) throws IOException {
            out.write(stringsCounter);
            out.write(endsCounter);

            for (int i = 0; i < listLength; i++) {
                if (edges.containsKey((char) i)) {
                    out.write(1);
                } else {
                    out.write(0);
                }
            }
        }

        public void deserialize(@NotNull InputStream in) throws IOException {
            stringsCounter = in.read();
            endsCounter = in.read();

            for (int i = 0; i < listLength; i++) {
                int currentEdge = in.read();
                if (currentEdge == 1) {
                    edges.put((char) i, null);
                }
            }
        }
    }

    private TrieNode deserializeTree(InputStream in) throws IOException {
        var node = new TrieNode();
        node.deserialize(in);
        for (var letter : node.edges.keySet()) {
            node.edges.put(letter, deserializeTree(in));
        }
        return node;
    }

    private void serializeTree(TrieNode node, OutputStream out) throws IOException {
        node.serialize(out);
        for (var currentNode : node.edges.values()) {
            serializeTree(currentNode, out);
        }
    }

    public Trie() {
        root = new TrieNode();
    }

    /**
     * Writes trie to a stream
     * @param out the stream to write
     */
    @Override
    public void serialize(@NotNull OutputStream out) throws IOException {
        serializeTree(root, out);
    }

    /**
     * Reads trie from a stream
     * @param in the stream from which trie will be read
     */
    @Override
    public void deserialize(@NotNull InputStream in) throws IOException {
        root = deserializeTree(in);
    }

    private TrieNode getNodeByString(String string) {
        TrieNode current = root;
        var characterList = string.toCharArray();

        for (int i = 0; i < string.length() && current != null; i++) {
            current = current.edges.get(characterList[i]);
        }
        return current;
    }

    /**
     * Adds element to trie
     * @return is it a new element for the trie
     */
    public boolean add(@NotNull String element) {
        TrieNode current = root;
        boolean result = contains(element);
        var characterList = element.toCharArray();

        for (int i = 0; i < element.length(); i++) {
            current = current.goByLetter(characterList[i]);
        }
        current.stringsCounter++;
        current.endsCounter++;
        return !result;
    }

    /**
     * Tells does trie contain such element
     */
    public boolean contains(@NotNull String element) {
        TrieNode node = getNodeByString(element);
        return (node != null && node.endsCounter > 0);
    }

    /**
     * Removes element from the trie
     * @return did trie contain this element
     */
    public boolean remove(@NotNull String element) {
        if (!contains(element)) {
            return false;
        }

        TrieNode current = root;
        var characterList = element.toCharArray();

        for (int i = 0; i < element.length(); i++) {
            current.stringsCounter--;
            TrieNode next = current.edges.get(characterList[i]);
            if (next.stringsCounter == 1) {
                current.edges.remove(characterList[i]);
            }
            current = next;
        }
        current.stringsCounter--;
        current.endsCounter--;
        return true;
    }

    /**
     * Returns total strings in trie
     */
    public int size() {
        return root.stringsCounter;
    }

    /**
     * Returns count of strings in trie that have such prefix
     */
    public int howManyStartsWithPrefix(@NotNull String prefix) {
        TrieNode node = getNodeByString(prefix);
        if (node == null) {
            return 0;
        }
        return node.stringsCounter;
    }
}
