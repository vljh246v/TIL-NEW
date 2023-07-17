package com.demo.chapter01.domain;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class Router {
    private final RouterType routerType;
    private final RouterId routerId;

    public static List<Router> retrieveRouter(final List<Router> routers, Predicate<Router> predicate) {
        return routers.stream()
                .filter(predicate)
                .toList();
    }

    public static Predicate<Router> filterRouterByType(RouterType routerType) {
        return routerType.equals(RouterType.CORE) ? isCore() : isEdge();
    }
    private static Predicate<Router> isCore() {
        return router -> router.getRouterType() == RouterType.CORE;
    }

    private static Predicate<Router> isEdge() {
        return router -> router.getRouterType() == RouterType.EDGE;
    }

    public RouterType getRouterType() {
        return routerType;
    }

    @Override
    public String toString() {
        return "Router{" +
                "routerType=" + routerType +
                ", routerId=" + routerId +
                '}';
    }
}
