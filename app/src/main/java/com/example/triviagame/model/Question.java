package com.example.triviagame.model;

public class Question {
     String question;
     boolean isTrue;

    public Question(String question, boolean isTrue){
        this.question = question;
        this.isTrue = isTrue;
    }

    public String getQuestion() {
        return question;
    }

    public boolean getAnswer() {
        return isTrue;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(boolean isTrue) {
        this.isTrue = isTrue;
    }
}
