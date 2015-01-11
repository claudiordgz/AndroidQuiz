package com.bignerdranch.android.androidquiz;


public class TrueFalse {

    public TrueFalse(String question, boolean trueQuestion) {
        setQuestion(question);
        setTrueQuestion(trueQuestion);

    }

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String Question) {
        this.mQuestion = Question;
    }

    public boolean isTrueQuestion() {
        return mTrueQuestion;
    }

    public void setTrueQuestion(boolean TrueQuestion) {
        this.mTrueQuestion = TrueQuestion;
    }

    private String mQuestion;
    private boolean mTrueQuestion;
}
