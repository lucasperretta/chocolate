package com.chocolate.requests.loopj;

import android.content.Context;

import com.google.gson.Gson;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.TextHttpResponseHandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"RedundantThrows", "RedundantSuppression", "unused"})
public abstract class GsonParsableRequest<Self extends StringParsableRequest, ResponseType extends GsonParsableRequest.Response<ParseType, ErrorType>, ParseType, ErrorType> extends StringParsableRequest<Self, ResponseType, ParseType> {

    // Variables.....
    @SuppressWarnings("WeakerAccess") @Nullable protected SetupGsonCallback setupGsonCallback = null;

    // Constructors.....
    @SuppressWarnings("WeakerAccess") public GsonParsableRequest(@NotNull Context context, @Nullable String description) {
        super(context, description);
    }

    public GsonParsableRequest(@NotNull Context context) {
        this(context, null);
    }

    // Abstract Methods.....
    protected abstract ParseType parse(String responseString, @NotNull Gson gson) throws Throwable;

    protected abstract ErrorType parseError(String responseString, @NotNull Gson gson) throws Throwable;

    protected abstract ResponseType response(boolean success, int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable, ParseType parsed, ErrorType parsedError, Throwable parseError);

    // Methods.....
    @Deprecated @Override protected final ParseType parse(String responseString) { return null; }

    @Deprecated @Override protected final ResponseType response(boolean success, int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable, ParseType parsed, Throwable parseError) { return null; }

    @Override protected RequestHandle perform() {
        return performRequest(new TextHttpResponseHandler() {
            @Override public void onProgress(long bytesWritten, long totalSize) {
                int progress = (int) ((bytesWritten * 100) / totalSize);
                if (progress <= 100 && progress >= 0) {
                    onProgressUpdate(bytesWritten, totalSize, progress);
                }
            }

            @Override public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                ParseType parsed = null;
                Throwable parseThrowable = null;
                try {
                    parsed = parse(responseString, getGsonParser());
                } catch (Throwable e) {
                    if (printStackTraceOnParseFailure) e.printStackTrace();
                    parseThrowable = e;
                }
                onFinished(response(parseThrowable == null, statusCode, headers, responseString, null, parsed, null, parseThrowable));
            }

            @Override public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                ErrorType parsed = null;
                Throwable parseThrowable = null;
                try {
                    parsed = parseError(responseString, getGsonParser());
                } catch (Throwable e) {
                    if (printStackTraceOnParseFailure) e.printStackTrace();
                    parseThrowable = e;
                }
                onFinished(response(false, statusCode, headers, responseString, throwable, null, parsed, parseThrowable));
            }
        });
    }

    public Self setupGson(SetupGsonCallback callback) {
        this.setupGsonCallback = callback;
        return self();
    }

    @SuppressWarnings("WeakerAccess") protected Gson getGsonParser() {
        Gson gson = null;
        if (setupGsonCallback != null) gson = setupGsonCallback.setup();
        if (gson == null) gson = new Gson();
        return gson;
    }

    // Classes.....
    public static abstract class Response<Type, Error> extends StringParsableRequest.Response<Type> {

        // Variables.....
        @SuppressWarnings("WeakerAccess") public final Error errorValue;

        // Constructor.....
        public Response(@Nullable Type value, @Nullable Error error, @NotNull Status status, @Nullable cz.msebera.android.httpclient.Header[] headers, @Nullable Throwable throwable, @Nullable String stringResponse, @Nullable Throwable parseError) {
            super(value, status, headers, throwable, stringResponse, parseError);
            this.errorValue = error;
        }

    }

    // Interfaces.....
    @SuppressWarnings("WeakerAccess") public interface SetupGsonCallback {
        @Nullable Gson setup();
    }

}
