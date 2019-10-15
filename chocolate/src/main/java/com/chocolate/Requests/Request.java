package com.chocolate.Requests;

import android.content.Context;

import com.chocolate.Requests.Loopj.JSONRequest;
import com.chocolate.Requests.Loopj.StringRequest;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unused")
public abstract class Request<Self extends Request, ResponseType extends Request.Response, Body, Handler> {

    // Static Variables.....
    private static String baseURL;

    // Variables.....
    @NotNull protected final Context context;
    @SuppressWarnings({"WeakerAccess", "NullableProblems"}) @NotNull protected Callback<ResponseType> callback;
    @NotNull protected Method method = Method.GET;
    @NotNull protected String URL;
    @Nullable protected Body body;
    @SuppressWarnings("WeakerAccess") @Nullable protected Progress.Listener progressListener;
    @NotNull protected final List<Header> headers = new ArrayList<>();
    protected int timeout = 20*1000;

    // Constructor.....
    public Request(@NotNull Context context) {
        this.context = context;
        this.URL = baseURL;
    }

    // Abstract Methods.....
    protected abstract Handler perform();

    // Protected Methods.....
    protected void onProgressUpdate(long bytesWritten, long totalSize, int progress) {
        if (progressListener != null) progressListener.progress(new Progress(bytesWritten, totalSize, progress));
    }

    protected void onFinished(ResponseType response) {
        callback.finished(response);
    }

    @SuppressWarnings("unchecked")
    protected Self self() {
        return (Self) this;
    }

    // Setters.....
    public Self to(@NotNull String url) {
        return to(url, false);
    }

    @SuppressWarnings("WeakerAccess")
    public Self to(@NotNull String url, boolean ignoreBaseURL) {
        this.URL = (ignoreBaseURL ? "" : baseURL) + url;
        return self();
    }

    public Self method(@NotNull Method method) {
        this.method = method;
        return self();
    }

    public Self body(Body body) {
        this.body = body;
        return self();
    }

    public Self addHeader(String header, String value) {
        headers.add(new Header(header, value));
        return self();
    }

    public Self progress(Progress.Listener listener) {
        this.progressListener = listener;
        return self();
    }

    public Self timeout(int timeout) {
        this.timeout = timeout;
        return self();
    }

    // Getters.....
    @NotNull public String getURL() { return URL; }

    @NotNull public Method getMethod() { return method; }

    @Nullable public Body getBody() { return body; }

    @NotNull public List<Header> getHeaders() { return headers; }

    public int getTimeout() { return timeout; }

    @Nullable public Progress.Listener getProgressListener() { return progressListener; }

    // Methods.....
    public Handler start(@NotNull Callback<ResponseType> callback) {
        this.callback = callback;
        return perform();
    }

    // Static Methods.....
    public static void setBaseURL(String URL) {
        baseURL = URL;
    }

    public static String getBaseURL() {
        return baseURL;
    }

    public static StringRequest string(@NotNull Context context) {
        return new StringRequest(context);
    }

    public static JSONRequest jsonObjectRaw(@NotNull Context context) {
        return new JSONRequest(context);
    }

    // Enums.....
    public enum Method {

        // Values.....
        GET, POST, PUT, PATCH, DELETE, HEAD;

        // Methods.....
        @NotNull @Override public String toString() { return name(); }

    }

    // Classes.....
    public static abstract class Plugin<RequestType extends Request> {




    }

    @SuppressWarnings({"RedundantSuppression", "WeakerAccess", "SpellCheckingInspection", "NullableProblems"})
    public static abstract class Response<Type, Header, Error> {

        // Variables.....
        @Nullable public final Type value;
        @Nullable public final Error error;
        @Nullable public final Header[] headers;
        @NotNull public final Status status;

        // Constructor.....
        public Response(@Nullable Type value, @NotNull Status status, @Nullable Header[] headers, @Nullable Error error) {
            this.value = value;
            this.status = status;
            this.headers = headers;
            this.error = error;
        }

        // Methods.....
        public boolean failed() {
            return status.isSuccessful;
        }

    }

    @SuppressWarnings({"RedundantSuppression", "WeakerAccess", "SpellCheckingInspection", "NullableProblems"})
    public static class Progress {

        // Variables.....
        public final long bytesWritten;
        public final long totalSize;
        public final int progress;

        // Constructor.....
        public Progress(long bytesWritten, long totalSize, int progress) {
            this.bytesWritten = bytesWritten;
            this.totalSize = totalSize;
            this.progress = progress;
        }

        // Interfaces.....
        public interface Listener {
            void progress(Progress progress);
        }

    }

    @SuppressWarnings({"RedundantSuppression", "WeakerAccess", "SpellCheckingInspection", "NullableProblems"})
    public static class Status {
        // Variables.....
        public final int value;
        public final String description;
        public final boolean isSuccessful;
        private static final HashMap<String, String> codesHashMap;
        static {
            codesHashMap = new HashMap<>();
            //region Asignaciónes
            codesHashMap.put("0", "Timeout");
            codesHashMap.put("100", "Continue");
            codesHashMap.put("101", "Switching Protocols");
            codesHashMap.put("102", "Processing");
            codesHashMap.put("200", "OK");
            codesHashMap.put("201", "Created");
            codesHashMap.put("202", "Accepted");
            codesHashMap.put("203", "Non-Authoritative Information");
            codesHashMap.put("204", "No Content");
            codesHashMap.put("205", "Reset Content");
            codesHashMap.put("206", "Partial Content");
            codesHashMap.put("207", "Multi-Status");
            codesHashMap.put("208", "Already Reported");
            codesHashMap.put("226", "IM Used");
            codesHashMap.put("300", "Multiple Choices");
            codesHashMap.put("301", "Moved Permanently");
            codesHashMap.put("302", "Found");
            codesHashMap.put("303", "See Other");
            codesHashMap.put("304", "Not Modified");
            codesHashMap.put("305", "Use Proxy");
            codesHashMap.put("306", "(Unused)");
            codesHashMap.put("307", "Temporary Redirect");
            codesHashMap.put("308", "Permanent Redirect");
            codesHashMap.put("400", "Bad Request");
            codesHashMap.put("401", "Unauthorized");
            codesHashMap.put("402", "Payment Required");
            codesHashMap.put("403", "Forbidden");
            codesHashMap.put("404", "Not Found");
            codesHashMap.put("405", "Method Not Allowed");
            codesHashMap.put("406", "Not Acceptable");
            codesHashMap.put("407", "Proxy Authentication Required");
            codesHashMap.put("408", "Request Timeout");
            codesHashMap.put("409", "Conflict");
            codesHashMap.put("410", "Gone");
            codesHashMap.put("411", "Length Required");
            codesHashMap.put("412", "Precondition Failed");
            codesHashMap.put("413", "Request Entity Too Large");
            codesHashMap.put("414", "Request-URI Too Long");
            codesHashMap.put("415", "Unsupported Media Type");
            codesHashMap.put("416", "Requested Range Not Satisfiable");
            codesHashMap.put("417", "Expectation Failed");
            codesHashMap.put("418", "I'm a teapot");
            codesHashMap.put("419", "Must re-login");
            codesHashMap.put("420", "Enhance Your Calm");
            codesHashMap.put("422", "Unprocessable Entity");
            codesHashMap.put("423", "Locked");
            codesHashMap.put("424", "Failed Dependency");
            codesHashMap.put("425", "Reserved for WebDAV");
            codesHashMap.put("426", "Upgrade Required");
            codesHashMap.put("428", "Precondition Required");
            codesHashMap.put("429", "Too Many Requests");
            codesHashMap.put("431", "Request Header Fields Too Large");
            codesHashMap.put("444", "No Response");
            codesHashMap.put("449", "Retry With");
            codesHashMap.put("450", "Blocked by Windows Parental Controls");
            codesHashMap.put("451", "Unavailable For Legal Reasons");
            codesHashMap.put("499", "Client Closed Request");
            codesHashMap.put("500", "Internal Server Error");
            codesHashMap.put("501", "Not Implemented");
            codesHashMap.put("502", "Bad Gateway");
            codesHashMap.put("503", "Service Unavailable");
            codesHashMap.put("504", "Gateway Timeout");
            codesHashMap.put("505", "HTTP Version Not Supported");
            codesHashMap.put("506", "Variant Also Negotiates");
            codesHashMap.put("507", "Insufficient Storage");
            codesHashMap.put("508", "Loop Detected");
            codesHashMap.put("509", "Bandwidth Limit Exceeded");
            codesHashMap.put("510", "Not Extended");
            codesHashMap.put("511", "Network Authentication Required");
            codesHashMap.put("598", "Network read timeout error");
            codesHashMap.put("599", "Network connect timeout error");
            //endregion
        }

        // Constructor.....
        public Status(int code, boolean succeed) {
            value = code;
            description = codesHashMap.get(code + "");
            isSuccessful = succeed;
        }

        // Methods.....
        public boolean isValueSuccessful() {
            return value >= 200 && value <= 299;
        }

    }

    @SuppressWarnings({"RedundantSuppression", "WeakerAccess", "SpellCheckingInspection", "NullableProblems"})
    protected class Header {

        // Variables.....
        @NotNull public final String header;
        @NotNull public final String value;

        // Constructor.....
        protected Header(@NotNull String header, @NotNull String value) {
            this.header = header;
            this.value = value;
        }

    }

    // Interfaces.....
    public interface Callback<ResponseType extends Request.Response> {
        void finished(ResponseType response);
    }

}
