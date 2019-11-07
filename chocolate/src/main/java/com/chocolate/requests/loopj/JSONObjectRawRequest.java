package com.chocolate.requests.loopj;

import android.content.Context;

import androidx.annotation.NonNull;

import com.chocolate.requests.Request;
import com.loopj.android.http.RequestHandle;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

@SuppressWarnings("unused")
public final class JSONObjectRawRequest extends StringParsableRequest<JSONObjectRawRequest, JSONObjectRawRequest.Response, JSONObject> {

    // Variables.....
    private boolean addAcceptApplicationJsonHeader = true;

    // Constructors.....
    public JSONObjectRawRequest(@NotNull Context context, @Nullable String description) { super(context, description); }

    public JSONObjectRawRequest(@NotNull Context context) {
        super(context);
    }

    // Methods......
    @Override protected JSONObject parse(String responseString) throws Throwable {
        return new JSONObject(responseString);
    }

    @Override protected Response response(boolean success, int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable, JSONObject parsed, Throwable parseError) {
        return new Response(responseString, parsed, new Status(statusCode, success), headers, throwable, parseError);
    }

    @Override protected RequestHandle perform() {
        addHeader("Accept", "application/json");
        return super.perform();
    }

    @NonNull @Override public String getRequestType() {
        return "JSON Raw";
    }

    public JSONObjectRawRequest addJsonHeader(boolean add) {
        this.addAcceptApplicationJsonHeader = add;
        return self();
    }

    public boolean autoAddJsonHeader() {
        return addAcceptApplicationJsonHeader;
    }

    // Classes.....
    @SuppressWarnings({"RedundantSuppression", "WeakerAccess", "SpellCheckingInspection", "NullableProblems"})
    public static final class Response extends StringParsableRequest.Response<JSONObject> {

        // Constructor.....
        public Response(@NotNull String raw, @Nullable JSONObject object, @NotNull Request.Status status, @Nullable cz.msebera.android.httpclient.Header[] headers, @Nullable Throwable throwable, @Nullable Throwable parseError) {
            super(object, status, headers, throwable, raw, parseError);
        }

    }

}
