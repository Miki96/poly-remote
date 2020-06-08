package com.example.polyremote;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import com.example.polyremote.databinding.FragmentControlBinding;

import java.util.Random;


public class ControlFragment extends Fragment {
    private FragmentControlBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentControlBinding.inflate(inflater, container, false);

        // buttons
        createButtonCallbacks();

        // populate spinner
        populateSpinner();

        return binding.getRoot();
    }

    private void populateSpinner() {

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.planets_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        binding.spinner.setAdapter(adapter);

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
