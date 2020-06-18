package com.example.polyremote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.example.polyremote.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    public static final String PAGE_DATA = "PAGE_DATA";
    public static final String COMMAND_SELECTED = "COMMAND_SELECTED";
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
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        loadData();

        // set web service
        initializeService();

        // menu bar callbacks
        addNavigationCallbacks();

        // set initial fragment
        changeFragment(page);

        // create service
        if (preferences.getBoolean("notifications", true)) {
            createService();
        }
    }

    public void createService() {
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction(MusicService.ACTION_START);
        startService(intent);
    }

    public void stopService() {
        Intent intent = new Intent(this, MusicService.class);
        stopService(intent);
    }


    private void loadData() {
        page = preferences.getInt(PAGE_DATA, 0);
    }

    private void saveData() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PAGE_DATA, page);
        editor.apply();
    }

    private void initializeService() {
        WebRequests.getInstance().setActivity(this);
        WebRequests.getInstance().setUrlRoot(preferences.getString("ip", ""));
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
                fragment = new ControlFragment();
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
                fragment = new SettingsFragment();
                break;
        }

        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(binding.snackHolder.getId(), fragment);
            transaction.commit();
        }

        setSelectedAnimation(page);
    }

    private void addNavigationCallbacks() {
        binding.buttonMusic.setOnClickListener((View v) -> { changeFragment(0); });
        binding.buttonMouse.setOnClickListener((View v) -> { changeFragment(1); });
        binding.buttonKeyboard.setOnClickListener((View v) -> { changeFragment(2); });
        binding.buttonDesktop.setOnClickListener((View v) -> { changeFragment(3); });
        binding.buttonSettings.setOnClickListener((View v) -> { changeFragment(4); });

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
            msg = getString(R.string.server_connected);
        } else {
            msg = getString(R.string.server_error);
        }

        Snackbar snackbar = Snackbar.make(binding.snackHolder, msg, Snackbar.LENGTH_LONG);
//        snackbar.setTextColor(Color.WHITE);
//        snackbar.setActionTextColor(Color.GRAY);
        snackbar.setBackgroundTint(Color.parseColor("#1C1C1C"));

        snackbar.setAction("OK", (View v) -> {});

//        if (connected) {
//        } else if (page != 4) {
//            snackbar.setAction("SETTINGS", (View v) -> {
//                changeFragment(4);
//            });
//        }

        snackbar.show();
    }

    public void addDefaultMediaPlayers() {
        Resources res = getResources();
        TypedArray players = res.obtainTypedArray(R.array.players);

        for (int i = 0; i < players.length(); i++) {
            int id = players.getResourceId(i, -1);
            if (id != -1) {
                String[] player = res.getStringArray(id);
                Command cmd = new Command(player[0], player[1]);
                AppDatabase.getInstance(getApplicationContext()).commandDao().insertCommand(cmd);
            }
        }

        players.recycle();

        Snackbar snackbar = Snackbar.make(binding.snackHolder, R.string.players_added, Snackbar.LENGTH_LONG);
        snackbar.setAction("OK", (View v) -> {});
        snackbar.show();
    }

}
