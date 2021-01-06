package com.kinnarastudio.commons.function;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * Throwable version of {@link Supplier}
 *
 * @param <R>
 * @param <E>
 */
@FunctionalInterface
public interface TrySupplier<R, E extends Exception> extends Supplier<R> {
    @Nullable
    R tryGet() throws E;

    @Nullable
    default R get() {
        try {
            return tryGet();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).severe(e.getMessage());
            return null;
        }
    }

    default TrySupplier<R, E> onCatch(Function<? super E, R> onCatch) {
        try {
            return this::tryGet;
        } catch (Exception e) {
            Objects.requireNonNull(onCatch);
            return () -> onCatch.apply((E) e);
        }
    }
}