package com.creditscoring.deal.service.hook;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Profile("concurrency")
public class TestLockHookImpl implements LockHook {

    private final AtomicBoolean firstEntered = new AtomicBoolean(true);
    public CountDownLatch firstLocked = new CountDownLatch(1);
    public CountDownLatch allowFirstToFinish = new CountDownLatch(1);

    public void reset() {
        firstEntered.set(true);
        firstLocked = new CountDownLatch(1);
        allowFirstToFinish = new CountDownLatch(1);
    }

    @Override
    public void afterLockCaptured() {
        if (firstEntered.compareAndSet(true, false)) {
            firstLocked.countDown();

            try {
                allowFirstToFinish.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
