package com.example.android.mtg_mania.notes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.mtg_mania.R;
import com.example.android.mtg_mania.utils.MtgmContract;
import com.example.android.mtg_mania.utils.MtgmDbHelper;

import java.util.ArrayList;

import static com.example.android.mtg_mania.utils.MtgmContract.NoteEntry.TABLE_NAME;


public class NotesActivity extends AppCompatActivity {

    private RecyclerView items;
    private RecyclerView.Adapter adapter;

    //Flag specific to changes
    public static Integer modificationsWereMade = 0;

    // Create and/or open a database to read from it
    SQLiteDatabase dbR = MtgmDbHelper.getInstance().getReadableDatabase();

    // Create and/or open a database to write in it
    SQLiteDatabase dbW = MtgmDbHelper.getInstance().getWritableDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        // Toast that will notify the user with the total number of existing notes
        Context mContext = getApplicationContext();
        if( displayNotesNumber() == 1){
            Toast.makeText(mContext, "You have 1 written note.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "You have " + displayNotesNumber() + " written notes.", Toast.LENGTH_SHORT).show();
        }

        //Get the notes from our database
        getItems();

        //Find the "Create a Note" Button view
        Button createNote = (Button) findViewById(R.id.create);

        //Set a clickListener on that View
        createNote.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent createNoteIntent = new Intent(NotesActivity.this, CreateNoteActivity.class );
                startActivity(createNoteIntent);
            }
        });
    }

    public void getItems(){
        // Initialize the list, fetching the notes from the database
        ArrayList<NotesListItem> items = initNotesList();

        // Use items to represent every note,RecyclerView and an adapter, creating a dynamic layout that will contain every note in the database
        this.items = (RecyclerView) findViewById(R.id.items);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        this.items.setLayoutManager(mLayoutManager);
        adapter = new NotesListItemAdapter(items);
        this.items.setAdapter(adapter);

        //We have received the latest version
        this.modificationsWereMade = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(this.modificationsWereMade == 1) {
            getItems();
        }
    }

    private int displayNotesNumber() {

        // Perform this raw SQL query "SELECT * FROM notes"
        // to get a Cursor that contains all rows from the notes table.
        Cursor cursor = dbR.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        Integer notesCount = cursor.getCount();

        // Always close the cursor when you're done reading from it. This releases all its
        // resources and makes it invalid.
        cursor.close();

        return notesCount;
    }

    public void deleteNote(View view) {
        // Get the tag
        Integer noteTag = (Integer) view.getTag();

        //Delete the note from the database.
        dbW.delete(TABLE_NAME,"_id=?",new String[]{noteTag.toString()});

        //Send the items to the adapter again, so that we will refresh the notes list.
        getItems();

        //Proper notification.
        Toast.makeText(this, "Note was successfully deleted.", Toast.LENGTH_SHORT).show();
    }

    public void editNote(View view){
        // Get the tag
        Integer noteTag = (Integer) view.getTag();

        //Start the Edit Activity and send the tag value to it
        Intent intent = new Intent(NotesActivity.this, EditNoteActivity.class);
        intent.putExtra("noteID", noteTag);
        startActivity(intent);
    }

    private ArrayList<NotesListItem> initNotesList(){
        ArrayList<NotesListItem> list = new ArrayList<>();

        // Perform this raw SQL query "SELECT * FROM notes"
        // to get a Cursor that contains all rows from the notes table.
        Cursor cursor = dbR.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        //Prepare DB index for each column
        int idColumnIndex = cursor.getColumnIndex(MtgmContract.NoteEntry._ID);
        int titleColumnIndex = cursor.getColumnIndex(MtgmContract.NoteEntry.COLUMN_TITLE);
        int textColumnIndex = cursor.getColumnIndex(MtgmContract.NoteEntry.COLUMN_TEXT);
        int dateColumnIndex = cursor.getColumnIndex(MtgmContract.NoteEntry.COLUMN_DATE);
        int timeColumnIndex = cursor.getColumnIndex(MtgmContract.NoteEntry.COLUMN_TIME);

        //Fetch notes from DB and display
        while (cursor.moveToNext()){

            Integer currentID = cursor.getInt(idColumnIndex);
            String currentTitle = cursor.getString(titleColumnIndex);
            String currentText = cursor.getString(textColumnIndex);
            String currentDate = cursor.getString(dateColumnIndex);
            String currentTime = cursor.getString(timeColumnIndex);

            list.add(new NotesListItem(currentID, currentTitle, currentText, currentDate, currentTime));
        }

        // Close the cursor when you're done reading from it.
        // This releases all its resources and makes it invalid.
        cursor.close();

        return list;
    }
}
