package io.neural.circuitbreaker.service;

import io.neural.circuitbreaker.annotation.GuardByCircuitBreaker;

public interface DemoService {

    @GuardByCircuitBreaker(noTripExceptions = {})
    String getUuid(int idx);

    @GuardByCircuitBreaker(noTripExceptions = {IllegalArgumentException.class})
    void illegalEx(int idx);

}
