package com.kr.md5;

import java.io.*;
import java.nio.charset.*;
import java.security.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Class for counting check-sum from the task.
 */
public class MD {

    public static final int BUFFER_SIZE = 4096; //as in example
    private final ForkJoinPool pool;

    /**
     * Creates new instance
     * Creates new ForkJoinPool
     */
    public MD() {
        pool = new ForkJoinPool();
    }

    private byte[] countFile(File file, InputStream in, MessageDigest md) throws IOException {
        try (DigestInputStream stream = new DigestInputStream(in, md)) {
            var buffer = new byte[BUFFER_SIZE];
            while (stream.read() > -1) {
            }
            return stream.getMessageDigest().digest();
        }
    }

    private byte[] commonCount(File file, boolean type) throws IOException, NoSuchAlgorithmException {
        var md = MessageDigest.getInstance("MD5");

        if (file.isDirectory()) {
            var builder = new StringBuilder(BUFFER_SIZE);
            builder.append(file.getName());
            if (type) {
                var taks = new ArrayList<ForkJoinTask<byte[]>>();

                for (var inner : file.listFiles()) {
                    taks.add(pool.submit(() -> {return commonCount(inner, true);}));
                }

                for (int i = 0; i < file.listFiles().length; i++) {
                    var result = taks.get(i).join();
                    builder.append(Arrays.toString(result));
                }
            } else {

                for (var inner : file.listFiles()) {
                    builder.append(Arrays.toString(commonCount(inner, false)));
                }
            }

            return countFile(file, new ByteArrayInputStream(builder.toString().getBytes(StandardCharsets.UTF_8)), md);
        }

        if (file.isFile()) {
            return countFile(file, new FileInputStream(file), md);
        }

        return new byte[0];
    }

    /**
     * Counts sum using main thread
     */
    public byte[] simpleCount(File file) throws NoSuchAlgorithmException, IOException {
        return commonCount(file, false);
    }

    /**
     * Counts sum using fork/join method
     */
    public byte[] forkJoinCount(File file) throws NoSuchAlgorithmException, IOException {
        return commonCount(file, true);
    }

}
