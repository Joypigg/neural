package io.neural.jwt.support;

import java.util.Date;

public final class ClockImpl implements Clock {

    @Override
    public Date getToday() {
        return new Date();
    }
}
