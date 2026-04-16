package com.qsi.parser;

public class LogParser {

    public QueryLog parse(String line) {
        try {
            String[] parts = line.split("\\|");

            long latency = Long.parseLong(parts[1].trim().replace("ms", ""));
            String query = parts[2].trim();

            QueryLog q = new QueryLog();
            q.setLatency(latency);
            q.setQuery(query);

            return q;

        } catch (Exception e) {
            // skip malformed line
            return null;
        }
    }
}