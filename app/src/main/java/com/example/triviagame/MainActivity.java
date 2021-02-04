package com.example.triviagame;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.nfc.cardemulation.CardEmulation;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.triviagame.data.AnswerListAsyncResponse;
import com.example.triviagame.data.QuestionBank;
import com.example.triviagame.model.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int CLEAR_REQUEST = 1;
    public static String DATA_ID;

    List<Question> questionArrayList;
    List<Question> questionArrayListN;
    Button trueButton, falseButton, clearButton;
    //ImageButton prevButton, nextButton;
    TextView counter, content, score;
    boolean gotWrong = false;
    int currentIndex;
    int currentScore;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DATA_ID = "data";
        currentScore = 0;


        trueButton = findViewById(R.id.true_button); falseButton = findViewById(R.id.false_button); clearButton = findViewById( R.id.clear_button);
        //prevButton = findViewById( R.id.prev_button); nextButton = findViewById( R.id.next_button);
        counter = findViewById( R.id.text_count);
        content = findViewById( R.id.content);
        score = findViewById( R.id.text_score);

        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);
        //prevButton.setOnClickListener(this);
       // nextButton.setOnClickListener(this);



        questionArrayList = new QuestionBank().getQuestions( new AnswerListAsyncResponse() {
            @Override
            public void processFinished( ArrayList<Question> questionArrayList){
                Log.d( "call Back", "Questions: " + questionArrayList);
                content.setText( questionArrayList.get(0).getQuestion());
                questionArrayListN = questionArrayList;

                //once it finish fetching the questions get the sharedPreferences as well!

                SharedPreferences sharedPreferences = getSharedPreferences( DATA_ID, MODE_PRIVATE);
                if( sharedPreferences != null && questionArrayList.size() > 0) {
                    currentScore = sharedPreferences.getInt("score", 0);
                    currentIndex = sharedPreferences.getInt("index", 0);

                    update();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        Log.d( "here", "" + questionArrayList.size());

        switch( v.getId()) {
            case R.id.true_button:
                answered( checkAnswer( true));
                break;

            case R.id.false_button:
                answered( checkAnswer( false));
                break;

            case R.id.clear_button:
                Intent clear = new Intent( this, ClearActivity.class);
                startActivityForResult( clear, CLEAR_REQUEST);
                break;


//            case R.id.next_button:
//                currentIndex = ( currentIndex + 1 ) % questionArrayList.size();
//                update();
//                break;

//            case R.id.prev_button:
//                if( currentIndex > 0)
//                currentIndex = ( currentIndex - 1 ) % questionArrayList.size();
//                else
//                    currentIndex = questionArrayList.size() - 1;
//                update();
//                break;
        }
    }

    public boolean checkAnswer( boolean isTrue){
        if( questionArrayList.get( currentIndex).getAnswer() == isTrue)
            return true;
        else
            return false;
    }

    public void update(){
        counter.setText( currentIndex + " out of " + questionArrayList.size());
        content.setText( questionArrayList.get( currentIndex).getQuestion());
        score.setText( " Score : " + currentScore);

        setSharedPreferences();
    }

    public void answered( boolean correct){
        if ( correct) {
            Toast.makeText(MainActivity.this, " نجم!", Toast.LENGTH_SHORT).show();
            correctAnimation();
            currentIndex++;

            if(!gotWrong)
                 currentScore += 100;
            else {
                currentScore += 50;
                gotWrong = false;
            }

            update();
        }

        else {
            Toast.makeText(MainActivity.this, "غلط يسطا!", Toast.LENGTH_SHORT).show();
            wrongAnimation();
            //important to update for the ViewCard to repaint
            gotWrong = true;
            update();
        }
    }

    public void wrongAnimation(){
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);
        final CardView cardView = findViewById( R.id.card);
        cardView.setAnimation( shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void correctAnimation(){
        Animation dance = AnimationUtils.loadAnimation( MainActivity.this, R.anim.dancing_animation);
        final CardView cardView = findViewById( R.id.card);
        cardView.setAnimation( dance);

        dance.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    public void setSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences( DATA_ID, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt( "index", currentIndex);
        editor.putInt( "score", currentScore);
        editor.apply();
    }

    public void clearAndStartOver(){
        currentIndex = 0; currentScore = 0;
        setSharedPreferences();
        update();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( requestCode == 1)
            if( resultCode == -1)
                clearAndStartOver();
    }

    @Override
    protected void onPause() {
        super.onPause();

        setSharedPreferences();
    }
}