package com.qsi.metrics;

import java.util.concurrent.atomic.AtomicLong;

public class QpsCounter {

    private final AtomicLong counter = new AtomicLong();

    public void increment() {
        counter.incrementAndGet();
    }

    public long getAndReset() {
        return counter.getAndSet(0);
    }
}