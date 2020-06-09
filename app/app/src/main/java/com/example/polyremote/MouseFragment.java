package com.example.polyremote;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.polyremote.databinding.FragmentMouseBinding;


public class MouseFragment extends Fragment implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

    private FragmentMouseBinding binding;
    private static final String DEBUG_TAG = "Gestures";
    private GestureDetectorCompat mDetector;

    // scrolling
    private int scrollSpeed;
    private int mouseSpeed;

    // drag
    private int dragSpeed;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // touch callbacks
        mDetector = new GestureDetectorCompat(this.getContext(), this);
        mDetector.setOnDoubleTapListener(this);
        //mDetector.setIsLongpressEnabled(false);

        // scroll initialize
        scrollSpeed = 1;
        mouseSpeed = 1;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mouseSpeed = sharedPreferences.getInt("mouse_speed", 1);

        // drag initialize
        dragSpeed = 1;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMouseBinding.inflate(inflater, container, false);

        // add touch callbacks
        binding.panel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mDetector.onTouchEvent(event)) {
                    return true;
                }
                return false;
            }
        });

        return binding.getRoot();
    }

    @Override
    public boolean onDown(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        return true;
    }

    // Mouse right click
    @Override
    public void onLongPress(MotionEvent event) {
        WebRequests.getInstance().sendAction(WebRequests.REMOTE_ACTION.MOUSE_RIGHT);
    }

    // Mouse scroll
    @Override
    public boolean onScroll(MotionEvent event1, MotionEvent event2, float distX, float distY) {
        if (event2.getPointerCount() == 2 && distY != 0) {
            // mouse scroll
            WebRequests.getInstance().mouseAction(WebRequests.REMOTE_ACTION.MOUSE_SCROLL,
                    0, -(int)distY, scrollSpeed);
        } else {
            // mouse move
            WebRequests.getInstance().mouseAction(WebRequests.REMOTE_ACTION.MOUSE_MOVE,
                    -(int)distX, -(int)distY, mouseSpeed);

        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) { }

    // Mouse click
    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        WebRequests.getInstance().sendAction(WebRequests.REMOTE_ACTION.MOUSE_LEFT);
        return true;
    }

    // Mouse double click
    @Override
    public boolean onDoubleTap(MotionEvent event) {
        WebRequests.getInstance().sendAction(WebRequests.REMOTE_ACTION.MOUSE_LEFT);
        return false;
    }

    // Mouse drag
    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_MOVE) {
//            int x = 3;
//            int y = 3;
//            WebRequests.getInstance().mouseAction(WebRequests.REMOTE_ACTION.MOUSE_DRAG,
//                    -x, -y, dragSpeed);
//        }
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        return true;
    }
}
