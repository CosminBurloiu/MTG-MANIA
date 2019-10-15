package com.example.android.mtg_mania;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.mtg_mania.counter.CounterActivity;
import com.example.android.mtg_mania.notes.NotesActivity;
import com.example.android.mtg_mania.posts.PostsActivity;
import com.example.android.mtg_mania.videos.VideosActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find the View that shows the Counter activity
        Button counter = (Button) findViewById(R.id.counter);


        //Set a clickListener on that View
        counter.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent counterIntent = new Intent(MainActivity.this, CounterActivity.class );
                startActivity(counterIntent);

                Toast.makeText(view.getContext(), "Let the war begin.", Toast.LENGTH_SHORT).show();
            }
        });

        //Find the View that shows the Notes activity
        Button notes = (Button) findViewById(R.id.notes);

        //Set a clickListener on that View
        notes.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent notesIntent = new Intent(MainActivity.this, NotesActivity.class );
                startActivity(notesIntent);
            }
        });

        //Find the View that shows the Videos activity
        Button videos = (Button) findViewById(R.id.videos);

        //Set a clickListener on that View
        videos.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent videosIntent = new Intent(MainActivity.this, VideosActivity.class );
                startActivity(videosIntent);
            }
        });

        //Find the View that shows the Videos activity
        Button news = (Button) findViewById(R.id.community);

        //Set a clickListener on that View
        news.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent newsIntent = new Intent(MainActivity.this, PostsActivity.class );
                startActivity(newsIntent);
            }
        });
    }

}
