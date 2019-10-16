package com.chocolate.Requests.Plugins;

import android.util.Log;

import com.chocolate.Requests.Request;

public class LoggerPlugin extends Request.Plugin {

    // Methods.....
    @Override public void onStartingRequest(Request request) {
        print(request.getMethod().toString() + " Request to URL: " + request.getURL(), false);
    }

    @Override public void onFinishingRequest(Request request, Request.Response response) {
        print("Request\n" +
                        (request.getDescription() != null ? ("Detail: " + request.getDescription() + "\n") : "") +
                        "URL: " + request.getURL() + "\n" +
                        "Request Method: " + request.getMethod().toString() + "\n" +
                        "Request Status: " + (response.success() ? "SUCCESS" : "FAILURE") + "\n" +
                        "Elapsed Time: " + request.getElapsedTime() + "ms (Timeout: " + request.getTimeout() + "ms)\n" +
                        "Status Code: " + response.status.value + " " + response.status.description + "\n" +
                        "Request Type: " + request.getRequestType() +
                        (response.stringResponse != null ? ("\nResponse: " + response.stringResponse) : ""),
                response.failed());
    }

    private static void print(String veryLongString, boolean isError) {
        for(int i = 0; i <= veryLongString.length() / 1000; i++) {
            int start = i * 1000;
            int end = (i+1) * 1000;
            end = end > veryLongString.length() ? veryLongString.length() : end;
            if (isError) {
                Log.e("APP_" + Request.class.getSimpleName(), veryLongString.substring(start, end));
            } else {
                Log.i("APP_" + Request.class.getSimpleName(), veryLongString.substring(start, end));
            }
        }
    }

}
