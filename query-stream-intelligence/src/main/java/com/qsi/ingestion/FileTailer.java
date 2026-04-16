package com.qsi.ingestion;

import java.io.RandomAccessFile;

public class FileTailer {

    public interface LineHandler {
        void handle(String line);
    }

    public void tail(String path, LineHandler handler) throws Exception {

        RandomAccessFile file = new RandomAccessFile(path, "r");

        // เริ่มอ่านจากท้ายไฟล์
        long filePointer = file.length();

        while (true) {
            long fileLength = file.length();

            if (fileLength > filePointer) {
                file.seek(filePointer);

                String line;
                while ((line = file.readLine()) != null) {
                    handler.handle(line);
                }

                filePointer = file.getFilePointer();
            }

            // sleep นิดนึงกัน CPU 100%
            Thread.sleep(500);
        }
    }
}