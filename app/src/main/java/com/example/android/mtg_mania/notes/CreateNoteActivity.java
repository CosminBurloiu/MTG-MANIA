package com.example.android.mtg_mania.notes;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.mtg_mania.R;
import com.example.android.mtg_mania.utils.MtgmContract;
import com.example.android.mtg_mania.utils.MtgmDbHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreateNoteActivity extends AppCompatActivity {

    /** EditText field to enter the note's title */
    private EditText titleEditText;

    /** EditText field to enter the note's text */
    private EditText textEditText;

    // Create and/or open a database to read from it
    SQLiteDatabase dbR = MtgmDbHelper.getInstance().getReadableDatabase();

    // Create and/or open a database to write in it
    SQLiteDatabase dbW = MtgmDbHelper.getInstance().getWritableDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_note);

        //Define the variables that will represent the views presented in the Create Note activity
        //From these views we will get the user input
         titleEditText = (EditText) findViewById(R.id.title);
         textEditText = (EditText) findViewById(R.id.text);
    }

    public int saveNote(){

        // Read from input fields
        String titleString = titleEditText.getText().toString();
        String textString = textEditText.getText().toString();

        // Prepare date and time variables.
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("dd / MM / yyyy");
        SimpleDateFormat time = new SimpleDateFormat("hh:mm");

        String dateString =  date.format(calendar.getTime());
        String timeString = time.format(calendar.getTime());

         //Sanitize user input
         if (TextUtils.isEmpty(titleString) && TextUtils.isEmpty(textString) ){
             Toast.makeText(this, "Note was empty, so it wasn't saved.", Toast.LENGTH_SHORT).show();
             finish();
             return 0;
         }

         if (TextUtils.isEmpty(titleString)){
            titleString="...";
         }

        if (TextUtils.isEmpty(textString)){
            textString="";
        }


        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(MtgmContract.NoteEntry.COLUMN_TITLE, titleString);
        values.put(MtgmContract.NoteEntry.COLUMN_TEXT, textString);
        values.put(MtgmContract.NoteEntry.COLUMN_DATE, dateString);
        values.put(MtgmContract.NoteEntry.COLUMN_TIME, timeString);

        // Insert a new row for the note in the database, returning the ID of that new row.
        long newRowId = dbW.insert(MtgmContract.NoteEntry.TABLE_NAME, null, values);

        if(newRowId == -1){
            Toast.makeText(this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
        }

        //Notify that changes were made
        NotesActivity.modificationsWereMade = 1;

        finish();
        return 1;
    }

    @Override
    protected void onPause() {
        saveNote();

        super.onPause();
    }

}
