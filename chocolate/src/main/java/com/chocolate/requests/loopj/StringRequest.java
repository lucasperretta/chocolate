package com.chocolate.requests.loopj;

import android.content.Context;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public final class StringRequest extends StringParsableRequest<StringRequest, StringRequest.Response, String> {

    // Constructors.....
    public StringRequest(@NotNull Context context, @Nullable String description) { super(context, description); }

    public StringRequest(@NotNull Context context) { super(context); }

    // Methods.....
    @Override protected String parse(String responseString) {
        return responseString;
    }

    @Override protected Response response(boolean success, int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable, String parsed) {
        return new Response(parsed, new Status(statusCode, success), headers, throwable);
    }

    @Override public String getRequestType() {
        return String.class.getSimpleName();
    }

    // Classes.....
    @SuppressWarnings({"RedundantSuppression", "WeakerAccess", "SpellCheckingInspection", "NullableProblems"})
    public static final class Response extends BaseRequest.Response<String> {

        // Constructors.....
        public Response(@Nullable String value, @NotNull Status status, @Nullable cz.msebera.android.httpclient.Header[] headers, @Nullable Throwable throwable) {
            super(value, status, headers, throwable, value);
        }

    }

}
