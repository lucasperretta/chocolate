package com.chocolate.requests.loopj;

import android.content.Context;

import androidx.annotation.NonNull;

import com.chocolate.requests.Request;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"unused", "RedundantThrows"})
public final class JSONObjectRequest<Type, Error> extends GsonParsableRequest<JSONObjectRequest<Type, Error>, JSONObjectRequest.Response<Type, Error>, Type, Error> {

    // Variables.....
    @SuppressWarnings("WeakerAccess") @NotNull protected final Class<Type> typeClass;
    @SuppressWarnings("WeakerAccess") @NotNull protected final Class<Error> errorClass;

    // Constructors.....
    public JSONObjectRequest(@NotNull Context context, @NotNull Class<Type> typeClass, @NotNull Class<Error> errorClass, @Nullable String description) {
        super(context, description);
        this.typeClass = typeClass;
        this.errorClass = errorClass;
    }

    public JSONObjectRequest(@NotNull Context context, @NotNull Class<Type> typeClass, @NotNull Class<Error> errorClass) {
        this(context, typeClass, errorClass, null);
    }

    // Methods.....
    @Override protected Type parse(String responseString, @NotNull Gson gson) throws Throwable {
        return gson.fromJson(responseString, typeClass);
    }

    @Override protected Error parseError(String responseString, @NotNull Gson gson) throws Throwable {
        return gson.fromJson(responseString, errorClass);
    }

    @Override protected Response<Type, Error> response(boolean success, int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable, Type parsed, Error parsedError, Throwable parseError) {
        return new Response<>(responseString, parsed, parsedError, new Status(statusCode, success), headers, throwable, parseError);
    }

    @NonNull @Override public String getRequestType() {
        return "JSON Object (" + typeClass.getSimpleName() + ")";
    }

    // Classes.....
    @SuppressWarnings({"RedundantSuppression", "WeakerAccess", "SpellCheckingInspection", "NullableProblems"})
    public static final class Response<Type, Error> extends GsonParsableRequest.Response<Type, Error> {

        // Constructor.....
        public Response(@NotNull String raw, @Nullable Type object, @Nullable Error errorValue, @NotNull Request.Status status, @Nullable cz.msebera.android.httpclient.Header[] headers, @Nullable Throwable throwable, @Nullable Throwable parseThrowable) {
            super(object, errorValue, status, headers, throwable, raw, parseThrowable);
        }

    }

}
