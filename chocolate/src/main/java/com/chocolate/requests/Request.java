package com.chocolate.requests;

import android.content.Context;

import androidx.annotation.NonNull;

import com.chocolate.requests.loopj.BinaryRequest;
import com.chocolate.requests.loopj.FileRequest;
import com.chocolate.requests.loopj.JSONArrayRawRequest;
import com.chocolate.requests.loopj.JSONArrayRequest;
import com.chocolate.requests.loopj.JSONObjectRawRequest;
import com.chocolate.requests.loopj.JSONObjectRequest;
import com.chocolate.requests.loopj.StringRequest;
import com.chocolate.requests.plugins.LoggerPlugin;
import com.chocolate.utilities.Time;
import com.loopj.android.http.PersistentCookieStore;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings({"unused", "rawtypes", "RedundantSuppression"})
public abstract class Request<Self extends Request, ResponseType extends Request.Response, Body, Handler> {

    // Static Variables.....
    private static Configuration configuration = new Configuration();

    // Variables.....
    @NotNull protected final Context context;
    @SuppressWarnings({"WeakerAccess", "RedundantSuppression"}) @Nullable protected final String description;
    @SuppressWarnings({"WeakerAccess", "NullableProblems", "NotNullFieldNotInitialized"}) @NotNull protected Callback<ResponseType> callback;
    @Nullable CallbackErrorHandler callbackErrorHandler;
    @NotNull protected Method method = Method.GET;
    @NotNull protected String URL;
    @Nullable protected Body body;
    @SuppressWarnings("WeakerAccess") @Nullable protected Progress.Listener progressListener;
    @NotNull protected final List<Header> headers = new ArrayList<>();
    protected int timeout = 20*1000;
    private boolean canceled = false;
    @SuppressWarnings("WeakerAccess") protected Long requestStartTime;
    @SuppressWarnings("WeakerAccess") protected Long requestEndTime;
    private int nextPluginToCall;
    private boolean printQuietly = false;
    private boolean handleCallbackErrors = false;
    @SuppressWarnings("WeakerAccess") protected HashMap<String, String> tags;

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

    protected void onFinished(@NotNull ResponseType response) {
        this.requestEndTime = Time.current.millis();
        nextPluginToCall = -1;
        continueFinishingRequest(response, new Plugin.FinishingCallback<>(this, response));
    }

    private void continueFinishingRequest(@NotNull ResponseType response, @NotNull Plugin.FinishingCallback<ResponseType, Handler> finishingCallback) {
        if (canceled) return;
        nextPluginToCall++;
        if (nextPluginToCall >= configuration.plugins.size()) {
            if (handleCallbackErrors) {
                try {
                    this.callback.finished(response);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    if (callbackErrorHandler != null) callbackErrorHandler.onError(throwable);
                }
            } else {
                this.callback.finished(response);
            }
            return;
        }
        configuration.plugins.get(nextPluginToCall).onFinishingRequest(context, this, response, finishingCallback);
    }

    private void restart() {
        canceled = false;
        start(callback);
    }

    @SuppressWarnings("WeakerAccess")
    protected void cancel() {
        for (int i = 0; i < configuration.plugins.size(); i++) {
            configuration.plugins.get(i).onRequestCanceled(context, this);
        }
        canceled = true;
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

    public Self get() {
        this.method = Method.GET;
        return self();
    }

    public Self post() {
        this.method = Method.POST;
        return self();
    }

    public Self put() {
        this.method = Method.PUT;
        return self();
    }

    public Self patch() {
        this.method = Method.PATCH;
        return self();
    }

    public Self delete() {
        this.method = Method.DELETE;
        return self();
    }

    public Self head() {
        this.method = Method.HEAD;
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

    public Self printQuietly() {
        this.printQuietly = true;
        return self();
    }

    public Self setPrintQuietly(boolean printQuietly) {
        this.printQuietly = printQuietly;
        return self();
    }

    public Self addTag(String tag) {
        return addTag(tag, null);
    }

    @SuppressWarnings("WeakerAccess") public Self addTag(String tag, String value) {
        if (tags == null) tags = new HashMap<>();
        tags.put(tag, value);
        return self();
    }

    public Self handleCallbackErrors(boolean handle) {
        this.handleCallbackErrors = handle;
        return self();
    }

    public Self callbackErrorHandler(CallbackErrorHandler callback) {
        this.callbackErrorHandler = callback;
        return self();
    }

    // Getters.....
    @NotNull public static Configuration getConfiguration() {
        return configuration;
    }

    @Nullable public String getDescription() { return description; }

    @NotNull public String getURL() { return URL; }

    @NotNull public Method getMethod() { return method; }

    @Nullable public Body getBody() { return body; }

    @NotNull public List<Header> getHeaders() { return headers; }

    public int getTimeout() { return timeout; }

    public boolean getPrintQuietly() { return printQuietly; }

    @Nullable public Progress.Listener getProgressListener() { return progressListener; }

    @Nullable public Long getRequestStartTime() { return requestStartTime; }

    @Nullable public Long getRequestEndTime() { return requestEndTime; }

    @Nullable public Long getElapsedTime() {
        if (requestStartTime != null && requestEndTime != null) {
            return requestEndTime - requestStartTime;
        }
        return null;
    }

    @Nullable public String getTag(String tag) { return tags != null ? tags.get(tag) : null; }

    public boolean hasTag(String tag) { return tags != null && tags.containsKey(tag); }

    public boolean handlesCallbackErrors() {
        return handleCallbackErrors;
    }

    @Nullable public CallbackErrorHandler getCallbackErrorHandler() { return callbackErrorHandler; }

    // Methods.....
    @SuppressWarnings({"WeakerAccess", "UnusedReturnValue"}) public Handler start(@NotNull Callback<ResponseType> callback) {
        this.callback = callback;
        this.requestStartTime = Time.current.millis();
        Plugin.StartingCallback startingCallback = new Plugin.StartingCallback<>(this);
        for (int i = 0; i < configuration.plugins.size() && !canceled; i++) {
            configuration.plugins.get(i).onStartingRequest(context, this, startingCallback);
        }
        if (canceled) return null;
        return perform();
    }

    // Static Methods.....
    public static void setup(Configuration.SetupCallback callback) {
        configuration = new Configuration();
        callback.setup(configuration);
    }

    @NotNull public static String getBaseURL() {
        return configuration.baseURL;
    }

    public static void clearCookies(@NonNull Context context) {
        new PersistentCookieStore(context).clear();
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
        public abstract void onStartingRequest(@NotNull Context context, @NotNull Request request, @NotNull StartingCallback callback);

        public abstract void onFinishingRequest(@NotNull Context context, @NotNull Request request, @NotNull Response response, @NotNull FinishingCallback callback);

        // Overridable Methods.....
        @SuppressWarnings("EmptyMethod") public void onRequestCanceled(@NotNull Context context, @NotNull Request request) {}

        // Classes.....
        public static class StartingCallback<ResponseType extends Response, HandlerType> extends Plugin.Callback<ResponseType, HandlerType> {

            // Constructors.....
            private StartingCallback(@NotNull Request<?, ResponseType, ?, HandlerType> request) {
                super(request);
            }

        }

        public static class FinishingCallback<ResponseType extends Response, HandlerType> extends Plugin.Callback<ResponseType, HandlerType> {

            // Variables.....
            @NotNull private final ResponseType response;

            // Constructors.....
            private FinishingCallback(@NotNull Request<?, ResponseType, ?, HandlerType> request, @NotNull ResponseType response) {
                super(request);
                this.response = response;
            }

            // Methods.....
            public void continueRequest() {
                request.continueFinishingRequest(response, this);
            }

        }

        // Interfaces.....
        private static abstract class Callback<ResponseType extends Response, HandlerType> {

            // Variables.....
            @NotNull protected final Request<?, ResponseType, ?, HandlerType> request;

            // Constructors.....
            private Callback(@NotNull Request<?, ResponseType, ?, HandlerType> request) {
                this.request = request;
            }

            // Methods.....
            public void cancelRequest() {
                request.cancel();
            }

            public void restartRequest() {
                request.restart();
            }

        }

    }

    @SuppressWarnings({"RedundantSuppression", "WeakerAccess", "SpellCheckingInspection", "NullableProblems"})
    public static abstract class Response<Type, Header, Error> {

        // Variables.....
        public final Type value;
        public final Error error;
        public final Header[] headers;
        @NotNull public final Status status;
        public final String stringResponse;

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
        private boolean ignoreSSLVerification = false;
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

        public void ignoreSSLVerification(boolean ignoreSSLVerification) {
            this.ignoreSSLVerification = ignoreSSLVerification;
        }

        public boolean getIgnoreSSLVerification() {
            return ignoreSSLVerification;
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
    protected static class Header {

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
    @SuppressWarnings({"WeakerAccess", "RedundantSuppression", "rawtypes"}) public interface Callback<ResponseType extends Request.Response> {
        void finished(ResponseType response);
    }

    public interface CallbackErrorHandler {
        void onError(Throwable throwable);
    }

}
