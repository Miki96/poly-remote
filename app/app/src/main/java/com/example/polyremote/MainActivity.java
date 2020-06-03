package com.example.polyremote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.animation.ObjectAnimator;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.example.polyremote.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

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

        // create service
        createService();
    }

    private void createService() {
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction(MusicService.ACTION_START);
        startService(intent);
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
        // test connection
        WebRequests.getInstance().testConnection(WebRequests.REMOTE_ACTION.MOUSE_RIGHT);
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
            case 5:
                fragment = new SettingsFragment();
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
        binding.buttonDesktop.setOnClickListener((View v) -> { changeFragment(3); });
        binding.buttonPower.setOnClickListener((View v) -> { changeFragment(4); });
        binding.buttonSettings.setOnClickListener((View v) -> { changeFragment(5); });

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

    public void serverStatusUpdate(boolean connected) {
        String msg;
        if (connected) {
            msg = "Server connected. Start using your remote";
        } else {
            msg = "Server error. Check IP in settings.";
        }

        Snackbar snackbar = Snackbar.make(binding.snackHolder, msg, Snackbar.LENGTH_LONG);
        snackbar.setTextColor(Color.WHITE);
        snackbar.setActionTextColor(Color.GRAY);

        if (connected) {
            snackbar.setAction("OK", (View v) -> {
            });
        } else if (page != 5) {
            snackbar.setAction("SETTINGS", (View v) -> {
                changeFragment(5);
            });
        }

        snackbar.show();

        Log.d("MIKI", "STATUS");

    }

}
