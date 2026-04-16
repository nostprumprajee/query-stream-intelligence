package com.qsi;

import com.qsi.ingestion.FileTailer;
import com.qsi.parser.LogParser;
import com.qsi.parser.QueryLog;
import com.qsi.normalize.QueryNormalizer;
import com.qsi.aggregation.QueryAggregator;
import com.qsi.metrics.QpsCounter;
import com.qsi.output.ConsoleReporter;
import com.qsi.output.DashboardSocketHandler;
import com.qsi.simulator.LogGenerator;
import com.qsi.simulator.SpikeController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class Application {

    @Autowired
    private DashboardSocketHandler socket;

    @PostConstruct
    public void start() throws Exception {

        String filePath = "log.txt";

        // === Core components ===
        LogParser parser = new LogParser();
        QueryNormalizer normalizer = new QueryNormalizer();
        QueryAggregator aggregator = new QueryAggregator();
        QpsCounter qps = new QpsCounter();

        // === Reporter (สำคัญ) ===
        ConsoleReporter reporter =
                new ConsoleReporter(aggregator, qps, socket);
        reporter.start();

        // === Generator ===
        LogGenerator generator = new LogGenerator(filePath);
        generator.start();

        // === Controller ===
        SpikeController controller = new SpikeController(generator);
        controller.start();

        // === Tailer ===
        FileTailer tailer = new FileTailer();

        new Thread(() -> {
            try {
                tailer.tail(filePath, line -> {
                    QueryLog log = parser.parse(line);
                    if (log == null) return;

                    String normalized = normalizer.normalize(log.getQuery());

                    boolean isNew = aggregator.addAndCheckNew(
                            normalized,
                            log.getLatency()
                    );

                    if (isNew) {
                        System.out.println("🆕 New query detected: " + normalized);
                    }

                    qps.increment();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}