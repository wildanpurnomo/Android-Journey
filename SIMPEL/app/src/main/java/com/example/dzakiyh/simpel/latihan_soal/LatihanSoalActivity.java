package com.example.dzakiyh.simpel.latihan_soal;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dzakiyh.simpel.R;
import com.example.dzakiyh.simpel.login_and_register.MainActivity;

import java.util.Random;

public class LatihanSoalActivity extends AppCompatActivity {

    Button answer1, answer2, answer3, answer4;
    TextView score, question;
    Random r;

    private Questions mQuestions = new Questions();

    private String mAnswer;
    private int mScore = 0;
    private int mQuestionsLength = mQuestions.mQuestions.length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latihan_soal);

        r = new Random();

        answer1 = (Button) findViewById(R.id.answer1);
        answer2 = (Button) findViewById(R.id.answer2);
        answer3 = (Button) findViewById(R.id.answer3);
        answer4 = (Button) findViewById(R.id.answer4);

        question = (TextView) findViewById(R.id.question);
        score = (TextView) findViewById(R.id.score);

        updateQuestion(r.nextInt(mQuestionsLength));

        score.setText("score:" + mScore);

        answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(answer1.getText() == mAnswer) {
                    mScore++;
                    score.setText("score:" + mScore);
                    updateQuestion(r.nextInt(mQuestionsLength));
                }else {
                    gameOver();
                }

            }
        });

        answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(answer2.getText() == mAnswer) {
                    mScore++;
                    score.setText("score:" + mScore);
                    updateQuestion(r.nextInt(mQuestionsLength));
                }else {
                    gameOver();
                }

            }
        });

        answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(answer3.getText() == mAnswer) {
                    mScore++;
                    score.setText("score:" + mScore);
                    updateQuestion(r.nextInt(mQuestionsLength));
                }else {
                    gameOver();
                }

            }
        });

        answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(answer4.getText() == mAnswer) {
                    mScore++;
                    score.setText("score:" + mScore);
                    updateQuestion(r.nextInt(mQuestionsLength));
                }else {
                    gameOver();
                }

            }
        });
    }

    private void updateQuestion(int num) {
        Log.d("updateQuestion ", Integer.toString(num));
        question.setText(mQuestions.getQuestion(num));
        answer1.setText(mQuestions.getChoice1(num));
        answer2.setText(mQuestions.getChoice2(num));
        answer3.setText(mQuestions.getChoice3(num));
        answer4.setText(mQuestions.getChoice4(num));

        mAnswer = mQuestions.getCorrectAnswer(num);
    }

    private void gameOver() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LatihanSoalActivity.this);
        alertDialogBuilder
                .setMessage("Game Over! your score is" + mScore + "points")
                .setCancelable(false)
                .setPositiveButton("NEW GAME",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                startActivity(new Intent(getApplicationContext(), LatihanSoalActivity.class));
                            }
                        })
                .setNegativeButton("EXIT",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                finish();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}
