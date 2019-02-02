package com.hw.trie;

import org.jetbrains.annotations.NotNull;

import java.io.*;

/**
 * String storage structure. Adds, removes and finds element for O(|element|).
 */
public class Trie implements Serialize {

    private TrieNode root;

    private class TrieNode {
        private TrieNode[] edges;
        private int stringsCounter;
        private int endsCounter;
        private static final int listLength = Character.MAX_VALUE + 1;



        private TrieNode() {
            edges = new TrieNode[listLength];
        }

        private TrieNode goByLetter(char character) {
            stringsCounter++;
            if (edges[character] == null) {
                edges[character] = new TrieNode();
            }
            return edges[character];
        }

        private void serialize(OutputStream out) throws IOException {
            out.write(stringsCounter);
            out.write(endsCounter);

            for (TrieNode edge : edges) {
                if (edge != null) {
                    out.write(1);
                } else {
                    out.write(0);
                }
            }
        }

        private void deserialize(InputStream in) throws IOException {
            stringsCounter = in.read();
            endsCounter = in.read();

            for (int i = 0; i < edges.length; i++) {
                int currentEdge = in.read();
                if (currentEdge == 1) {
                    edges[i] = this;
                }
            }
        }

    }

    private TrieNode deserializeTree(InputStream in) throws IOException {
        var node = new TrieNode();
        node.deserialize(in);
        for (int i = 0; i < node.edges.length; i++) {
            if (node.edges[i] != null) {
                node.edges[i] = deserializeTree(in);
            }
        }
        return node;
    }

    private void serializeTree(TrieNode node, OutputStream out) throws IOException {
        node.serialize(out);
        for (int i = 0; i < node.edges.length; i++) {
            if (node.edges[i] != null) {
                serializeTree(node.edges[i], out);
            }
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
    public void serialize(OutputStream out) throws IOException {
        serializeTree(root, out);
    }

    /**
     * Reads trie from a stream
     * @param in the stream from which trie will be read
     */
    @Override
    public void deserialize(InputStream in) throws IOException {
        root = deserializeTree(in);
    }

    private TrieNode getNodeByString(String string) {
        TrieNode current = root;
        var characterList = string.toCharArray();

        for (int i = 0; i < string.length() && current != null; i++) {
            current = current.edges[characterList[i]];
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
            TrieNode next = current.edges[characterList[i]];
            if (next.stringsCounter == 1) {
                current.edges[characterList[i]] = null;
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
