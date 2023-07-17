package com.demo.chapter01.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RouterId {
    private final String id;

    public static RouterId of(final String id) {
        return new RouterId(id);
    }

    @Override
    public String toString() {
        return "RouterId{" +
                "id='" + id + '\'' +
                '}';
    }
}
