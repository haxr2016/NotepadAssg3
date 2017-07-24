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
import android.util.JsonWriter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.hemanth.notepadassg3.models.Note;
import com.example.hemanth.notepadassg3.adapter.NotesRvAdapter;
import com.example.hemanth.notepadassg3.R;
import com.example.hemanth.notepadassg3.listners.RecyclerTouchListener;
import com.example.hemanth.notepadassg3.utils.JsonReadWriteUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
        new DeleteNote().execute(note);
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


    public class DeleteNote extends AsyncTask<Note, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Note... notes) {

            try {

                File filesDir = new File(MainActivity.this.getApplicationContext().getFilesDir().getAbsolutePath());

                File noteFile = new File(filesDir.getAbsolutePath() + "/" + "notes.json");

                if (!noteFile.exists()) {
                    if (!noteFile.createNewFile()) {
                        return false;
                    } else {
                        InputStream inputStream = new FileInputStream(noteFile);
                        OutputStream outputStream = new FileOutputStream(noteFile);
                        JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
                        JsonWriter writer = new JsonWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                        try {
                            readNotesArray(reader, writer, notes[0]);
                        } finally {
                            writer.flush();
                            reader.close();
                            writer.close();
                        }
                    }
                }

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        private void readNotesArray(JsonReader reader, JsonWriter writer, Note note) throws IOException {

            reader.beginArray();
            writer.beginArray();
            while (reader.hasNext()) {

                if (readNote(reader, writer, note)) {
                    deleteNotes(writer, note);
                    return;
                }
                writer.beginObject();
                writer.endObject();
            }
            reader.endArray();
            writer.endArray();
        }

        private void deleteNotes(JsonWriter writer, Note note) throws IOException {

            writer.beginObject();
            writer.nullValue();
            writer.endObject();
        }


        public Boolean readNote(JsonReader reader, JsonWriter writer, Note note) throws IOException {
            String id = "";

            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("id")) {
                    id = reader.nextString();
                    if (id.equalsIgnoreCase(note.getId())) {
                        return true;
                    }

                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            return false;
        }


        @Override
        protected void onPostExecute(Boolean deleted) {

            if (!deleted) {
                Toast.makeText(MainActivity.this, "Couldn't deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
                new LoadFiles().execute();
            }
        }
    }

    public class LoadFiles extends AsyncTask<Void, Void, ArrayList<Note>> {

        @Override
        protected ArrayList<Note> doInBackground(Void... nothing) {

            ArrayList<Note> notes = new ArrayList<>();

            File filesDir = new File(MainActivity.this.getApplicationContext().getFilesDir().getAbsolutePath());

            File noteFile = new File(filesDir.getAbsolutePath() + "/" + "notes.json");

            if (noteFile.exists()) {
                String data = JsonReadWriteUtils.readFromFile(noteFile);

                try {
                    JSONArray jsonArray = new JSONArray(data);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        if (object != null)
                            notes.add(Note.getModelFromJson(object));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return notes;
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
