package com.chocolate.Requests.Loopj;

import android.content.Context;

import com.chocolate.Requests.Request;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.TextHttpResponseHandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONRequest extends LoopjBaseRequest<JSONRequest, JSONRequest.Response> {

    // Constructor.....
    public JSONRequest(Context context) {
        super(context);
    }

    // Methods......
    @Override protected RequestHandle perform() {
        return performRequest(new TextHttpResponseHandler() {
            @Override public void onProgress(long bytesWritten, long totalSize) {
                int progress = (int) ((bytesWritten * 100) / totalSize);
                if (progress <= 100 && progress >= 0) {
                    onProgressUpdate(bytesWritten, totalSize, progress);
                }
            }

            @Override public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                JSONObject jsonObject = null;
                JSONException exception = null;
                try {
                    jsonObject = new JSONObject(responseString);
                } catch (JSONException e) {
                    e.printStackTrace();
                    exception = e;
                }
                onFinished(new Response(responseString, jsonObject, new Request.Status(statusCode, false), headers, throwable == null ? exception : throwable));
            }

            @Override public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                JSONObject jsonObject = null;
                JSONException exception = null;
                try {
                    jsonObject = new JSONObject(responseString);
                } catch (JSONException e) {
                    e.printStackTrace();
                    exception = e;
                }
                onFinished(new Response(responseString, jsonObject, new Request.Status(statusCode, exception == null), headers, exception));
            }
        });
    }

    // Classes.....
    public static class Response extends Request.Response<JSONObject, cz.msebera.android.httpclient.Header, Throwable> {

        // Variables.....
        public String raw;

        // Constructor.....
        public Response(@NotNull String raw, @Nullable JSONObject object, @NotNull Request.Status status, @Nullable cz.msebera.android.httpclient.Header[] headers, @Nullable Throwable throwable) {
            super(object, status, headers, throwable);
            this.raw = raw;
        }

    }

}
