package com.project.sublime.eazynames.utils;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.OkHttpClient;

/**
 * Created by goonerDroid on 17-04-2016.
 */
public class VolleySingleton {


    private static VolleySingleton instance;
    private RequestQueue reQueue;

    private VolleySingleton(Context context) {
        OkHttpClient client = new OkHttpClient();
        client.networkInterceptors().add(new StethoInterceptor());
        reQueue = Volley.newRequestQueue(context,new OkHttpStack(client));
    }

    public static VolleySingleton getInstance(Context context) {
        if(instance == null)
            instance = new VolleySingleton(context);
        return instance;
    }

    public RequestQueue getRequestQueue() {
        return reQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        request.setTag("app");
        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(Constants.TIMEOUT, DefaultRetryPolicy
                .DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(request);
    }
}
