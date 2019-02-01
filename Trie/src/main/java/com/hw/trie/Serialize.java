package com.hw.trie;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * The interface allows writing a structure  and reading from a stream.
 */
public interface Serialize {
    void serialize(OutputStream out) throws IOException;
    void deserialize(InputStream in) throws IOException;
}
