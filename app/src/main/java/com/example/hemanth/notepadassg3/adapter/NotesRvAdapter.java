package com.example.hemanth.notepadassg3.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hemanth.notepadassg3.models.Note;
import com.example.hemanth.notepadassg3.R;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;


public class NotesRvAdapter extends RecyclerView.Adapter<NotesRvAdapter.MyViewHolder> {

    private List<Note> noteList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, content, luTime;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            content = (TextView) view.findViewById(R.id.content);
            luTime = (TextView) view.findViewById(R.id.lu_time);
        }
    }


    public NotesRvAdapter(List<Note> noteList) {
        this.noteList = noteList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Note note = noteList.get(position);
        if (note != null) {
            holder.title.setText(note.getTitle());
            holder.content.setText(getEllipsizeContent(note.getContent()));

            Date netDate = new Date(Long.parseLong(note.getLu_time()));
            holder.luTime.setText(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(netDate));

        }
    }

    private String getEllipsizeContent(String content) {
        if (content.length() > 80) {
            return (content.substring(0, 77) + "...");
        } else {
            return content;
        }
    }

    @Override
    public int getItemCount() {
        return (noteList != null) ? noteList.size() : 0;
    }
}