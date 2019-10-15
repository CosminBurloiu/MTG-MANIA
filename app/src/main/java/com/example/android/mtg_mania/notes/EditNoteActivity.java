package com.example.android.mtg_mania.notes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.mtg_mania.R;
import com.example.android.mtg_mania.utils.MtgmContract;
import com.example.android.mtg_mania.utils.MtgmDbHelper;

import static com.example.android.mtg_mania.utils.MtgmContract.NoteEntry.TABLE_NAME;
import static com.example.android.mtg_mania.utils.MtgmContract.NoteEntry._ID;

public class EditNoteActivity extends AppCompatActivity {

    //Define the variables that will represent the views presented in the Create Note activity
    //From these views we will get the user input
    EditText titleEditText;
    EditText textEditText;

    private Integer noteID = 0;

    // Create and/or open a database to read from it
    SQLiteDatabase dbR = MtgmDbHelper.getInstance().getReadableDatabase();

    // Create and/or open a database to write in it
    SQLiteDatabase dbW = MtgmDbHelper.getInstance().getWritableDatabase();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_note);

        this.titleEditText = (EditText) findViewById(R.id.title);
        this.textEditText = (EditText) findViewById(R.id.text);

        Intent intent = getIntent();
        noteID = intent.getIntExtra("noteID", 0);

        //Create a cursor with the strings from the requested note
        Cursor cursor = dbR.rawQuery("SELECT * FROM " + MtgmContract.NoteEntry.TABLE_NAME + " WHERE " + _ID + "=" + noteID, null);

        if(cursor.moveToFirst()) {
            //Assign each value to its specific view.
            String noteTitle = cursor.getString(cursor.getColumnIndex(MtgmContract.NoteEntry.COLUMN_TITLE));
            String noteText = cursor.getString(cursor.getColumnIndex(MtgmContract.NoteEntry.COLUMN_TEXT));

            this.titleEditText.setText(noteTitle, TextView.BufferType.EDITABLE);

            this.textEditText.setText(noteText, TextView.BufferType.EDITABLE);
        }
        //Finish it.
        cursor.close();
    }

    @Override
    protected void onPause() {
        update(noteID);

        super.onPause();
    }

    public void update(Integer noteID){

        // Get new data from the input fields
        String newNoteTitle = this.titleEditText.getText().toString();
        String newNoteText = this.textEditText.getText().toString();

        // Sanitize user input
        if (TextUtils.isEmpty(newNoteTitle) && TextUtils.isEmpty(newNoteTitle) ){
            //Delete the note from the database.
            dbW.delete(TABLE_NAME,"_id=?",new String[]{noteID.toString()});

            finish();
        }

        if (TextUtils.isEmpty(newNoteTitle)){
            newNoteTitle="...";
        }

        if (TextUtils.isEmpty(newNoteText)){
            newNoteText="";
        }

        // Create a ContentValues object where we will add the modified strings
        ContentValues values = new ContentValues();
        values.put(MtgmContract.NoteEntry.COLUMN_TITLE, newNoteTitle);
        values.put(MtgmContract.NoteEntry.COLUMN_TEXT, newNoteText);

        // Update the table row
        dbW.update(TABLE_NAME, values, "_id=" + noteID, null);

        //Notify that changes were made
        NotesActivity.modificationsWereMade = 1;
        finish();
    }


}
