package com.qsi.ingestion;

import java.io.BufferedReader;
import java.io.FileReader;

public class LogStreamer {

    public interface LineHandler {
        void handle(String line);
    }

    public void streamFile(String path, LineHandler handler) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(path), 64 * 1024)) {
            String line;
            while ((line = reader.readLine()) != null) {
                handler.handle(line);
            }
        }
    }
}