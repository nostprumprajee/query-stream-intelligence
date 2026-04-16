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
    private final DashboardSocketHandler socket;

    private final AnomalyDetector detector = new AnomalyDetector();
    private final InsightGenerator insightGen = new InsightGenerator();

    public ConsoleReporter(QueryAggregator aggregator,
                           QpsCounter qps,
                           DashboardSocketHandler socket) {
        this.aggregator = aggregator;
        this.qps = qps;
        this.socket = socket;
    }

    public void start() {
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(() -> {

                    long currentQps = qps.getAndReset();
                    long globalAvg = aggregator.getGlobalAvgLatency();

                    // anomaly detection
                    String alert = detector.detect(globalAvg);

                    // root cause
                    String rootCause = aggregator.findRootCauseDetail();

                    String insight = null;
                    if (alert != null && rootCause != null) {
                        insight = insightGen.explain(rootCause);
                    }

                    // ===== Console Output =====
                    System.out.println("\n==============================");
                    System.out.println("QPS: " + currentQps);
                    System.out.println("Global Avg Latency: " + globalAvg + " ms");

                    if (alert != null) {
                        System.out.println(alert);
                    }

                    if (rootCause != null) {
                        System.out.println("🔥 Root Cause:");
                        System.out.println(rootCause);
                    }

                    if (insight != null) {
                        System.out.println("🧠 Insight: " + insight);
                    }

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

                    // ===== WebSocket JSON =====
                    try {
                        String json = buildJson(currentQps, globalAvg, alert, rootCause, insight, top);
                        socket.broadcast(json);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }, 2, 2, TimeUnit.SECONDS); // update ถี่ขึ้นหน่อย
    }

    private String buildJson(long qps,
                             long latency,
                             String alert,
                             String rootCause,
                             String insight,
                             Map<String, Stats> top) {

        StringBuilder topJson = new StringBuilder("[");

        boolean first = true;
        for (Map.Entry<String, Stats> e : top.entrySet()) {
            if (!first) topJson.append(",");
            first = false;

            topJson.append(String.format(
                    "{\"query\":\"%s\",\"count\":%d,\"avg\":%d}",
                    escape(e.getKey()),
                    e.getValue().getCount(),
                    e.getValue().avg()
            ));
        }

        topJson.append("]");

        return String.format("""
        {
          "qps": %d,
          "latency": %d,
          "alert": "%s",
          "rootCause": "%s",
          "insight": "%s",
          "topQueries": %s
        }
        """,
                qps,
                latency,
                safe(alert),
                safe(rootCause),
                safe(insight),
                topJson.toString()
        );
    }

    private String safe(String s) {
        return s == null ? "" : escape(s);
    }

    private String escape(String s) {
        return s.replace("\"", "\\\"");
    }
}