package com.chocolate.Requests.Loopj;

import android.content.Context;

import com.chocolate.Requests.Request;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.jetbrains.annotations.NotNull;

public abstract class LoopjBaseRequest<CurrentClass extends LoopjBaseRequest, Response extends Request.Response> extends Request<CurrentClass, Response, RequestParams, RequestHandle> {

    // Variables.....
    @NotNull protected final Context context;
    protected boolean fixNoHttpException = false;
    protected boolean useCookies = true;

    // Constructor.....
    public LoopjBaseRequest(@NotNull Context context) {
        this.context = context;
        addHeader("X-REQUESTED-WITH", "android");
        addHeader("Accept", "application/json");
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

    // Methods.....
    public CurrentClass useCookies(boolean value) {
        this.useCookies = value;
        return getThis();
    }

    public boolean getUseCookies() { return useCookies; }

    public CurrentClass fixNoHttpException(boolean value) {
        this.fixNoHttpException = value;
        return getThis();
    }

    public boolean getFixNoHttpException() {
        return fixNoHttpException;
    }

}
