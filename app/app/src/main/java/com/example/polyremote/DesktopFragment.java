package com.example.polyremote;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.example.polyremote.databinding.FragmentDesktopBinding;


public class DesktopFragment extends Fragment {
    private FragmentDesktopBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDesktopBinding.inflate(inflater, container, false);

        // load image when view is ready
        binding.image.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.image.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setImageSource();
            }
        });

        binding.buttonRefresh.setOnClickListener((View v) -> {
            setImageSource();
        });

        return binding.getRoot();
    }

    private void setImageSource() {

        WebRequests.getInstance().getImage(WebRequests.REMOTE_ACTION.SCREEN, binding.image);

    }
}
