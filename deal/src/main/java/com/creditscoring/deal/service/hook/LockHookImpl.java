package com.creditscoring.deal.service.hook;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!concurrency")
public class LockHookImpl implements LockHook {

    @Override
    public void afterLockCaptured() {

    }
}
