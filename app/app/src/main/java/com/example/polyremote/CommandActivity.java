package com.example.polyremote;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.polyremote.databinding.ActivityCommandBinding;

import java.util.ArrayList;
import java.util.List;

public class CommandActivity extends AppCompatActivity implements
        CommandDialogFragment.CommandDialogListener,
        DeleteDialogFragment.DeleteDialogListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ActivityCommandBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCommandBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addButtonCallbacks();

        recyclerSetup();
    }

    private void recyclerSetup() {
        recyclerView = binding.listCommands;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        CommandModel commandModel = new ViewModelProvider(this).get(CommandModel.class);
        CommandsAdapter adapter = new CommandsAdapter();

        commandModel.commandsList.observe(this, list -> adapter.submitList(list));

        recyclerView.setAdapter(adapter);
    }

    private void addButtonCallbacks() {

        binding.buttonBack.setOnClickListener((View v) -> {
            super.onBackPressed();
        });

        binding.buttonAdd.setOnClickListener((View v) -> {
            CommandDialogFragment dialogFragment = new CommandDialogFragment(CommandDialogFragment.Type.CREATE_DIALOG, null);
            dialogFragment.show(getSupportFragmentManager(), "CommandDialog");
        });
    }


    @Override
    public void onCommandPositiveClick(CommandDialogFragment dialog) {
        EditText textTitle = dialog.getDialog().findViewById(R.id.text_title);
        EditText textCommand = dialog.getDialog().findViewById(R.id.text_command);
        String title = textTitle.getText().toString();
        String command = textCommand.getText().toString();

        if (!title.equals("") && !command.equals("")) {

            Command cmd = new Command(title, command);
            if (dialog.type == CommandDialogFragment.Type.CREATE_DIALOG) {
                AppDatabase.getInstance(getApplicationContext()).commandDao().insertCommand(cmd);
            } else {
                cmd.commandId = dialog.cmd.commandId;
                AppDatabase.getInstance(getApplicationContext()).commandDao().updateCommand(cmd);
            }
        }
    }

    public void onDeleteCommand(Command cmd) {
        // create confirm dialog
        DeleteDialogFragment dialogFragment = new DeleteDialogFragment(cmd);
        dialogFragment.show(getSupportFragmentManager(), "DeleteDialog");
    }

    public void onEditCommand(Command cmd) {
        CommandDialogFragment dialogFragment = new CommandDialogFragment(CommandDialogFragment.Type.UPDATE_DIALOG, cmd);
        dialogFragment.show(getSupportFragmentManager(), "CommandDialog");
    }

    @Override
    public void onDeletePositiveClick(Command cmd) {
        AppDatabase.getInstance(getApplicationContext()).commandDao().deleteCommand(cmd);
    }
}
