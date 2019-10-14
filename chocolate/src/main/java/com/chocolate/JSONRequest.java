package com.chocolate;

import com.loopj.android.http.RequestHandle;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class JSONRequest extends LoopjRequest<JSONRequest.Response> {

    // Methods......
    @Override protected RequestHandle perform() {
        return null;
    }

    // Classes.....
    public static class Response extends Request.Response<JSONObject, Header, Throwable> {

        public Response(@Nullable JSONObject object, @NotNull Status status, @Nullable Header[] headers, @Nullable Throwable throwable) {
            super(object, status, headers, throwable);
        }

    }

}
