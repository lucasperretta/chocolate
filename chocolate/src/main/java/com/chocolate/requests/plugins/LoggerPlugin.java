package com.chocolate.requests.plugins;

import android.content.Context;

import androidx.annotation.NonNull;

import com.chocolate.logger.Logger;
import com.chocolate.logger.LoggerInterface;
import com.chocolate.requests.Request;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unused", "rawtypes"})
public class LoggerPlugin extends Request.Plugin implements LoggerInterface {

    // Variables.....
    private static boolean printRequestBodyEnabled = false;

    // Methods.....
    @Override public void onStartingRequest(@NotNull Context context, @NotNull Request request, @NotNull StartingCallback callback) {
        if (!request.getPrintQuietly()) print(request.getMethod().toString() + " Request to URL: " + request.getURL());
    }

    @SuppressWarnings("rawtypes") @Override public void onFinishingRequest(@NotNull Context context, @NotNull Request request, @NotNull Request.Response response, @NotNull FinishingCallback callback) {
        log(response.failed() ? Logger.STYLE_ERROR : request.getPrintQuietly() ? Logger.STYLE_VERBOSE : Logger.STYLE_INFO,"Request\n" +
            (request.getDescription() != null ? ("Detail: " + request.getDescription() + "\n") : "") +
            "URL: " + request.getURL() + "\n" +
            "Request Method: " + request.getMethod().toString() + "\n" +
            "Request Status: " + (response.success() ? "SUCCESS" : "FAILURE") + "\n" +
            "Elapsed Time: " + request.getElapsedTime() + "ms (Timeout: " + request.getTimeout() + "ms)\n" +
            "Status Code: " + response.status.value + " " + response.status.description + "\n" +
            "Request Type: " + request.getRequestType() +
            (printRequestBodyEnabled && request.getBody() != null ? "\nRequest Body: " + request.getBody() : "") +
            (response.stringResponse != null ? ("\nResponse: " + response.stringResponse) : ""));
        callback.continueRequest();
    }

    @NonNull @Override public String getTag() {
        return Request.class.getSimpleName();
    }

    public static void setPrintRequestBodyEnabled(boolean printRequestBodyEnabled) {
        LoggerPlugin.printRequestBodyEnabled = printRequestBodyEnabled;
    }

}
