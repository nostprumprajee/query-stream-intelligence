package com.qsi;

import com.qsi.ingestion.FileTailer;
import com.qsi.ingestion.LogStreamer;
import com.qsi.parser.LogParser;
import com.qsi.parser.QueryLog;
import com.qsi.normalize.QueryNormalizer;
import com.qsi.aggregation.QueryAggregator;
import com.qsi.metrics.QpsCounter;
import com.qsi.output.ConsoleReporter;
import com.qsi.simulator.LogGenerator;
import com.qsi.simulator.SpikeController;

public class Application {

    public static void main(String[] args) throws Exception {

        String filePath = "log.txt";

// start generator
        LogGenerator generator = new LogGenerator(filePath);
        generator.start();

// start controller
        SpikeController controller = new SpikeController(generator);
        controller.start();
        LogStreamer streamer = new LogStreamer();
        LogParser parser = new LogParser();
        QueryNormalizer normalizer = new QueryNormalizer();
        QueryAggregator aggregator = new QueryAggregator();
        QpsCounter qps = new QpsCounter();

        ConsoleReporter reporter = new ConsoleReporter(aggregator, qps);
        reporter.start();

        FileTailer tailer = new FileTailer();

        tailer.tail(filePath, line -> {
            QueryLog log = parser.parse(line);
            if (log == null) return;

            String normalized = normalizer.normalize(log.getQuery());

            boolean isNew = aggregator.addAndCheckNew(normalized, log.getLatency());

            if (isNew) {
                System.out.println("🆕 New query detected: " + normalized);
            }

            qps.increment();
        });
    }

}