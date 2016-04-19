package com.project.sublime.eazynames.service;

import android.content.Context;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.sublime.eazynames.model.Events;
import com.project.sublime.eazynames.model.UsersBean;
import com.project.sublime.eazynames.utils.Constants;
import com.project.sublime.eazynames.utils.Timber;
import com.project.sublime.eazynames.utils.VolleySingleton;

import org.json.JSONObject;

import java.io.IOException;

import de.greenrobot.event.EventBus;

/**
 * Created by goonerDroid on 17-04-2016.
 */
public class RequestService {


    private static RequestService instance;
    private final EventBus eventBus;
    private Context context;
    private UsersBean userBean;

    public RequestService(Context context) {
        this.context = context;
        eventBus = EventBus.getDefault();
    }

    public synchronized static RequestService getInstance(Context context) {
        if (instance == null) {
            instance = new RequestService(context);
        }
        return instance;
    }


    //Get list of users.
    public void getUsers() {
        JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method
                .GET, Constants.URL, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Timber.v("Response" + jsonObject.toString());
                ObjectMapper mapper = new ObjectMapper();
                try {
                    userBean = mapper.readValue(jsonObject.toString(), UsersBean.class);
                    eventBus.post(new Events(Events.GET_USER_EVENT, true,userBean.getUserArrayList() ));
                } catch (IOException e) {
                    e.printStackTrace();
                    eventBus.post(new Events(Events.GET_USER_EVENT, false, Constants
                            .EXCEPTION_ERROR_STRING));
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                eventBus.post(new Events(Events.GET_USER_EVENT, false, volleyError));
            }
        });
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }
}
