package com.example.polyremote;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.polyremote.databinding.FragmentKeyboardBinding;


public class KeyboardFragment extends Fragment {

    private FragmentKeyboardBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentKeyboardBinding.inflate(inflater, container, false);

        // add button callbacks
        createButtonCallbacks();

        return binding.getRoot();
    }

    private void createButtonCallbacks() {
        addAction(binding.buttonBack, WebRequests.REMOTE_ACTION.KEYBOARD_BACK);
        addAction(binding.buttonEnter, WebRequests.REMOTE_ACTION.KEYBOARD_RETURN);
        addAction(binding.buttonTab, WebRequests.REMOTE_ACTION.KEYBOARD_TAB);
        addAction(binding.buttonEsc, WebRequests.REMOTE_ACTION.KEYBOARD_ESC);

        // clear text
        binding.buttonCancel.setOnClickListener((View v) -> {
            binding.editTextInput.setText("");
        });

        // send text
        binding.buttonType.setOnClickListener((View v) -> {
            WebRequests.getInstance().typeAction(
                    WebRequests.REMOTE_ACTION.KEYBOARD_TYPE,
                    binding.editTextInput.getText().toString());
        });
    }

    private void addAction(ImageButton button, WebRequests.REMOTE_ACTION action) {
        button.setOnClickListener((View v) -> {
            WebRequests.getInstance().sendAction(action);
        });
    }
}
