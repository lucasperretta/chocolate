package com.chocolate.Requests.Loopj;

import android.content.Context;

import com.chocolate.Requests.Request;
import com.google.gson.Gson;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.TextHttpResponseHandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONObjectRequest<Type> extends LoopjBaseRequest<JSONObjectRequest<Type>, JSONObjectRequest.Response<Type>> {

    // Variables.....
    private Class<Type> typeClass;

    // Constructors.....
    public JSONObjectRequest(@NotNull Context context, @Nullable String description) { super(context, description); }

    public JSONObjectRequest(@NotNull Context context, Class<Type> typeClass) {
        super(context);
        this.typeClass = typeClass;
    }

    // Methods.....
    @Override protected RequestHandle perform() {
        return performRequest(new TextHttpResponseHandler() {
            @Override public void onProgress(long bytesWritten, long totalSize) {
                int progress = (int) ((bytesWritten * 100) / totalSize);
                if (progress <= 100 && progress >= 0) {
                    onProgressUpdate(bytesWritten, totalSize, progress);
                }
            }

            @Override public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                Type responseObject = null;
                Throwable parserThrowable = null;
                try {
                    responseObject = new Gson().fromJson(responseString, typeClass);
                } catch (Throwable e) {
                    e.printStackTrace();
                    parserThrowable = e;
                }
                onFinished(new Response<>(responseString, responseObject, new Request.Status(statusCode, false), headers, throwable == null ? parserThrowable : throwable));
            }

            @Override public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Type responseObject = null;
                Throwable parserThrowable = null;
                try {
                    responseObject = new Gson().fromJson(responseString, typeClass);
                } catch (Throwable e) {
                    e.printStackTrace();
                    parserThrowable = e;
                }
                onFinished(new Response<>(responseString, responseObject, new Request.Status(statusCode, parserThrowable == null), headers, parserThrowable));
            }
        });
    }

    @Override public String getRequestType() {
        return "JSON Object (" + typeClass.getSimpleName() + ")";
    }

    // Classes.....
    @SuppressWarnings({"RedundantSuppression", "WeakerAccess", "SpellCheckingInspection", "NullableProblems"})
    public static class Response<Type> extends LoopjBaseRequest.Response<Type> {

        // Constructor.....
        public Response(@NotNull String raw, @Nullable Type object, @NotNull Request.Status status, @Nullable cz.msebera.android.httpclient.Header[] headers, @Nullable Throwable throwable) {
            super(object, status, headers, throwable, raw);
        }

    }

}
