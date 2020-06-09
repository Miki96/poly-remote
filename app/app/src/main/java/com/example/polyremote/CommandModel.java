package com.example.polyremote;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class CommandModel extends AndroidViewModel {

    public final LiveData<List<Command>> commandsList;

    public CommandModel(@NonNull Application application) {
        super(application);
        commandsList = AppDatabase.getInstance(application.getApplicationContext()).commandDao().getAllCommands();
    }

}
