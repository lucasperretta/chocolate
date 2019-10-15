package com.chocolate.Requests.Loopj;

import android.content.Context;

import com.chocolate.Requests.Request;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class LoopjBaseRequest<Self extends LoopjBaseRequest, Response extends Request.Response> extends Request<Self, Response, RequestParams, RequestHandle> {

    // Variables.....
    protected boolean fixNoHttpException = false;
    protected boolean useCookies = true;
    protected boolean enableRedirects = true;
    @Nullable SetupClientCallback setupClientCallback;

    // Constructor.....
    public LoopjBaseRequest(@NotNull Context context) {
        super(context);
        this
                .addHeader("X-REQUESTED-WITH", "android")
                .addHeader("Accept", "application/json");
    }

    // Protected Methods.....
    protected AsyncHttpClient getHttpClient() {
        AsyncHttpClient client = new AsyncHttpClient(fixNoHttpException, 80, 443);
        for (Header header : headers) {
            client.addHeader(header.header, header.value);
        }
        client.setTimeout(timeout);
        if (useCookies) {
            PersistentCookieStore persistentCookieStore = new PersistentCookieStore(context);
            client.setCookieStore(persistentCookieStore);
        }
        client.setEnableRedirects(enableRedirects);

        if (setupClientCallback != null) setupClientCallback.setup(client);

        return client;
    }

    protected RequestHandle performRequest(AsyncHttpResponseHandler handler) {
        RequestHandle requestHandle = null;
        if (body == null) body = new RequestParams();

        AsyncHttpClient client = getHttpClient();

        switch (method) {
            case GET:
                requestHandle = client.get(URL, body, handler);
                break;
            case POST:
                requestHandle = client.post(URL, body, handler);
                break;
            case HEAD:
                requestHandle = client.head(URL, body, handler);
                break;
            case PUT:
                requestHandle = client.put(URL, body, handler);
                break;
            case DELETE:
                client.delete(URL, body, handler);
                break;
            case PATCH:
                requestHandle = client.patch(URL, body, handler);
                break;
        }

        return requestHandle;
    }

    // Setters.....
    public Self useCookies(boolean value) {
        this.useCookies = value;
        return self();
    }

    public Self fixNoHttpException(boolean value) {
        this.fixNoHttpException = value;
        return self();
    }

    public Self enableRedirects(boolean enable) {
        this.enableRedirects = enable;
        return self();
    }

    public Self setupClient(SetupClientCallback callback) {
        this.setupClientCallback = callback;
        return self();
    }

    // Getters.....
    public boolean getUseCookies() { return useCookies; }

    public boolean getFixNoHttpException() { return fixNoHttpException; }

    public boolean getEnableRedirects() { return enableRedirects; }

    // Classes.....
    public static abstract class Response<Type> extends Request.Response<Type, cz.msebera.android.httpclient.Header, Throwable> {

        // Constructors.....
        public Response(@Nullable Type value, @NotNull Status status, @Nullable cz.msebera.android.httpclient.Header[] headers, @Nullable Throwable throwable) {
            super(value, status, headers, throwable);
        }

    }

    // Interfaces.....
    public interface SetupClientCallback {
        void setup(AsyncHttpClient client);
    }

}
