package com.chocolate.Requests.Loopj;

import android.content.Context;

import com.chocolate.Requests.Request;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.TextHttpResponseHandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import cz.msebera.android.httpclient.Header;

public class StringRequest extends LoopjBaseRequest<StringRequest, StringRequest.Response> {

    // Constructors.....
    public StringRequest(@NotNull Context context) { super(context); }

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
                onFinished(new Response(responseString, new Status(statusCode, false), headers, throwable));
            }

            @Override public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {

            }
        });
    }

    // Classes.....
    public static class Response extends LoopjBaseRequest.Response<String> {

        // Constructors.....
        public Response(@Nullable String value, @NotNull Status status, @Nullable cz.msebera.android.httpclient.Header[] headers, @Nullable Throwable throwable) {
            super(value, status, headers, throwable);
        }

    }

}
