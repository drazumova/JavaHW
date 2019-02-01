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


        @Override
        public void serialize(OutputStream out) throws IOException {

        }

        @Override
        public void deserialize(InputStream in) throws IOException {

        }
    }

    private TrieNode root;


    public Trie() {
        root = new TrieNode();
    }

}
