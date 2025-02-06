package org.example.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class UserRegistrationsMetric {
    private final AtomicInteger userRegistrationsCounter= new AtomicInteger(0);

    @Autowired
    public UserRegistrationsMetric(MeterRegistry meterRegistry) {
        meterRegistry.gauge("registrations.users.count", userRegistrationsCounter);
    }

    public void incrementUserRegistrations() {
        userRegistrationsCounter.incrementAndGet();
    }

    public void decrementUserRegistrations() {
        userRegistrationsCounter.decrementAndGet();
    }
}

