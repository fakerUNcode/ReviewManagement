// src/main/java/com/example/reviewmanagement/Activity/QuestionDetailActivity.java
package com.example.reviewmanagement.Activity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.reviewmanagement.Helper.DatabaseHelper;
import com.example.reviewmanagement.R;

public class QuestionDetailActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);
        dbHelper = new DatabaseHelper(this);

        TextView questionDetailTextView = findViewById(R.id.textview_question_detail);
        TextView questionAnswerTextView = findViewById(R.id.textview_question_answer);
        TextView questionTypeTextView = findViewById(R.id.textview_question_type);

        // 获取传递的详细信息并显示
        String questionDetail = getIntent().getStringExtra("question_detail");
        String questionAnswer = getIntent().getStringExtra("question_answer");
        String questionType = getIntent().getStringExtra("question_type");
        Log.d("QuestionDetailActivity", "Question type received: " + getIntent().getStringExtra("question_type"));



        questionDetailTextView.setText("问题： " + questionDetail);
        questionAnswerTextView.setText("答案： " + questionAnswer);
        questionTypeTextView.setText(questionType);

    }


    @SuppressLint("Range")
    private String getQuestionTypeName(int questionTypeId) {
        return dbHelper.getQuestionTypeName(questionTypeId);
    }
}
