package com.example.hemanth.notepadassg3.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.hemanth.notepadassg3.extras.AppConstants;
import com.example.hemanth.notepadassg3.models.Note;
import com.example.hemanth.notepadassg3.adapter.NotesRvAdapter;
import com.example.hemanth.notepadassg3.R;
import com.example.hemanth.notepadassg3.listners.RecyclerTouchListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String NOTE_KEY = "note_key";
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
                showDeleteDialog(noteList.get(position));
            }
        }));
        noteList = new ArrayList<>();

    }

    private void showDeleteDialog(final Note note) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Delete Note")
                .setMessage("Do you want to delete Note?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteNote(note);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void deleteNote(Note note) {
        File file = new File(note.getFilePath());
        if (!file.delete()) {
            Toast.makeText(MainActivity.this, "Couldn't deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            new LoadFiles().execute();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new LoadFiles().execute();
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
        intent.putExtra(NoteDetailActivity.TYPE, NoteDetailActivity.ADD_NOTE);
        startActivity(intent);
    }

    private void openNoteDetailActivity(Note note) {
        Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
        intent.putExtra(NoteDetailActivity.TYPE, NoteDetailActivity.VIEW_NOTE);
        intent.putExtra(NOTE_KEY, note);
        startActivity(intent);
    }

    private void openAboutActivity() {
        startActivity(new Intent(MainActivity.this, AboutActivity.class));
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


    public class LoadFiles extends AsyncTask<Void, Void, ArrayList<Note>> {

        @Override
        protected ArrayList<Note> doInBackground(Void... nothing) {

            ArrayList<Note> notes = new ArrayList<>();
            File notesDir = new File(MainActivity.this.getApplicationContext().getFilesDir().getAbsolutePath() + AppConstants.NOTES_DIRECTORY);
            if (!notesDir.exists() && !notesDir.isDirectory()) {
                return null;
            }

            File[] fileList = notesDir.listFiles();
            try {
                for (File f : fileList) {

                    if (f.isFile() && f.getPath().endsWith(".json")) {
                        FileInputStream in = new FileInputStream(f);
                        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
                        Note note = readNote(reader);
                        if (note != null)
                            notes.add(note);
                        reader.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return notes;
        }

        public Note readNote(JsonReader reader) throws IOException {
            Note note = new Note();
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals(Note.ID)) {
                    note.setId(reader.nextString());
                } else if (name.equals(Note.TITLE)) {
                    note.setTitle(reader.nextString());
                } else if (name.equals(Note.CONTENT)) {
                    note.setContent(reader.nextString());
                } else if (name.equals(Note.FILEPATH)) {
                    note.setFilePath(reader.nextString());
                } else if (name.equals(Note.LU_TIME)) {
                    note.setLu_time(reader.nextString());
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            return note;
        }

        @Override
        protected void onPostExecute(ArrayList<Note> notes) {

            if (notes != null) {
                noteList.clear();
                noteList.addAll(notes);
                Collections.sort(noteList, new Comparator<Note>() {
                    @Override
                    public int compare(Note note, Note t1) {
                        return Long.compare(Long.parseLong(t1.getLu_time()), Long.parseLong(note.getLu_time()));
                    }
                });
            }
            if (noteList.size() == 0) {
                noNoteLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                return;
            } else {
                noNoteLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                NotesRvAdapter mAdapter = new NotesRvAdapter(noteList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);

            }

        }
    }

}
