package com.chocolate.requests;

import android.content.Context;

import com.chocolate.requests.loopj.BinaryRequest;
import com.chocolate.requests.loopj.FileRequest;
import com.chocolate.requests.loopj.JSONArrayRawRequest;
import com.chocolate.requests.loopj.JSONArrayRequest;
import com.chocolate.requests.loopj.JSONObjectRawRequest;
import com.chocolate.requests.loopj.JSONObjectRequest;
import com.chocolate.requests.loopj.StringRequest;
import com.chocolate.requests.plugins.LoggerPlugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unused")
public abstract class Request<Self extends Request, ResponseType extends Request.Response, Body, Handler> {

    // Static Variables.....
    private static Configuration configuration = new Configuration();

    // Variables.....
    @NotNull protected final Context context;
    @Nullable protected final String description;
    @SuppressWarnings({"WeakerAccess", "NullableProblems"}) @NotNull protected Callback<ResponseType> callback;
    @NotNull protected Method method = Method.GET;
    @NotNull protected String URL;
    @Nullable protected Body body;
    @SuppressWarnings("WeakerAccess") @Nullable protected Progress.Listener progressListener;
    @NotNull protected final List<Header> headers = new ArrayList<>();
    protected int timeout = 20*1000;
    private boolean canceled = false;
    @SuppressWarnings("WeakerAccess") protected Long requestStartTime;
    @SuppressWarnings("WeakerAccess") protected Long requestEndTime;

    // Constructor.....
    public Request(@NotNull Context context, @Nullable String description) {
        this.context = context;
        this.description = description;
        this.URL = configuration.baseURL;
    }

    public Request(@NotNull Context context) {
        this(context, null);
    }

    // Abstract Methods.....
    @Nullable protected abstract Handler perform();

    @NotNull public abstract String getRequestType();

    // Protected Methods.....
    protected void onProgressUpdate(long bytesWritten, long totalSize, int progress) {
        if (progressListener != null) progressListener.progress(new Progress(bytesWritten, totalSize, progress));
    }

    protected void onFinished(ResponseType response) {
        this.requestEndTime = Calendar.getInstance().getTimeInMillis();
        for (int i = 0; i < configuration.plugins.size() && !canceled; i++) {
            configuration.plugins.get(i).onFinishingRequest(context, this, response);
        }
        if (canceled) return;
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
        this.URL = (ignoreBaseURL ? "" : configuration.baseURL) + url;
        return self();
    }

    public Self url(@NotNull String url) {
        return to(url, false);
    }

    public Self url(@NotNull String url, boolean ignoreBaseURL) {
        return to(url, ignoreBaseURL);
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
    @Nullable public String getDescription() { return description; }

    @NotNull public String getURL() { return URL; }

    @NotNull public Method getMethod() { return method; }

    @Nullable public Body getBody() { return body; }

    @NotNull public List<Header> getHeaders() { return headers; }

    public int getTimeout() { return timeout; }

    @Nullable public Progress.Listener getProgressListener() { return progressListener; }

    @Nullable public Long getRequestStartTime() { return requestStartTime; }

    @Nullable public Long getRequestEndTime() { return requestEndTime; }

    @Nullable public Long getElapsedTime() {
        if (requestStartTime != null && requestEndTime != null) {
            return requestEndTime - requestStartTime;
        }
        return null;
    }

    // Methods.....
    @SuppressWarnings("WeakerAccess") public Handler start(@NotNull Callback<ResponseType> callback) {
        this.callback = callback;
        this.requestStartTime = Calendar.getInstance().getTimeInMillis();
        for (int i = 0; i < configuration.plugins.size() && !canceled; i++) {
            configuration.plugins.get(i).onStartingRequest(context, this);
        }
        if (canceled) return null;
        return perform();
    }

    public Handler restart() {
        canceled = false;
        return start(callback);
    }

    public void cancel() {
        for (int i = 0; i < configuration.plugins.size(); i++) {
            configuration.plugins.get(i).onRequestCanceled(context, this);
        }
        canceled = true;
    }

    // Static Methods.....
    public static void setup(Configuration.SetupCallback callback) {
        configuration = new Configuration();
        callback.setup(configuration);
    }

    @NotNull public static String getBaseURL() {
        return configuration.baseURL;
    }

    // Static Helpers.....
    public static BinaryRequest binary(@NotNull Context context, @Nullable String description) {
        return new BinaryRequest(context, description);
    }

    public static FileRequest file(@NotNull Context context, @Nullable String description) {
        return new FileRequest(context, description);
    }

    public static StringRequest string(@NotNull Context context, @Nullable String description) {
        return new StringRequest(context, description);
    }

    public static <Type, Error> JSONObjectRequest<Type, Error> jsonObject(@NotNull Context context, @Nullable String description, @NotNull Class<Type> typeClass, @NotNull Class<Error> errorClass) {
        return new JSONObjectRequest<>(context, typeClass, errorClass, description);
    }

    public static <Type, Error> JSONArrayRequest<Type, Error> jsonArray(@NotNull Context context, @Nullable String description, @NotNull Class<Type> typeClass, @NotNull Class<Error> errorClass) {
        return new JSONArrayRequest<>(context, typeClass, errorClass, description);
    }

    public static <Type> JSONObjectRequest<Type, Void> jsonObject(@NotNull Context context, @Nullable String description, @NotNull Class<Type> typeClass) {
        return new JSONObjectRequest<>(context, typeClass, Void.class, description);
    }

    public static <Type> JSONArrayRequest<Type, Void> jsonArray(@NotNull Context context, @Nullable String description, @NotNull Class<Type> typeClass) {
        return new JSONArrayRequest<>(context, typeClass, Void.class, description);
    }

    public static JSONObjectRawRequest jsonObjectRaw(@NotNull Context context, @Nullable String description) {
        return new JSONObjectRawRequest(context, description);
    }

    public static JSONArrayRawRequest jsonArrayRaw(@NotNull Context context, @Nullable String description) {
        return new JSONArrayRawRequest(context, description);
    }

    // Enums.....
    public enum Method {

        // Values.....
        GET, POST, PUT, PATCH, DELETE, HEAD;

        // Methods.....
        @NotNull @Override public String toString() { return name(); }

    }

    // Classes.....
    @SuppressWarnings({"RedundantSuppression", "WeakerAccess", "SpellCheckingInspection", "NullableProblems"})
    public static abstract class Plugin {

        // Abstract Methods.....
        public abstract void onStartingRequest(@NotNull Context context, @NotNull Request request);

        public abstract void onFinishingRequest(@NotNull Context context, @NotNull Request request, @NotNull Response response);

        // Overridable Methods.....
        @SuppressWarnings("EmptyMethod") public void onRequestCanceled(@NotNull Context context, @NotNull Request request) {}

        // Classes.....
        public static class Callback<ResponseType> {

            // Methods.....
            public void cancelRequest() {

            }

            public void continueRequest() {

            }

        }

    }

    @SuppressWarnings({"RedundantSuppression", "WeakerAccess", "SpellCheckingInspection", "NullableProblems"})
    public static abstract class Response<Type, Header, Error> {

        // Variables.....
        @Nullable public final Type value;
        @Nullable public final Error error;
        @Nullable public final Header[] headers;
        @NotNull public final Status status;
        @Nullable public final String stringResponse;

        // Constructor.....
        public Response(@Nullable Type value, @NotNull Status status, @Nullable Header[] headers, @Nullable Error error, @Nullable String stringResponse) {
            this.value = value;
            this.status = status;
            this.headers = headers;
            this.error = error;
            this.stringResponse = stringResponse;
        }

        // Methods.....
        public boolean failed() { return !status.isSuccessful; }

        public boolean success() { return status.isSuccessful; }

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
    public static class Configuration {

        // Variables.....
        @NotNull private String baseURL = "";
        @NotNull private final LoggerPlugin loggerPlugin = new LoggerPlugin();
        @NotNull private final List<Plugin> plugins = new ArrayList<>();

        // Constructor.....
        private Configuration() {
            enableLogging(true);
        }

        // Methods.....
        public Configuration setBaseURL(@NotNull String URL) {
            this.baseURL = URL;
            return this;
        }

        public Configuration addPlugin(@NotNull Plugin plugin) {
            if (!plugins.contains(plugin)) plugins.add(plugin);
            return this;
        }

        public Configuration removePlugin(@NotNull Plugin plugin) {
            plugins.remove(plugin);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue") public Configuration enableLogging(boolean enable) {
            if (enable) {
                if (!plugins.contains(loggerPlugin)) plugins.add(0, loggerPlugin);
            } else {
                plugins.remove(loggerPlugin);
            }
            return this;
        }

        // Interfaces.....
        public interface SetupCallback {
            void setup(Configuration configuration);
        }

    }

    @SuppressWarnings({"RedundantSuppression", "WeakerAccess", "SpellCheckingInspection", "NullableProblems"})
    public static class Status {
        // Variables.....
        public final int value;
        public final String description;
        public final boolean isSuccessful;
        public static final HashMap<String, String> codesHashMap;
        static {
            codesHashMap = new HashMap<>();
            //region Asignaciónes
            codesHashMap.put("0", "ERROR");
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
            codesHashMap.put("419", "Authentication Timeout");
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
            description = codesHashMap.containsKey(code + "") ? codesHashMap.get(code + "") : "Unknown";
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
