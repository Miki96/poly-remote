package com.example.polyremote;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CommandDao {

    @Insert
    void insertCommand(Command... commands);

    @Update
    void updateCommand(Command... commands);

    @Delete
    void deleteCommand(Command... commands);

    @Query("SELECT * FROM commands")
    LiveData<List<Command>> getAllCommands();
}
