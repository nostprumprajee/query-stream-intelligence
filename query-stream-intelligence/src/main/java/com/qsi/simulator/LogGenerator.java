package com.qsi.simulator;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

public class LogGenerator {

    private final String filePath;
    private volatile boolean spikeMode = false;
    private final Random random = new Random();

    public LogGenerator(String filePath) {
        this.filePath = filePath;
    }

    public void start() {
        new Thread(() -> {
            while (true) {
                try {
                    writeLog(generateLine());
                    Thread.sleep(100); // base rate
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void enableSpike() {
        System.out.println("🔥 SPIKE MODE ON");
        spikeMode = true;
    }

    public void disableSpike() {
        System.out.println("✅ SPIKE MODE OFF");
        spikeMode = false;
    }

    private String generateLine() {
        long latency;

        if (spikeMode) {
            latency = 1500 + random.nextInt(1000); // 800–1300 ms
        } else {
            latency = 50 + random.nextInt(100); // 50–150 ms
        }

        String query;
        if (random.nextBoolean()) {
            query = "SELECT * FROM user WHERE id = " + random.nextInt(1000);
        } else {
            query = "SELECT * FROM orders WHERE user_id = " + random.nextInt(1000);
        }

        return String.format("%s | %dms | %s",
                LocalDateTime.now(),
                latency,
                query
        );
    }

    private void writeLog(String line) {
        try (FileWriter fw = new FileWriter(filePath, true)) {
            fw.write(line + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}