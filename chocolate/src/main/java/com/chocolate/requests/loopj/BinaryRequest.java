package com.chocolate.requests.loopj;

import android.content.Context;

import androidx.annotation.NonNull;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public final class BinaryRequest extends BaseRequest<BinaryRequest, BinaryRequest.Response> {

    // Constructors.....
    public BinaryRequest(@NotNull Context context, @Nullable String description) {
        super(context, description);
    }

    public BinaryRequest(@NotNull Context context) {
        super(context);
    }

    // Methods.....
    @Override @Nullable protected RequestHandle perform() {
        return performRequest(new AsyncHttpResponseHandler() {
            @Override public void onProgress(long bytesWritten, long totalSize) {
                int progress = (int) ((bytesWritten * 100) / totalSize);
                if (progress <= 100 && progress >= 0) {
                    onProgressUpdate(bytesWritten, totalSize, progress);
                }
            }

            @Override public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                onFinished(new Response(responseBody, new Status(statusCode, true), headers, null, null));
            }

            @Override public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                onFinished(new Response(responseBody, new Status(statusCode, false), headers, error, null));
            }
        });
    }

    @NonNull @Override public String getRequestType() {
        return "Byte Array";
    }

    // Classes.....
    @SuppressWarnings({"RedundantSuppression", "WeakerAccess", "SpellCheckingInspection", "NullableProblems"})
    public static final class Response extends BaseRequest.Response<byte[]> {

        // Constructors.....
        public Response(@Nullable byte[] value, @NotNull Status status, @Nullable cz.msebera.android.httpclient.Header[] headers, @Nullable Throwable throwable, @Nullable String stringResponse) {
            super(value, status, headers, throwable, stringResponse);
        }

    }

}
