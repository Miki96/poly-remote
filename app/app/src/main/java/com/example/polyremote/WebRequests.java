package com.example.polyremote;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class WebRequests {

    enum REMOTE_ACTION {
        PLAY,
        PAUSE,
        NEXT,
        PREVIOUS,
        VOLUME_UP,
        VOLUME_DOWN,
        MUTE,
    }

    private String urlRoot;

    private WebRequests() {
        this.urlRoot = null;
    }

    private static WebRequests instance;
    public static WebRequests getInstance() {
        if (instance == null) {
            instance = new WebRequests();
            instance.urlRoot = "http://192.168.100.97:6252/";
        }
        return instance;
    }

    public void setUrlRoot(String urlRoot) {
        this.urlRoot = urlRoot;
    }


    public void sendAction(Context c, REMOTE_ACTION action) {

        String actionCode = getAction(action);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(c);
        String url = urlRoot + "?action=" + actionCode;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Display the first 500 characters of the response string.
                    //binding.textView.setText("Response is: "+ response.substring(0,500));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //binding.textView.setText("That didn't work! \n" + error.getMessage() + "\n" + error.getLocalizedMessage());
                }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    private String getAction(REMOTE_ACTION action) {
        String r = null;
        switch (action) {
            case PLAY:
                r = "5";
                break;
            case PAUSE:
                r = "5";
                break;
            case NEXT:
                r = "1";
                break;
            case PREVIOUS:
                r = "2";
                break;
            case VOLUME_UP:
                r = "3";
                break;
            case VOLUME_DOWN:
                r = "4";
                break;
            case MUTE:
                r = "6";
                break;
        }
        return r;
    }




}
