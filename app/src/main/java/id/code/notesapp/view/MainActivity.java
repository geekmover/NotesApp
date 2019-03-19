package id.code.notesapp.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.code.notesapp.R;
import id.code.notesapp.database.DatabaseHelper;
import id.code.notesapp.database.model.Note;
import id.code.notesapp.utils.RecyclerTouchListener;

public class MainActivity extends AppCompatActivity {

    // Step 1:
    private NotesAdapter notesAdapter;
    private List<Note> noteList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView rv_listnote;
    private TextView noNotesView;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Step 2:
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        // Step 3:
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinator_layout_main);
        rv_listnote = (RecyclerView)findViewById(R.id.rv_list);
        noNotesView = (TextView)findViewById(R.id.txt_emptyfound);

        // Step 4:
        db = new DatabaseHelper(this);
        noteList.addAll(db.getAllNotes());

        // Step 5:
        FloatingActionButton fab_newnotes = (FloatingActionButton)findViewById(R.id.fab_add);
        fab_newnotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Step 12: Isi Parameter:
                showNoteDialog(false, null, -1);
            }
        });

        // Step 13: Set Adapter untuk Menampilkan Data Kedalam RecyclerView
        notesAdapter = new NotesAdapter(this, noteList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rv_listnote.setLayoutManager(layoutManager);

        rv_listnote.setAdapter(notesAdapter);

        toggleEmptyNote();

        // Step 14: Menambahkan Package baru di project package java dengan nama utils
        //          kemudian tambahkan file class RecyclerTouchListener.java didalam package
        //          utils yang sudah kita buat tadi

        // Step 5: Menambahkan Action Click pada List RecyclerView data yang Ada (OnClick)

        rv_listnote.addOnItemTouchListener(new RecyclerTouchListener(
                this, rv_listnote, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int poisiton) {

            }
        }));

    }

    // Step 6:
    private void showNoteDialog(final boolean shouldUpdate, final Note note, final int position) {
        LayoutInflater layoutInflaterAddNote = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAddNote.inflate(R.layout.add_note_dialog, null);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
        alertBuilder.setView(view);

        final EditText getInputNote = view.findViewById(R.id.edt_new_note);
        TextView dialogTitle = view.findViewById(R.id.dialog_add_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.add_set) : getString(R.string.edit_set));

        if (shouldUpdate && note != null) {
            getInputNote.setText(note.getNote());
        }

        alertBuilder
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialoBox, int id) {

                    }
                });

        final AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(getInputNote.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Bro, Masih Kosong! Isi dulu.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                if (shouldUpdate && note != null) {
                    // Step 7: Buat Dulu Mehtod Update
                    updateNote(getInputNote.getText().toString(), position);
                } else {
                    // Step 10: Buat Dulu Mehtod Create Data
                    createNote(getInputNote.getText().toString());
                }
            }
        });
    }

    private void createNote(String note) {
        long id = db.insertNote(note);

        Note getIdNote = db.getNote(id);

        if (getIdNote != null) {
            noteList.add(0, getIdNote);
            notesAdapter.notifyDataSetChanged();
            toggleEmptyNote();
        }
    }
    // Step 11: Balik lagi ke Comment Step 10

    private void updateNote(String note, int position) {
        Note getNoteList = noteList.get(position);

        getNoteList.setNote(note);
        db.updateNote(getNoteList);

        noteList.set(position, getNoteList);
        notesAdapter.notifyItemChanged(position);

        // Step 8: Bikin Method TextView kalo List Kosong si TextView No Note View itu Muncul sebaliknya di hilangkan
        toggleEmptyNote();
    }

    private void toggleEmptyNote() {
        if (db.getNotesCount() > 0) {
            noNotesView.setVisibility(View.GONE);
        } else {
            noNotesView.setVisibility(View.VISIBLE);
        }
    }

    // Step 9: Balik lagi ke Comment Step 7

}
