package com.example.smartteachingsystem.view.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LoginResource<T> {
    @NonNull
    public final LoginStatus status;

    @Nullable
    public final T data;

    @Nullable
    public final String message;


    public LoginResource(@NonNull LoginStatus status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> LoginResource<T> success (@Nullable T data) {
        return new LoginResource<>(LoginStatus.SUCCESS,data, null);
    }

    public static <T> LoginResource<T> error(@NonNull String msg,@Nullable T data) {

        return new LoginResource<>(LoginStatus.ERROR,data, msg);
    }

    public static <T> LoginResource<T> loading(@Nullable T data) {
        return new LoginResource<>(LoginStatus.LOADING,data, null);
    }

    public enum LoginStatus {
        SUCCESS,
        ERROR,
        LOADING
    }
}
