package com.chocolate.requests.loopj;

import android.content.Context;

import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.TextHttpResponseHandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"unused", "rawtypes", "RedundantSuppression"})
public abstract class StringParsableRequest<Self extends StringParsableRequest, ResponseType extends StringParsableRequest.Response<ParseType>, ParseType> extends BaseRequest<Self, ResponseType> {

    // Variables.....
    @SuppressWarnings("WeakerAccess") protected boolean printStackTraceOnParseFailure = true;

    // Constructors.....
    @SuppressWarnings("WeakerAccess") public StringParsableRequest(@NotNull Context context, @Nullable String description) {
        super(context, description);
    }

    @SuppressWarnings("WeakerAccess") public StringParsableRequest(@NotNull Context context) {
        super(context);
    }

    // Abstract Methods.....
    protected abstract ParseType parse(String responseString) throws Throwable;

    protected abstract ResponseType response(boolean success, int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable, ParseType parsed, Throwable parseError);

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
                ParseType parsed = null;
                Throwable parseThrowable = null;
                try {
                    parsed = parse(responseString);
                } catch (Throwable e) {
                    if (printStackTraceOnParseFailure) e.printStackTrace();
                    parseThrowable = e;
                }
                onFinished(response(false, statusCode, headers, responseString, throwable, parsed, parseThrowable));
            }

            @Override public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                ParseType parsed = null;
                Throwable parseThrowable = null;
                try {
                    parsed = parse(responseString);
                } catch (Throwable e) {
                    if (printStackTraceOnParseFailure) e.printStackTrace();
                    parseThrowable = e;
                }
                onFinished(response(parseThrowable == null, statusCode, headers, responseString, null, parsed, parseThrowable));
            }
        });
    }

    @NotNull @Override protected ResponseType constructFailedResponse() {
        return response(false, 0, null, null, null, null, null);
    }

    public Self printStackTraceOnParseFailure(boolean print) {
        this.printStackTraceOnParseFailure = print;
        return self();
    }

    // Classes.....
    public static abstract class Response<Type> extends BaseRequest.Response<Type> {

        // Variables.....
        @SuppressWarnings({"WeakerAccess", "unused"}) @Nullable public final Throwable parseError;

        // Constructor.....
        public Response(@Nullable Type value, @NotNull Status status, @Nullable cz.msebera.android.httpclient.Header[] headers, @Nullable Throwable throwable, @Nullable String stringResponse, @Nullable Throwable parseError) {
            super(value, status, headers, throwable, stringResponse);
            this.parseError = parseError;
        }

    }

}
