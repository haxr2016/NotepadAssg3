package com.example.hemanth.notepadassg3.activities;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hemanth.notepadassg3.extras.AppConstants;
import com.example.hemanth.notepadassg3.models.Note;
import com.example.hemanth.notepadassg3.R;

import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

public class NoteDetailActivity extends AppCompatActivity {

    public static String TAG = "NoteDetailActivity";
    public static final String TYPE = "type";
    public static final String ADD_NOTE = "addnote";
    public static final String VIEW_NOTE = "viewnote";
    private boolean noteChanged = false;
    private Toolbar toolbar;
    private EditText title;
    private TextView luTime;
    private EditText noteEt;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.note_detail_acivity);

        NoteDetailActivity.this.overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left);

        title = (EditText) findViewById(R.id.title);
        noteEt = (EditText) findViewById(R.id.content);
        luTime = (TextView) findViewById(R.id.lu_time);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        if (getIntent().getStringExtra(TYPE).equals(ADD_NOTE)) {
            getSupportActionBar().setTitle("Add Note");
            note = new Note();
            note.setLu_time("" + System.currentTimeMillis());
            Date netDate = new Date(Long.parseLong(note.getLu_time()));
            luTime.setText(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(netDate));

        } else if (getIntent().getStringExtra(TYPE).equals(VIEW_NOTE)) {
            getSupportActionBar().setTitle("Edit Note");

            note = (Note) getIntent().getParcelableExtra(MainActivity.NOTE_KEY);
            title.setText(note.getTitle());
            noteEt.setText(note.getContent());

            Date netDate = new Date(Long.parseLong(note.getLu_time()));
            luTime.setText(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(netDate));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notedetail, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!note.getTitle().equalsIgnoreCase(editable.toString())) {
                    noteChanged = true;
                }
            }
        });

        noteEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!note.getContent().equalsIgnoreCase(editable.toString())) {
                    noteChanged = true;
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.note_save:
                saveNote();
                return true;
            case android.R.id.home:
                if (noteChanged)
                    showSaveDialog();
                else NoteDetailActivity.this.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSaveDialog() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(NoteDetailActivity.this);
        builder.setTitle("Save Note")
                .setMessage("Your note is not saved.\n Do you want to save?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        saveNote();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        NoteDetailActivity.this.onBackPressed();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void saveNote() {
        if(title.getText().toString().isEmpty()){
            Toast.makeText(NoteDetailActivity.this, "Un titled Note cant be saved", Toast.LENGTH_SHORT).show();
            NoteDetailActivity.this.onBackPressed();
            return;
        }
        note.setTitle(title.getText().toString());
        note.setContent(noteEt.getText().toString());
        note.setLu_time("" + System.currentTimeMillis());
        new SaveFile().execute(note);
    }


    public class SaveFile extends AsyncTask<Note, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Note... notes) {

            try {

                File dir = new File(NoteDetailActivity.this.getApplicationContext().getFilesDir().getAbsolutePath() + AppConstants.NOTES_DIRECTORY);
                if (!dir.exists()) {
                    if (!dir.mkdir())
                        return false;
                }
                String fileName = notes[0].getFilePath();
                if (fileName != null && fileName.isEmpty()) {
                    fileName = "note_" + UUID.randomUUID().toString() + ".json";
                    notes[0].setFilePath(dir.getAbsolutePath() + File.separator + fileName);
                }

                File data = new File(notes[0].getFilePath());

                if (!data.exists()) {
                    if (!data.createNewFile()) {
                        return false;
                    }
                }
                String jsonData = Note.getJsonFromModel(notes[0]);

                FileWriter file = new FileWriter(data);
                file.write(jsonData);
                file.flush();
                file.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                Toast.makeText(NoteDetailActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();
                NoteDetailActivity.this.onBackPressed();
            } else {
                Toast.makeText(NoteDetailActivity.this, "Couldn't Save Note", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
