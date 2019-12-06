package com.chocolate.requests.plugins;

import android.content.Context;

import androidx.annotation.NonNull;

import com.chocolate.logger.Logger;
import com.chocolate.logger.LoggerInterface;
import com.chocolate.requests.Request;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class LoggerPlugin extends Request.Plugin implements LoggerInterface {

    // Methods.....
    @Override public void onStartingRequest(@NotNull Context context, @NotNull Request request, @NotNull StartingCallback callback) {
        print(request.getMethod().toString() + " Request to URL: " + request.getURL());
    }

    @Override public void onFinishingRequest(@NotNull Context context, @NotNull Request request, @NotNull Request.Response response, @NotNull FinishingCallback callback) {
        log(response.failed() ? Logger.STYLE_ERROR : Logger.STYLE_INFO,"Request\n" +
                        (request.getDescription() != null ? ("Detail: " + request.getDescription() + "\n") : "") +
                        "URL: " + request.getURL() + "\n" +
                        "Request Method: " + request.getMethod().toString() + "\n" +
                        "Request Status: " + (response.success() ? "SUCCESS" : "FAILURE") + "\n" +
                        "Elapsed Time: " + request.getElapsedTime() + "ms (Timeout: " + request.getTimeout() + "ms)\n" +
                        "Status Code: " + response.status.value + " " + response.status.description + "\n" +
                        "Request Type: " + request.getRequestType() +
                        (response.stringResponse != null ? ("\nResponse: " + response.stringResponse) : ""));
        callback.continueRequest();
    }

    @NonNull @Override public String getTag() {
        return Request.class.getSimpleName();
    }

}
