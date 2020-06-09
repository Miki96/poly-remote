package com.example.polyremote;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.polyremote.databinding.FragmentControlBinding;

import java.util.Random;


public class ControlFragment extends Fragment {
    private FragmentControlBinding binding;
    private CommandModel commandModel;
    private int selected = -1;
    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        preferences = getActivity().getPreferences(getActivity().MODE_PRIVATE);
        loadData();
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

        commandModel = new ViewModelProvider(this).get(CommandModel.class);

        ArrayAdapter<Command> adapter = new ArrayAdapter<Command>(getContext(), android.R.layout.simple_list_item_1);

        commandModel.commandsList.observe(getActivity(), (list) -> {
            adapter.clear();
            adapter.addAll(list);
            if (selected != -1)
                binding.spinner.setSelection(selected);
        });

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);

        // add on click event
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected = -1;
            }
        });
    }

    private void createButtonCallbacks() {
        addAction(binding.buttonPlay, WebRequests.REMOTE_ACTION.PLAY);
        addAction(binding.buttonPrev, WebRequests.REMOTE_ACTION.PREVIOUS);
        addAction(binding.buttonNext, WebRequests.REMOTE_ACTION.NEXT);

        addAction(binding.buttonVolDown, WebRequests.REMOTE_ACTION.VOLUME_DOWN);
        addAction(binding.buttonVolUp, WebRequests.REMOTE_ACTION.VOLUME_UP);
        addAction(binding.buttonVolMute, WebRequests.REMOTE_ACTION.MUTE);

        addAction(binding.buttonShutdown, WebRequests.REMOTE_ACTION.SHUTDOWN);
        addAction(binding.buttonRestart, WebRequests.REMOTE_ACTION.RESTART);
        addAction(binding.buttonSleep, WebRequests.REMOTE_ACTION.SLEEP);

        // edit commands
        binding.buttonCommands.setOnClickListener((View v) -> {
            Intent intent = new Intent(getContext(), CommandActivity.class);
            startActivity(intent);
        });

        // launcher
        binding.buttonLaunch.setOnClickListener((View v) -> {
            Command cmd = (Command) binding.spinner.getSelectedItem();
            if (cmd != null) {
                WebRequests.getInstance().commandAction(WebRequests.REMOTE_ACTION.COMMAND, cmd.command);
            }
        });
    }

    private void addAction(ImageButton button, WebRequests.REMOTE_ACTION action) {
        button.setOnClickListener((View v) -> {
            WebRequests.getInstance().sendAction(action);
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        saveData();
    }

    private void saveData() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(MainActivity.COMMAND_SELECTED, selected);
        editor.apply();
    }

    private void loadData() {
        selected = preferences.getInt(MainActivity.COMMAND_SELECTED, -1);
    }
}
