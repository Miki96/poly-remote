package com.example.polyremote;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class CommandDialogFragment extends DialogFragment {

    public interface CommandDialogListener {
        public void onCommandPositiveClick(CommandDialogFragment dialog);
    }

    public enum Type {
        CREATE_DIALOG,
        UPDATE_DIALOG
    }

    private CommandDialogListener listener;
    public Type type;
    public Command cmd;

    public CommandDialogFragment(Type type, Command cmd) {
        this.type = type;
        this.cmd = cmd;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (CommandDialogListener) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_command, null))
                .setTitle("Create Command")
                .setPositiveButton("SAVE", (dialog, id) -> {
                    listener.onCommandPositiveClick(this);
                })
                .setNegativeButton("CANCEL", (dialog, id) -> {});
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        setRetainInstance(true);

        if (cmd != null) {
            EditText textTitle = getDialog().findViewById(R.id.text_title);
            EditText textCommand = getDialog().findViewById(R.id.text_command);
            textTitle.setText(cmd.title);
            textCommand.setText(cmd.command);
        }
    }
}
