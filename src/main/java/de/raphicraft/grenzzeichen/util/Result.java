package de.raphicraft.grenzzeichen.util;

import org.jetbrains.annotations.Nullable;

public record Result<T, E>(@Nullable T data, @Nullable E error) {
    public static <T, E> Result<T, E> of(@Nullable T data, @Nullable E message) {
        return new Result<>(data, message);
    }
}