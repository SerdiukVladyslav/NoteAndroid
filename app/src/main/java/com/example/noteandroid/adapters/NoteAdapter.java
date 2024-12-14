package com.example.noteandroid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteandroid.R;
import com.example.noteandroid.data.*;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private Context context;
    private List<Note> noteList;

    public NoteAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.titleTextView.setText(note.getTitle());
        holder.contentTextView.setText(note.getContent());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView contentTextView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
        }
    }
}
