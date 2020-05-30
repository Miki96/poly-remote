package com.example.polyremote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.example.polyremote.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public static final String PAGE_DATA = "PAGE_DATA";
    private ActivityMainBinding binding;
    private Toast errorToast = null;
    private int page = 0;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // load data from persistent storage
        preferences = getPreferences(MODE_PRIVATE);
        loadData();

        // set web service
        initializeService();

        // menu bar callbacks
        addNavigationCallbacks();

        // set initial fragment
        changeFragment(page);
    }

    private void loadData() {
        page = preferences.getInt(PAGE_DATA, 0);
    }

    private void saveData() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PAGE_DATA, page);
        editor.apply();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(PAGE_DATA, page);
        super.onSaveInstanceState(outState);
    }

    private void initializeService() {
        WebRequests.getInstance().setActivity(this);
        WebRequests.getInstance().setUrlRoot("http://192.168.100.97:6252/");
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    private void changeFragment(int page) {
        Fragment fragment = null;
        this.page = page;

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
                fragment = new DesktopFragment();
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

        setSelectedAnimation(page);
    }

    private void addNavigationCallbacks() {
        binding.buttonMusic.setOnClickListener((View v) -> { changeFragment(0); });
        binding.buttonMouse.setOnClickListener((View v) -> { changeFragment(1); });
        binding.buttonKeyboard.setOnClickListener((View v) -> { changeFragment(2); });
        binding.buttonGamepad.setOnClickListener((View v) -> { changeFragment(3); });
        binding.buttonPrograms.setOnClickListener((View v) -> { changeFragment(4); });

        // selected callback
        binding.selected.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.selected.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setSelectedAnimation(page);
            }
        });
    }

    private void setSelectedAnimation(int page) {
        int offset = 0;
        String transitionType = "translationX";
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            offset = binding.selected.getHeight() * page;
            transitionType = "translationY";
        } else {
            offset = binding.selected.getWidth() * page;
            transitionType = "translationX";
        }

        ObjectAnimator animation = ObjectAnimator.ofFloat(binding.selected, transitionType, offset);
        animation.setDuration(200);
        animation.start();
    }

    public void serverError(String info) {
        //String msg = "Server error: Make sure your IP is correct and server is started.";
        //Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}
