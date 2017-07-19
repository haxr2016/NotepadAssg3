package com.example.hemanth.notepadassg3;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import static com.example.hemanth.notepadassg3.MainActivity.NOTE_KEY;


public class NoteDetailActivity extends AppCompatActivity {

    public static String TAG = "NoteDetailActivity";
    public static final String TYPE = "type";
    public static final String ADD_NOTE = "addnote";
    public static final String VIEW_NOTE = "viewnote";
    private Toolbar toolbar;
    private EditText title;
    private TextView luTime;
    private EditText noteEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.note_detail_acivity);

        NoteDetailActivity.this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        title = (EditText) findViewById(R.id.title);
        noteEt = (EditText) findViewById(R.id.content);
        luTime = (TextView) findViewById(R.id.lu_time);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent().getStringExtra(TYPE).equals(ADD_NOTE)) {
            toolbar.setTitle("Add Note");


        } else if (getIntent().getStringExtra(TYPE).equals(VIEW_NOTE)) {

            Note note = (Note) getIntent().getParcelableExtra(NOTE_KEY);
            title.setText(note.getTitle());
            noteEt.setText(note.getContent());
            luTime.setText(note.getLu_time() + "");
        }

    }


}
