package org.example.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TrainingMetric {
    private final AtomicInteger trainingsCounter = new AtomicInteger();


    public TrainingMetric(MeterRegistry meterRegistry) {
        meterRegistry.gauge("trainings.count", trainingsCounter);
    }

    public void incrementTrainingsCount() {
        trainingsCounter.incrementAndGet();
    }

    public void decrementTrainingsCount() {
        trainingsCounter.decrementAndGet();
    }
}
