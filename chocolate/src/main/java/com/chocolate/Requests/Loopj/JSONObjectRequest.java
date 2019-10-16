package com.chocolate.Requests.Loopj;

import android.content.Context;

import com.chocolate.Requests.Request;
import com.loopj.android.http.RequestHandle;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JSONObjectRequest<Type> extends LoopjBaseRequest<JSONObjectRequest<Type>, JSONObjectRequest.Response<Type>> {

    // Variables.....
    private Class<Type> typeClass;

    // Constructor.....
    public JSONObjectRequest(@NotNull Context context, Class<Type> typeClass) {
        super(context);
        this.typeClass = typeClass;
    }

    // Methods.....
    @Override protected RequestHandle perform() {
        return null;
    }

    @Override public String getRequestType() {
        return "JSON Object (" + typeClass.getSimpleName() + ")";
    }

    // Classes.....
    @SuppressWarnings({"RedundantSuppression", "WeakerAccess", "SpellCheckingInspection", "NullableProblems"})
    public static class Response<Type> extends LoopjBaseRequest.Response<Type> {

        // Variables.....
        public String raw;

        // Constructor.....
        public Response(@NotNull String raw, @Nullable Type object, @NotNull Request.Status status, @Nullable cz.msebera.android.httpclient.Header[] headers, @Nullable Throwable throwable) {
            super(object, status, headers, throwable);
            this.raw = raw;
        }

    }

}
