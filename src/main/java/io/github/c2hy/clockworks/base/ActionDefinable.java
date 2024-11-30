package io.github.c2hy.clockworks.base;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface ActionDefinable {
    ActionDefinable action(String routing, Runnable runnable);

    <T> ActionDefinable action(String routing, Supplier<T> supplier);

    <T> ActionDefinable action(String routing, Class<T> tClass, Consumer<T> consumer);

    <T, E> ActionDefinable action(String routing, Class<T> tClass, Function<T, E> consumer);

    void ready();
}
