package com.example.hemanth.notepadassg3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import static com.example.hemanth.notepadassg3.NoteDetailActivity.ADD_NOTE;
import static com.example.hemanth.notepadassg3.NoteDetailActivity.TYPE;
import static com.example.hemanth.notepadassg3.NoteDetailActivity.VIEW_NOTE;

public class MainActivity extends AppCompatActivity {

    public static String NOTE_KEY="note_key";
    private List<Note> noteList;
    private RecyclerView recyclerView;
    private RelativeLayout noNoteLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.notes_rv);
        noNoteLayout = (RelativeLayout) findViewById(R.id.no_note_layout);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                openNoteDetailActivity(noteList.get(position));
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        noteList = new ArrayList<>();

        prepareNoteData();

        if(noteList.size() == 0){
            noNoteLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            noNoteLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            NotesRvAdapter mAdapter = new NotesRvAdapter(noteList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this );
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
        }

    }


    private void prepareNoteData() {
        Note movie = new Note("Mad Max: Fury Road", "Action & Adventure", "2015");
        noteList.add(movie);

        movie = new Note("Inside Out", "Animation, Kids & Family", "2015");
        noteList.add(movie);

        movie = new Note("Star Wars: Episode VII - The Force Awakens", "Action", "2015");
        noteList.add(movie);

        movie = new Note("Shaun the Sheep", "Animation", "2015");
        noteList.add(movie);

        movie = new Note("The Martian", "Science Fiction & Fantasy", "2015");
        noteList.add(movie);

        movie = new Note("Mission: Impossible Rogue Nation", "Action", "2015");
        noteList.add(movie);

        movie = new Note("Up", "Animation", "2009");
        noteList.add(movie);

        movie = new Note("Star Trek", "Science Fiction", "2009");
        noteList.add(movie);

        movie = new Note("The LEGO Note", "Animation", "2014");
        noteList.add(movie);

        movie = new Note("Iron Man", "Action & Adventure", "2008");
        noteList.add(movie);

        movie = new Note("Aliens", "Science Fiction", "1986");
        noteList.add(movie);

        movie = new Note("Chicken Run", "Animation", "2000");
        noteList.add(movie);

        movie = new Note("Back to the Future", "Science Fiction", "1985");
        noteList.add(movie);

        movie = new Note("Raiders of the Lost Ark", "Action & Adventure", "1981");
        noteList.add(movie);

        movie = new Note("Goldfinger", "Action & Adventure", "1965");
        noteList.add(movie);

        movie = new Note("Guardians of the Galaxy", "Science Fiction & Fantasy", "2014");
        noteList.add(movie);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.note_detail:
                openAddNoteActivity();
                break;
            case R.id.about:
                openAboutActivity();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openAddNoteActivity() {

        Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
        intent.putExtra(TYPE,ADD_NOTE);
        startActivity(intent);
    }

    private void openNoteDetailActivity(Note note) {
        Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
        intent.putExtra(TYPE, VIEW_NOTE);
        intent.putExtra(NOTE_KEY,note);
        startActivity(intent);
    }

    private void openAboutActivity() {
        startActivity(new Intent(MainActivity.this, AboutActivity.class));
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

}
