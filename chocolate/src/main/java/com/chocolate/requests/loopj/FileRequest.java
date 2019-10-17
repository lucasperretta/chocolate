package com.chocolate.requests.loopj;

import android.content.Context;

import androidx.annotation.NonNull;

import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

@SuppressWarnings("unused")
public final class FileRequest extends BaseRequest<FileRequest, FileRequest.Response> {

    // Constructors.....
    public FileRequest(@NotNull Context context, @Nullable String description) {
        super(context, description);
    }

    public FileRequest(@NotNull Context context) {
        super(context);
    }

    // Methods.....
    @Override protected RequestHandle perform() {
        return performRequest(new FileAsyncHttpResponseHandler(context) {
            @Override public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, File file) {
                onFinished(new Response(file, new Status(statusCode, false), headers, throwable));
            }

            @Override public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, File file) {
                onFinished(new Response(file, new Status(statusCode, true), headers, null));
            }
        });
    }

    @NonNull @Override public String getRequestType() {
        return "File";
    }

    // Classes.....
    public static final class Response extends BaseRequest.Response<File> {

        // Constructor.....
        public Response(@Nullable File value, @NotNull Status status, @Nullable cz.msebera.android.httpclient.Header[] headers, @Nullable Throwable throwable) {
            super(value, status, headers, throwable, null);
        }

    }

}
