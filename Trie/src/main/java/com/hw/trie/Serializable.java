package com.hw.trie;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * The interface allows writing a structure  and reading from a stream.
 */
interface Serializable {
    void serialize(@NotNull OutputStream out) throws IOException;
    void deserialize(@NotNull InputStream in) throws IOException;
}
