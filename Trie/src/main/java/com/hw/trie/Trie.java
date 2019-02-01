package com.hw.trie;

import java.io.*;

interface Serialize{
    void serialize(OutputStream out) throws IOException;
    void deserialize(InputStream in) throws IOException;
}

/**
 * String storage structure. Adds, removes and finds element for O(|element|).
 */
public class Trie implements Serialize{

    @Override
    public void serialize(OutputStream out) throws IOException {

    }

    @Override
    public void deserialize(InputStream in) throws IOException {

    }

    private class TrieNode implements Serialize{
        private TrieNode[] edges;
        private int stringsCounter;
        private int endsCounter;
        private static final int listLenght = Character.MAX_VALUE + 1;


        private TrieNode() {
            edges = new TrieNode[listLenght];
        }

        private TrieNode goByLetter(char character) {
            stringsCounter++;
            if (edges[character] == null) {
                edges[character] = new TrieNode();
            }
            return edges[character];
        }


        @Override
        public void serialize(OutputStream out) throws IOException {
            out.write(stringsCounter);
            out.write(endsCounter);

            for (int i = 0; i < edges.length; i++) {
                if (edges[i] != null) {
                    out.write(1);
                } else {
                    out.write(0);
                }
            }
        }

        @Override
        public void deserialize(InputStream in) throws IOException {
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

    private TrieNode root;


    public Trie() {
        root = new TrieNode();
    }

}
