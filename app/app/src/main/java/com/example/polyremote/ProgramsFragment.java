package com.example.polyremote;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.polyremote.databinding.FragmentProgramsBinding;


public class ProgramsFragment extends Fragment {

    private FragmentProgramsBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProgramsBinding.inflate(inflater, container, false);

        // add button callbacks
        createButtonCallbacks();

        return binding.getRoot();
    }

    private void createButtonCallbacks() {
        addAction(binding.buttonShutdown, WebRequests.REMOTE_ACTION.SHUTDOWN);
        addAction(binding.buttonSleep, WebRequests.REMOTE_ACTION.SLEEP);
        addAction(binding.buttonRestart, WebRequests.REMOTE_ACTION.RESTART);
    }

    private void addAction(ImageButton button, WebRequests.REMOTE_ACTION action) {
        button.setOnClickListener((View v) -> {
            WebRequests.getInstance().sendAction(action);
        });
    }

}
