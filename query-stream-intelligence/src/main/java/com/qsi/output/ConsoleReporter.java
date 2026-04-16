package com.qsi.output;

import com.qsi.aggregation.QueryAggregator;
import com.qsi.aggregation.Stats;
import com.qsi.analyzer.InsightGenerator;
import com.qsi.metrics.QpsCounter;
import com.qsi.analyzer.AnomalyDetector;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConsoleReporter {

    private final QueryAggregator aggregator;
    private final QpsCounter qps;
    private final AnomalyDetector detector = new AnomalyDetector();
    private final InsightGenerator insightGen = new InsightGenerator();
    public ConsoleReporter(QueryAggregator aggregator, QpsCounter qps) {
        this.aggregator = aggregator;
        this.qps = qps;
    }

    public void start() {
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(() -> {

                    System.out.println("\n==============================");
                    System.out.println("QPS: " + qps.getAndReset());

                    // global latency
                    long globalAvg = aggregator.getGlobalAvgLatency();
                    System.out.println("Global Avg Latency: " + globalAvg + " ms");

                    // anomaly detection
                    String alert = detector.detect(globalAvg);

                    // หา root cause ตลอด
                    String rootCause = aggregator.findRootCauseDetail();

                    if (alert != null) {
                        System.out.println(alert);
                    }

                    // แสดง root cause เสมอ (แต่จะมีความหมายตอน spike)
                    if (rootCause != null) {
                        System.out.println("🔥 Root Cause:");
                        System.out.println(rootCause);

                        // explain เฉพาะตอนมี anomaly
                        if (alert != null) {
                            String explanation = insightGen.explain(rootCause);
                            System.out.println("🧠 Insight: " + explanation);
                        }
                    }

                    // top queries
                    System.out.println("\nTop Queries:");
                    Map<String, Stats> top = aggregator.topN(5);

                    for (Map.Entry<String, Stats> e : top.entrySet()) {
                        System.out.println(
                                e.getKey()
                                        + " | count=" + e.getValue().getCount()
                                        + " | avg=" + e.getValue().avg()
                        );
                    }

                    System.out.println("==============================\n");

                }, 5, 5, TimeUnit.SECONDS);
    }
}