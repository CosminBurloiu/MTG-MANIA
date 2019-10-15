package com.example.android.mtg_mania.counter;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.mtg_mania.R;

public class CounterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);
    }

    public void resetCounter(View view) {

        TextView counterPlayerOne = (TextView) findViewById(
                R.id.playerOne);

        TextView counterPlayerTwo = (TextView) findViewById(
                R.id.playerTwo);

        counterPlayerOne.setText("20");
        counterPlayerTwo.setText("20");
    }

    public void changeLifeTotal(View view) {

        Integer newLifeTotal;

        //Get the button ID and button tag
        String player = view.getTag().toString();

        Button counter = (Button) view;
        String buttonText = counter.getText().toString();
        String buttonValue = buttonText.split(" ")[1];

        //Use the string containing the View ID and get its value

        TextView counterTotalTextView = (TextView)findViewById(getResources().getIdentifier(player, "id", getPackageName()));

        String lifeTotal = counterTotalTextView.getText().toString();

        //Modify it.
        if(buttonText.contains("+")) {
             newLifeTotal = Integer.parseInt(lifeTotal) + Integer.parseInt(buttonValue);
        }
        else{
             newLifeTotal = Integer.parseInt(lifeTotal) - Integer.parseInt(buttonValue);
        }
        counterTotalTextView.setText( "" + newLifeTotal );
    }
}

