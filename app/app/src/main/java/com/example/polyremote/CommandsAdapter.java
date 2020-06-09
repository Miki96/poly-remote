package com.example.polyremote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.PrimaryKey;


public class CommandsAdapter extends ListAdapter<Command, CommandsAdapter.CommandsViewHolder> {

    public static class CommandsViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView command;
        private Context context;
        private CommandsAdapter adapter;
        public CommandsViewHolder(CommandsAdapter a, Context c, View v) {
            super(v);
            title = v.findViewById(R.id.text_title);
            command = v.findViewById(R.id.text_command);
            context = c;
            adapter = a;

            // delete
            ImageButton button = v.findViewById(R.id.button_remove);
            button.setOnClickListener((View vv) -> {
                ((CommandActivity)context).onDeleteCommand(adapter.getItem(getAdapterPosition()));
            });

            // edit
            v.setOnClickListener((View vv) -> {
                ((CommandActivity)context).onEditCommand(adapter.getItem(getAdapterPosition()));
            });
        }
    }

    public CommandsAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Command> DIFF_CALLBACK =
        new DiffUtil.ItemCallback<Command>() {
            @Override
            public boolean areItemsTheSame(@NonNull Command oldCommand, @NonNull Command newCommand) {
                return oldCommand.commandId == newCommand.commandId;
            }
            @Override
            public boolean areContentsTheSame(@NonNull Command oldCommand, @NonNull Command newCommand) {
                return oldCommand.title.equals(newCommand.title) & oldCommand.command.equals(newCommand.command);
            }
        };

    @Override
    public CommandsAdapter.CommandsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_command, parent, false);
        return new CommandsViewHolder(this, parent.getContext(), view);
    }

    @Override
    public void onBindViewHolder(CommandsViewHolder holder, int position) {
        holder.title.setText(getItem(position).title);
        holder.command.setText(getItem(position).command);
    }

}
