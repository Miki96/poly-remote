package com.example.polyremote;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "commands")
public class Command {

    @ColumnInfo(name = "command_id")
    @PrimaryKey(autoGenerate = true)
    public Integer commandId;

    public String title;
    public String command;

    public Command(String title, String command) {
        this.title = title;
        this.command = command;
    }

    @NonNull
    @Override
    public String toString() {
        return title;
    }
}
