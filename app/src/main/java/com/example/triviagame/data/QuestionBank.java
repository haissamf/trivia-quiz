package com.example.triviagame.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.triviagame.contoller.AppController;
import com.example.triviagame.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;

public class QuestionBank {

    ArrayList<Question> arabicQuestionsArrayList = new ArrayList<>();


    ArrayList<Question> questionsArrayList = new ArrayList<>();

    private String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public List<Question> getQuestions( final AnswerListAsyncResponse callBack) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, (JSONArray) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        questionsArrayList.add(new Question((String) response.getJSONArray(i).get(0), (boolean) response.getJSONArray(i).get(1)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Log.d( "here2", "123" );
                callBack.processFinished( questionsArrayList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return questionsArrayList;
    }
}

//    public List<Question> getQuestions(){
//        arabicQuestionsArrayList.add( new Question("", true);
//        return arabicQuestionsArrayList;
//    }


