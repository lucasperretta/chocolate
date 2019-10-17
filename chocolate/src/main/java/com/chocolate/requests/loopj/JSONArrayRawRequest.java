package com.chocolate.requests.loopj;

import android.content.Context;

import com.chocolate.requests.Request;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;

public final class JSONArrayRawRequest extends StringParsableRequest<JSONArrayRawRequest, JSONArrayRawRequest.Response, JSONArray> {

    // Constructors.....
    public JSONArrayRawRequest(@NotNull Context context, @Nullable String description) { super(context, description); }

    public JSONArrayRawRequest(Context context) {
        super(context);
    }

    // Methods......
    @Override protected JSONArray parse(String responseString) throws Throwable {
        return new JSONArray(responseString);
    }

    @Override protected Response response(boolean success, int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable, JSONArray parsed) {
        return new Response(responseString, parsed, new Status(statusCode, success), headers, throwable);
    }

    @Override public String getRequestType() {
        return "JSON Array Raw";
    }

    // Classes.....
    @SuppressWarnings({"RedundantSuppression", "WeakerAccess", "SpellCheckingInspection", "NullableProblems"})
    public static final class Response extends BaseRequest.Response<JSONArray> {

        // Constructor.....
        public Response(@NotNull String raw, @Nullable JSONArray array, @NotNull Request.Status status, @Nullable cz.msebera.android.httpclient.Header[] headers, @Nullable Throwable throwable) {
            super(array, status, headers, throwable, raw);
        }

    }

}