package com.bignerdranch.android.androidquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class QuizActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);
        ButtonSetup();
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            Log.d(TAG, "mCurrentIndex " + mCurrentIndex);
        }
        QuestionSetup();
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void QuestionSetup(){
        Log.d(TAG, "QuestionSetup called");
        String[] placeholder = getResources().getStringArray(R.array.question_array);
        mQuestionBank = new TrueFalse[placeholder.length/2];
        for(int i = 0; i != placeholder.length; i=i+2){
            mQuestionBank[i/2] = new TrueFalse(placeholder[i],
                    Boolean.valueOf(placeholder[i+1]));
        }
        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);
        updateQuestion();
    }
    private void ButtonSetup() {
        Log.d(TAG, "ButtonSetup called");
        Button mTrueButton = (Button)findViewById(R.id.true_button);
        addCheckAnswerListener(mTrueButton, true);
        Button mFalseButton = (Button)findViewById(R.id.false_button);
        addCheckAnswerListener(mFalseButton, false);

        ImageButton mNextButton = (ImageButton)findViewById(R.id.next_button);
        addNextQuestionListener(mNextButton);
        TextView mTextView = (TextView)findViewById(R.id.question_text_view);
        addNextQuestionListener(mTextView);

        ImageView mBackButton = (ImageView)findViewById(R.id.previous_button);
        addPreviousQuestionListener(mBackButton);

        Button mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QuizActivity.this, CheatActivity.class);
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();
                i.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE, answerIsTrue);
                startActivityForResult(i, 0);
            }
        });
    }
    private void addCheckAnswerListener(View view, final boolean usersAnswer) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(usersAnswer);
            }
        });
    }

    private void addNextQuestionListener(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });
    }

    private void addPreviousQuestionListener(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex > 0) ?
                        (mCurrentIndex - 1) : mQuestionBank.length-1;
                updateQuestion();
            }
        });
    }

    private void nextQuestion() {
        mCurrentIndex = (mQuestionBank.length-1 > mCurrentIndex) ?
                (mCurrentIndex + 1) : 0;
        mIsCheater = false;
        updateQuestion();
    }

    private void updateQuestion() {
        Log.d(TAG, "updateQuestion called");
        Log.d(TAG, "mCurrentIndex = " + mCurrentIndex);
        String question = mQuestionBank[mCurrentIndex].getQuestion();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue){
        boolean correctAnswer = mQuestionBank[mCurrentIndex].isTrueQuestion();
        Toast.makeText(this, messageId(userPressedTrue, correctAnswer),
                Toast.LENGTH_SHORT).show();
    }

    private int messageId(boolean userAnswer, boolean correctAnswer) {
        int messageResId;
        if (mIsCheater) {
            messageResId = R.string.judgment_toast;
        } else {
            if (userAnswer == correctAnswer) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }
        return messageResId;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        mIsCheater = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false);
    }

    private TextView mQuestionTextView;
    private TrueFalse[] mQuestionBank;
    private int mCurrentIndex = 0;
    private boolean mIsCheater;
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
}
