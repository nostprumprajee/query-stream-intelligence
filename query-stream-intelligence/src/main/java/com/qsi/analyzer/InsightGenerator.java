package com.qsi.analyzer;

public class InsightGenerator {

    public String explain(String query) {

        query = query.toLowerCase();

        if (query.contains("select *") && !query.contains("where")) {
            return "Query นี้อาจทำ full table scan (ไม่มี WHERE clause)";
        }

        if (query.contains("where") && !query.contains("id")) {
            return "Query อาจไม่มี index ที่เหมาะสมใน WHERE clause";
        }

        if (query.contains("join")) {
            return "Query มี JOIN อาจทำให้ performance ลดลงหากไม่มี index";
        }

        return "Query นี้มี latency สูง อาจต้องตรวจสอบ execution plan";
    }
}