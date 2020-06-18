package com.example.polyremote;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DeleteDialogFragment extends DialogFragment {

    public Command cmd;

    public DeleteDialogFragment(Command cmd) {
        this.cmd = cmd;
    }

    public interface DeleteDialogListener {
        public void onDeletePositiveClick(Command cmd);
    }

    DeleteDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (DeleteDialogListener) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(Html.fromHtml(getString(R.string.dialog_delete_delete) +
                " <font color='#EE0000'>" + cmd.title + "</font> " +
                getString(R.string.dialog_delete_command) + "?" ))
                .setPositiveButton(R.string.dialog_delete_confirm, (dialog, id) -> {
                    listener.onDeletePositiveClick(cmd);
                })
                .setNegativeButton(R.string.dialog_delete_cancel, (dialog, id) -> {});
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        setRetainInstance(true);
    }

}
