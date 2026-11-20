package com.lodh8.starter.shared.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MdcTaskDecoratorTest {

    @AfterEach
    void tearDown() {
        LogMdc.clear();
    }

    @Test
    void copiesMdcToDecoratedRunnable() {
        LogMdc.putUid("parent-uid");
        var decorator = new MdcTaskDecorator();
        AtomicReference<String> childUid = new AtomicReference<>();

        Runnable decorated = decorator.decorate(() -> childUid.set(LogMdc.uid()));
        LogMdc.clear();
        decorated.run();

        assertEquals("parent-uid", childUid.get());
        assertNull(LogMdc.uid());
    }
}
