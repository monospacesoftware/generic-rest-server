package com.monospace.server.rest;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DataStore {

    private final Map<String, DataWriter> writers = new ConcurrentHashMap<>();

    @Async
    public void save(String method, String uri, String data) {
        String path = String.format("%s/%s", method, uri);
        writers.computeIfAbsent(path, DataWriter::new).write(data);
    }

    @PreDestroy
    public void shutdown() {
        writers.values().forEach(DataWriter::close);
    }

    public void flush() {
        writers.values().forEach(DataWriter::flush);
    }

    public static class DataWriter {

        private final BufferedWriter out;

        public DataWriter(String path) {
            try {
                File file = new File("data/" + path);
                file.getParentFile().mkdirs();
                file.createNewFile();
                out = new BufferedWriter(new FileWriter(file, true));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void write(String data) {
            synchronized (out) {
                try {
                    out.write(data);
                    out.write("\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public void flush() {
            synchronized (out) {
                try {
                    out.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public void close() {
            synchronized (out) {
                try {
                    flush();
                    out.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
