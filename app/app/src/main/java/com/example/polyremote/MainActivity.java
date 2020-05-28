package com.example.polyremote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.example.polyremote.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // menu bar callbacks
        addNavigationCallbacks();

        // set initial fragment
        changeFragment(0);
    }


    private void changeFragment(int page) {
        Fragment fragment = null;

        switch (page) {
            case 0:
                fragment = new MusicFragment();
                break;
            case 1:
                fragment = new MouseFragment();
                break;
            case 2:
                fragment = new KeyboardFragment();
                break;
            case 3:
                fragment = new GamepadFragment();
                break;
            case 4:
                fragment = new ProgramsFragment();
                break;
        }

        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(binding.fragmentHolder.getId(), fragment);
            transaction.commit();
        }
    }

    private void addNavigationCallbacks() {
        binding.buttonMusic.setOnClickListener((View v) -> { changeFragment(0); });
        binding.buttonMouse.setOnClickListener((View v) -> { changeFragment(1); });
        binding.buttonKeyboard.setOnClickListener((View v) -> { changeFragment(2); });
        binding.buttonGamepad.setOnClickListener((View v) -> { changeFragment(3); });
        binding.buttonPrograms.setOnClickListener((View v) -> { changeFragment(4); });
    }

}
