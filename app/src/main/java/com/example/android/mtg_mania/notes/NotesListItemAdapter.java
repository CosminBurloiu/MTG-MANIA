package com.example.android.mtg_mania.notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.mtg_mania.R;

import java.util.ArrayList;

public class NotesListItemAdapter extends RecyclerView.Adapter<NotesListItemAdapter.ViewHolder> {

    private ArrayList<NotesListItem> items;

    public NotesListItemAdapter(ArrayList<NotesListItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesListItemAdapter.ViewHolder holder, int position) {

        NotesListItem notesListItem = items.get(position);

        holder.title.setText(notesListItem.getTitle());
        holder.noteText.setText(notesListItem.getNoteText());
        holder.date.setText(notesListItem.getDate());
        holder.time.setText(notesListItem.getTime());

        holder.deleteButton.setTag(notesListItem.getId());
        holder.editButton.setTag(notesListItem.getId());
    }

    public void setItems(ArrayList<NotesListItem> receivedItems) {
        items = receivedItems;
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        } else {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView title;
        public final TextView noteText;
        public final TextView date;
        public final TextView time;

        public final ImageButton deleteButton;
        public final ImageButton editButton;


        public ViewHolder(View view) {
            super(view);
            this.view = view;

            title = view.findViewById(R.id.title);
            noteText = view.findViewById(R.id.noteText);
            date = view.findViewById(R.id.date);
            time = view.findViewById(R.id.time);

            deleteButton = view.findViewById(R.id.deleteButton);
            editButton = view.findViewById(R.id.editButton);
        }
    }
}
