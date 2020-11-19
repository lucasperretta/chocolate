package com.chocolate.requests.loopj;

import android.content.Context;

import androidx.annotation.NonNull;

import com.chocolate.requests.Request;
import com.loopj.android.http.RequestHandle;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;

@SuppressWarnings({"unused", "RedundantSuppression"})
public final class JSONArrayRawRequest extends StringParsableRequest<JSONArrayRawRequest, JSONArrayRawRequest.Response, JSONArray> {

    // Variables.....
    private boolean addAcceptApplicationJsonHeader = true;

    // Constructors.....
    public JSONArrayRawRequest(@NotNull Context context, @Nullable String description) { super(context, description); }

    public JSONArrayRawRequest(@NotNull Context context) {
        super(context);
    }

    // Methods......
    @Override protected JSONArray parse(String responseString) throws Throwable {
        return new JSONArray(responseString);
    }

    @Override protected Response response(boolean success, int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable, JSONArray parsed, Throwable parseError) {
        return new Response(responseString, parsed, new Status(statusCode, success), headers, throwable, parseError);
    }

    @Override protected RequestHandle perform() {
        addHeader("Accept", "application/json");
        return super.perform();
    }

    @NonNull @Override public String getRequestType() {
        return "JSON Array Raw";
    }

    public JSONArrayRawRequest addJsonHeader(boolean add) {
        this.addAcceptApplicationJsonHeader = add;
        return self();
    }

    public boolean autoAddJsonHeader() {
        return addAcceptApplicationJsonHeader;
    }

    // Classes.....
    @SuppressWarnings({"RedundantSuppression", "WeakerAccess", "SpellCheckingInspection", "NullableProblems"})
    public static final class Response extends StringParsableRequest.Response<JSONArray> {

        // Constructor.....
        public Response(@NotNull String raw, @Nullable JSONArray array, @NotNull Request.Status status, @Nullable cz.msebera.android.httpclient.Header[] headers, @Nullable Throwable throwable, @Nullable Throwable parseError) {
            super(array, status, headers, throwable, raw, parseError);
        }

    }

}