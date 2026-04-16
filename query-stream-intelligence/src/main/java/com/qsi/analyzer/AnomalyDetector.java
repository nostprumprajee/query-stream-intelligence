package com.qsi.analyzer;

import java.util.ArrayDeque;
import java.util.Deque;

public class AnomalyDetector {

    private final Deque<Long> window = new ArrayDeque<>();
    private final int windowSize = 5;

    private long lastAlertTime = 0;
    private final long cooldownMs = 5000; // 5 วิ

    private Long fixedBaseline = null;

    public String detect(long currentAvgLatency) {

        if (window.size() < windowSize) {
            window.addLast(currentAvgLatency);
            return null;
        }

        if (fixedBaseline == null) {
            fixedBaseline = window.stream()
                    .mapToLong(Long::longValue)
                    .sum() / window.size();
        }

        long baseline = fixedBaseline;
        System.out.println(
                "DEBUG -> current=" + currentAvgLatency +
                        ", baseline=" + baseline
        );

        if (currentAvgLatency > baseline * 1.2) {
            return "⚠️ Latency spike detected!";
        }

        return null;
    }
}