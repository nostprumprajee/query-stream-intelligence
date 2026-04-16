package com.qsi.aggregation;

public class Stats {

    private long count;
    private long totalLatency;

    public synchronized void add(long latency) {
        count++;
        totalLatency += latency;
    }

    public long getCount() {
        return count;
    }

    public long avg() {
        if (count == 0) return 0;
        return totalLatency / count;
    }
}