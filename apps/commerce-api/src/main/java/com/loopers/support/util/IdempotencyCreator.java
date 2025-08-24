package com.loopers.support.util;

import java.util.UUID;

public final class IdempotencyCreator {

    public static String create(Object data) {
        return UUID.nameUUIDFromBytes(data.toString().getBytes()).toString();
    }

    private IdempotencyCreator() {
    }
}
