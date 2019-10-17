package com.chocolate.requests.loopj;

import android.content.Context;

import com.chocolate.requests.Request;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

@SuppressWarnings("unused")
public final class JSONArrayRequest<Type> extends StringParsableRequest<JSONArrayRequest<Type>, JSONArrayRequest.Response<Type>, ArrayList<Type>> {

    // Variables.....
    private Class<Type> typeClass;

    // Constructor.....
    public JSONArrayRequest(@NotNull Context context, Class<Type> typeClass, @Nullable String description) {
        super(context, description);
        this.typeClass = typeClass;
    }

    public JSONArrayRequest(@NotNull Context context, Class<Type> typeClass) {
        this(context, typeClass, null);
    }

    // Methods.....
    @Override protected ArrayList<Type> parse(String responseString) throws Throwable {
        return getGsonParser().fromJson(responseString, new TypeToken<ArrayList<Type>>() {}.getType());
    }

    @Override protected Response<Type> response(boolean success, int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable, ArrayList<Type> parsed) {
        return new Response<>(responseString, parsed, new Status(statusCode, success), headers, throwable);
    }

    @Override public String getRequestType() {
        return "JSON Array (" + typeClass.getSimpleName() + ")";
    }

    // Classes.....
    @SuppressWarnings({"RedundantSuppression", "WeakerAccess", "SpellCheckingInspection", "NullableProblems"})
    public static final class Response<Type> extends BaseRequest.Response<ArrayList<Type>> {

        // Constructor.....
        public Response(@NotNull String raw, @Nullable ArrayList<Type> array, @NotNull Request.Status status, @Nullable cz.msebera.android.httpclient.Header[] headers, @Nullable Throwable throwable) {
            super(array, status, headers, throwable, raw);
        }

    }

}
