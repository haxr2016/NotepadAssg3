package com.example.hemanth.notepadassg3;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        Note movie = noteList.get(position);
        holder.title.setText(movie.getTitle());
        holder.content.setText(movie.getContent());
        holder.luTime.setText(movie.getLu_time() + "");
    }

    @Override
    public int getItemCount() {
        return (noteList != null) ? noteList.size() : 0;
    }
}