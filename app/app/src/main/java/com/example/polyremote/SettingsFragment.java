package com.example.polyremote;

import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import androidx.preference.SwitchPreferenceCompat;


public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);

        // test server connection
        Preference testPreference = findPreference("test");
        testPreference.setOnPreferenceClickListener(preference -> {
            WebRequests.getInstance().testConnection(WebRequests.REMOTE_ACTION.TEST);
            return true;
        });

        // listen to ip changes
        EditTextPreference ipPreference = findPreference("ip");
        ipPreference.setOnPreferenceChangeListener(this);

        // add default player
        Preference playersPreference = findPreference("default");
        playersPreference.setOnPreferenceClickListener(preference -> {
            ((MainActivity)getActivity()).addDefaultMediaPlayers();
            return true;
        });

        // notification remote
        SwitchPreferenceCompat notificationPreference = findPreference("notifications");
        notificationPreference.setOnPreferenceChangeListener(this);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.getKey().equals("ip")) {
            WebRequests.getInstance().setUrlRoot((String)newValue);
        }
        if (preference.getKey().equals("notifications")) {
            boolean active = (boolean)newValue;
            if (active) {
                ((MainActivity)getActivity()).createService();
            } else {
                ((MainActivity)getActivity()).stopService();
            }
        }
        return true;
    }
}
