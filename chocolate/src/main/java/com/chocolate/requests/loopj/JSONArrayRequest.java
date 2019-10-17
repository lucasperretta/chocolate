package com.chocolate.requests.loopj;

import android.content.Context;

import com.chocolate.requests.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.TextHttpResponseHandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class JSONArrayRequest<Type> extends BaseRequest<JSONArrayRequest<Type>, JSONArrayRequest.Response<Type>> {

    // Variables.....
    private Class<Type> typeClass;

    // Constructor.....
    public JSONArrayRequest(@NotNull Context context, Class<Type> typeClass, @Nullable String description) {
        super(context, description);
        this.typeClass = typeClass;
    }

    public JSONArrayRequest(@NotNull Context context, Class<Type> typeClass) {
        this(context, typeClass, null);
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
                ArrayList<Type> responseObject = null;
                Throwable parserThrowable = null;
                try {
                    responseObject = new Gson().fromJson(responseString, new TypeToken<ArrayList<Type>>() {}.getType());
                } catch (Throwable e) {
                    e.printStackTrace();
                    parserThrowable = e;
                }
                onFinished(new Response<>(responseString, responseObject, new Request.Status(statusCode, false), headers, throwable == null ? parserThrowable : throwable));
            }

            @Override public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                ArrayList<Type> responseObject = null;
                Throwable parserThrowable = null;
                try {
                    responseObject = new Gson().fromJson(responseString, new TypeToken<ArrayList<Type>>() {}.getType());
                } catch (Throwable e) {
                    e.printStackTrace();
                    parserThrowable = e;
                }
                onFinished(new Response<>(responseString, responseObject, new Request.Status(statusCode, parserThrowable == null), headers, parserThrowable));
            }
        });
    }

    @Override public String getRequestType() {
        return "JSON Array (" + typeClass.getSimpleName() + ")";
    }

    // Classes.....
    @SuppressWarnings({"RedundantSuppression", "WeakerAccess", "SpellCheckingInspection", "NullableProblems"})
    public static class Response<Type> extends BaseRequest.Response<ArrayList<Type>> {

        // Constructor.....
        public Response(@NotNull String raw, @Nullable ArrayList<Type> array, @NotNull Request.Status status, @Nullable cz.msebera.android.httpclient.Header[] headers, @Nullable Throwable throwable) {
            super(array, status, headers, throwable, raw);
        }

    }

}
