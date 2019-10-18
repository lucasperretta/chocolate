package com.chocolate.requests.loopj;

import android.content.Context;

import androidx.annotation.NonNull;

import com.chocolate.requests.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

@SuppressWarnings("unused")
public final class JSONArrayRequest<Type, Error> extends GsonParsableRequest<JSONArrayRequest<Type, Error>, JSONArrayRequest.Response<Type, Error>, ArrayList<Type>, Error> {

    // Constructor.....
    public JSONArrayRequest(@NotNull Context context, @NotNull Class<Type> typeClass, @NotNull Class<Error> errorClass, @Nullable String description) {
        //noinspection unchecked
        super(context, (Class<ArrayList<Type>>) new TypeToken<ArrayList<Type>>(){}.getType(), errorClass, description);
    }

    public JSONArrayRequest(@NotNull Context context, @NotNull Class<Type> typeClass, @NotNull Class<Error> errorClass) {
        this(context, typeClass, errorClass, null);
    }

    // Methods.....
    @Override protected ArrayList<Type> parse(String responseString, @NotNull Gson gson) throws Throwable {
        return gson.fromJson(responseString, typeClass);
    }

    @Override protected Error parseError(String responseString, @NotNull Gson gson) throws Throwable {
        return gson.fromJson(responseString, errorClass);
    }

    @Override protected Response<Type, Error> response(boolean success, int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable, ArrayList<Type> parsed, Error parsedError, Throwable parseError) {
        return new Response<>(responseString, parsed, parsedError, new Status(statusCode, success), headers, throwable, parseError);
    }

    @NonNull @Override public String getRequestType() {
        return "JSON Array (" + typeClass.getSimpleName() + ")";
    }

    // Classes.....
    @SuppressWarnings({"RedundantSuppression", "WeakerAccess", "SpellCheckingInspection", "NullableProblems"})
    public static final class Response<Type, Error> extends GsonParsableRequest.Response<ArrayList<Type>, Error> {

        // Constructor.....
        public Response(@NotNull String raw, @Nullable ArrayList<Type> array, @Nullable Error errorValue, @NotNull Request.Status status, @Nullable cz.msebera.android.httpclient.Header[] headers, @Nullable Throwable throwable, @Nullable Throwable parseThrowable) {
            super(array, errorValue, status, headers, throwable, raw, parseThrowable);
        }

    }

}
