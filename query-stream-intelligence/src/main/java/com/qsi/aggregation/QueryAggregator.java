package com.qsi.aggregation;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class QueryAggregator {

    private final ConcurrentHashMap<String, Stats> map = new ConcurrentHashMap<>();
    private final Set<String> seenQueries = ConcurrentHashMap.newKeySet();

    public void add(String query, long latency) {
        map.computeIfAbsent(query, k -> new Stats())
                .add(latency);
    }

    // snapshot สำหรับ read (กัน concurrent issue)
    public Map<String, Stats> snapshot() {
        return new HashMap<>(map);
    }

    // detect query ใหม่
    public boolean isNewQuery(String query) {
        return seenQueries.add(query);
    }

    // avg latency ของทั้ง system
    public long getGlobalAvgLatency() {
        return (long) map.values().stream()
                .mapToLong(Stats::avg)
                .average()
                .orElse(0);
    }

    // top N query (by avg latency)
    public Map<String, Stats> topN(int n) {
        return map.entrySet().stream()
                .sorted((a, b) -> Long.compare(
                        b.getValue().avg(),
                        a.getValue().avg()
                ))
                .limit(n)
                .collect(
                        HashMap::new,
                        (m, e) -> m.put(e.getKey(), e.getValue()),
                        HashMap::putAll
                );
    }
    public boolean addAndCheckNew(String query, long latency) {

        boolean isNew = seenQueries.add(query);

        map.computeIfAbsent(query, k -> new Stats())
                .add(latency);

        return isNew;
    }
    public String findRootCauseDetail() {

        return map.entrySet().stream()
                .sorted((a, b) -> Long.compare(
                        b.getValue().avg(),
                        a.getValue().avg()
                ))
                .limit(1)
                .map(e -> e.getKey() + " (avg=" + e.getValue().avg() + " ms)")
                .findFirst()
                .orElse(null);
    }
}