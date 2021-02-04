package com.example.triviagame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ClearActivity extends AppCompatActivity implements View.OnClickListener {
    Button accept, decline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear);

        Button accept, decline;
        accept = findViewById( R.id.yes_button); decline = findViewById( R.id.no_button);

        accept.setOnClickListener(this);
        decline.setOnClickListener(this);

         //CASE_2 if you want to pass intent with data u can use this code as well
//        Intent intent = new Intent();
//        intent.putExtra("bla", "bla");
//        setResult( RESULT_OK, intent);



    }

    @Override
    public void onClick(View v) {
        switch( v.getId()){
            case R.id.yes_button:
                setResult( RESULT_OK);
                finish();
                break;

            case R.id.no_button:
                setResult( RESULT_CANCELED);
                finish();
                break;
        }
    }
}