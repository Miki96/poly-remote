package com.example.polyremote;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.polyremote.databinding.FragmentMusicBinding;

import java.util.Random;


public class MusicFragment extends Fragment {
    private FragmentMusicBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMusicBinding.inflate(inflater, container, false);

        // buttons
        createButtonCallbacks();

        return binding.getRoot();
    }

    private void createButtonCallbacks() {
        addAction(binding.buttonPlay, WebRequests.REMOTE_ACTION.PLAY);
        addAction(binding.buttonPrev, WebRequests.REMOTE_ACTION.PREVIOUS);
        addAction(binding.buttonNext, WebRequests.REMOTE_ACTION.NEXT);

        addAction(binding.buttonVolDown, WebRequests.REMOTE_ACTION.VOLUME_DOWN);
        addAction(binding.buttonVolUp, WebRequests.REMOTE_ACTION.VOLUME_UP);
        addAction(binding.buttonVolMute, WebRequests.REMOTE_ACTION.MUTE);
    }

    private void addAction(ImageButton button, WebRequests.REMOTE_ACTION action) {
        button.setOnClickListener((View v) -> {
            WebRequests.getInstance().sendAction(action);
        });
    }
}
