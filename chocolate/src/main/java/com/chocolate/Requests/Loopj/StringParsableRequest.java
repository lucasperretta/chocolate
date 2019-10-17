package com.chocolate.Requests.Loopj;

import android.content.Context;

import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.TextHttpResponseHandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class StringParsableRequest<Self extends StringParsableRequest, ResponseType extends BaseRequest.Response<ParseType>, ParseType> extends BaseRequest<Self, ResponseType> {

    // Variables.....
    private boolean printStackTraceOnParseFailure = true;

    // Constructors.....
    public StringParsableRequest(@NotNull Context context, @Nullable String description) {
        super(context, description);
    }

    public StringParsableRequest(@NotNull Context context) {
        super(context);
    }

    // Abstract Methods.....
    protected abstract ParseType parse(String responseString) throws Throwable;

    protected abstract ResponseType response(boolean success, int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable, ParseType parsed);

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
                onFinished(response(false, statusCode, headers, responseString, throwable == null ? parseThrowable : throwable, parsed));
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
                onFinished(response(parseThrowable == null, statusCode, headers, responseString, parseThrowable, parsed));
            }
        });
    }

    public Self printStackTraceOnParseFailure(boolean print) {
        this.printStackTraceOnParseFailure = print;
        return self();
    }

}
