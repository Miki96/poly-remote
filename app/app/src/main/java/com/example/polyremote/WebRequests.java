package com.example.polyremote;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class WebRequests {

    public class NoCacheImageLoader extends ImageLoader {
        public NoCacheImageLoader(RequestQueue queue, ImageCache imageCache) {
            super(queue, imageCache);
        }

        @Override
        protected Request<Bitmap> makeImageRequest(String requestUrl, int maxWidth, int maxHeight, ImageView.ScaleType scaleType, String cacheKey) {
            Request<Bitmap> request =  super.makeImageRequest(requestUrl, maxWidth, maxHeight, scaleType, cacheKey);
            // key method
            request.setShouldCache(false);
            return request;
        }
    }

    enum REMOTE_ACTION {
        TEST,
        PLAY,
        PAUSE,
        NEXT,
        PREVIOUS,
        VOLUME_UP,
        VOLUME_DOWN,
        MUTE,
        MOUSE_LEFT,
        MOUSE_RIGHT,
        MOUSE_MOVE,
        MOUSE_SCROLL,
        MOUSE_DRAG,
        SCREEN,
        SHUTDOWN,
        RESTART,
        SLEEP,
        KEYBOARD_TYPE,
        KEYBOARD_BACK,
        KEYBOARD_RETURN,
        KEYBOARD_TAB,
        KEYBOARD_ESC,
        COMMAND
    }

    private String urlRoot;
    private MainActivity activity;
    private RequestQueue queue;
    private NoCacheImageLoader imageLoader;

    private WebRequests() {
        this.urlRoot = null;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    private static WebRequests instance;
    public static WebRequests getInstance() {
        if (instance == null) {
            instance = new WebRequests();
        }
        return instance;
    }

    public void setUrlRoot(String urlRoot) {
        this.urlRoot = urlRoot;
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
        this.queue = Volley.newRequestQueue(activity);

        // create image loader
        this.imageLoader = new NoCacheImageLoader(queue,
            new ImageLoader.ImageCache() {
                private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                @Override
                public Bitmap getBitmap(String url) {
                    //return cache.get(url);
                    return null;
                }

                @Override
                public void putBitmap(String url, Bitmap bitmap) {
                    //cache.put(url, bitmap);
                }
            });
    }

    public void testConnection(REMOTE_ACTION action) {
        String actionCode = getAction(action);
        String url = urlRoot + "?action=" + actionCode;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    activity.serverStatusUpdate(true);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    activity.serverStatusUpdate(false);
                }
        });

        // set timeout time
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(  3 * 1000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }

    public void sendAction(REMOTE_ACTION action) {
        String actionCode = getAction(action);
        String url = urlRoot + "?action=" + actionCode;

        // create request and add to queue
        sendRequest(url);
    }

    public void mouseAction(REMOTE_ACTION action, int x, int y, int speed) {
        String actionCode = getAction(action);
        String url = urlRoot + "?action=" + actionCode +
                "&x=" + x +
                "&y=" + y +
                "&speed=" + speed;

        sendRequest(url);
    }

    public void typeAction(REMOTE_ACTION action, String text) {
        String actionCode = getAction(action);
        String url = urlRoot + "?action=" + actionCode +
                "&text=" + text;
        sendRequest(url);
    }

    public void commandAction(REMOTE_ACTION action, String command) {
        String actionCode = getAction(action);
        String url = urlRoot + "?action=" + actionCode +
                "&command=" + command;
        sendRequest(url);
    }

    public void getImage(REMOTE_ACTION action, NetworkImageView image) {
        String actionCode = getAction(action);
        String url = urlRoot + "?action=" + actionCode;
        imageLoader.get(url, ImageLoader.getImageListener(image, 0, 0), 3000, 3000, ImageView.ScaleType.CENTER);
        image.setImageUrl(url, imageLoader);
    }

    private void sendRequest(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, null, null);

        // set timeout time
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(  5 * 1000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }

    private String getAction(REMOTE_ACTION action) {
        String r = null;
        switch (action) {
            case TEST:
                r = "0";
                break;
            case PLAY:
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
            case MOUSE_MOVE:
                r = "20";
                break;
            case MOUSE_LEFT:
                r = "21";
                break;
            case MOUSE_RIGHT:
                r = "22";
                break;
            case MOUSE_SCROLL:
                r = "23";
                break;
            case MOUSE_DRAG:
                r = "24";
                break;
            case SCREEN:
                r = "40";
                break;
            case SHUTDOWN:
                r = "7";
                break;
            case RESTART:
                r = "9";
                break;
            case SLEEP:
                r = "8";
                break;
            case KEYBOARD_TYPE:
                r = "31";
                break;
            case KEYBOARD_BACK:
                r = "32";
                break;
            case KEYBOARD_RETURN:
                r = "33";
                break;
            case KEYBOARD_TAB:
                r = "34";
                break;
            case KEYBOARD_ESC:
                r = "35";
                break;
            case COMMAND:
                r = "100";
                break;
        }
        return r;
    }


}




















