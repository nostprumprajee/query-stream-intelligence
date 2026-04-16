package com.qsi.normalize;

public class QueryNormalizer {

    public String normalize(String query) {
        if (query == null) return "";

        // replace numbers
        query = query.replaceAll("\\b\\d+\\b", "?");

        // replace string literals
        query = query.replaceAll("'[^']*'", "?");

        return query;
    }
}