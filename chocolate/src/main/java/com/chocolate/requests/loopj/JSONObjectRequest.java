package com.chocolate.requests.loopj;

import android.content.Context;

import com.chocolate.requests.Request;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public final class JSONObjectRequest<Type> extends StringParsableRequest<JSONObjectRequest<Type>, JSONObjectRequest.Response<Type>, Type> {

    // Variables.....
    private Class<Type> typeClass;

    // Constructors.....
    public JSONObjectRequest(@NotNull Context context, Class<Type> typeClass, @Nullable String description) {
        super(context, description);
        this.typeClass = typeClass;
    }

    public JSONObjectRequest(@NotNull Context context, Class<Type> typeClass) {
        this(context, typeClass, null);
    }

    // Methods.....
    @Override protected Type parse(String responseString) throws Throwable {
        return getGsonParser().fromJson(responseString, typeClass);
    }

    @Override protected Response<Type> response(boolean success, int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable, Type parsed) {
        return new Response<>(responseString, parsed, new Status(statusCode, success), headers, throwable);
    }

    @Override public String getRequestType() {
        return "JSON Object (" + typeClass.getSimpleName() + ")";
    }

    // Classes.....
    @SuppressWarnings({"RedundantSuppression", "WeakerAccess", "SpellCheckingInspection", "NullableProblems"})
    public static final class Response<Type> extends BaseRequest.Response<Type> {

        // Constructor.....
        public Response(@NotNull String raw, @Nullable Type object, @NotNull Request.Status status, @Nullable cz.msebera.android.httpclient.Header[] headers, @Nullable Throwable throwable) {
            super(object, status, headers, throwable, raw);
        }

    }

}
