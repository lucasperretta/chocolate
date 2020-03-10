package com.chocolate.requests.loopj;

import android.content.Context;

import com.chocolate.requests.Request;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import cz.msebera.android.httpclient.HttpEntity;

@SuppressWarnings("unused")
public abstract class BaseRequest<Self extends BaseRequest, ResponseType extends BaseRequest.Response> extends Request<Self, ResponseType, RequestParams, RequestHandle> {

    // Variables.....
    @SuppressWarnings("WeakerAccess") protected boolean fixNoHttpException = false;
    @SuppressWarnings("WeakerAccess") protected boolean useCookies = true;
    @SuppressWarnings("WeakerAccess") protected boolean enableRedirects = true;
    @SuppressWarnings("WeakerAccess") protected HttpEntity httpEntity;
    @SuppressWarnings("WeakerAccess") protected String httpEntityContentType;
    @SuppressWarnings("WeakerAccess") @Nullable SetupClientCallback setupClientCallback;

    // Constructors.....
    @SuppressWarnings("WeakerAccess") public BaseRequest(@NotNull Context context, @Nullable String description) { super(context, description); }

    @SuppressWarnings("WeakerAccess") public BaseRequest(@NotNull Context context) {
        super(context);
        this
                .addHeader("X-REQUESTED-WITH", "android")
                .addHeader("Accept", "application/json");
    }

    // Protected Methods.....
    @SuppressWarnings("WeakerAccess") @NotNull protected AsyncHttpClient getHttpClient() {
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

    @SuppressWarnings("WeakerAccess") @Nullable protected RequestHandle performRequest(@NotNull AsyncHttpResponseHandler handler) {
        RequestHandle requestHandle = null;
        if (body == null) body = new RequestParams();

        AsyncHttpClient client = getHttpClient();

        if (httpEntity != null && method != Method.HEAD) {
            switch (method) {
                case GET:
                    requestHandle = client.get(context, URL, httpEntity, httpEntityContentType, handler);
                    break;
                case POST:
                    requestHandle = client.post(context, URL, httpEntity, httpEntityContentType, handler);
                    break;
                case PUT:
                    requestHandle = client.put(context, URL, httpEntity, httpEntityContentType, handler);
                    break;
                case DELETE:
                    requestHandle = client.delete(context, URL, httpEntity, httpEntityContentType, handler);
                    break;
                case PATCH:
                    requestHandle = client.patch(context, URL, httpEntity, httpEntityContentType, handler);
                    break;
            }
        } else {
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
                    requestHandle = client.delete(URL, body, handler);
                    break;
                case PATCH:
                    requestHandle = client.patch(URL, body, handler);
                    break;
            }
        }

        return requestHandle;
    }

    // Methods.....
    @Override public Self body(RequestParams params) {
        this.httpEntity = null;
        this.httpEntityContentType = null;
        return super.body(params);
    }

    public Self body(Object... params) {
        return this.body(new RequestParams(params));
    }

    public Self body(HttpEntity entity, String contentType) {
        this.body = null;
        this.httpEntity = entity;
        this.httpEntityContentType = contentType;
        return self();
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
    @SuppressWarnings({"RedundantSuppression", "WeakerAccess", "SpellCheckingInspection", "NullableProblems"})
    public static abstract class Response<Type> extends Request.Response<Type, cz.msebera.android.httpclient.Header, Throwable> {

        // Constructors.....
        public Response(@Nullable Type value, @NotNull Status status, @Nullable cz.msebera.android.httpclient.Header[] headers, @Nullable Throwable throwable, @Nullable String stringResponse) {
            super(value, status, headers, throwable, stringResponse);
        }

    }

    // Interfaces.....
    @SuppressWarnings({"WeakerAccess", "RedundantSuppression"}) public interface SetupClientCallback {
        void setup(AsyncHttpClient client);
    }

}
