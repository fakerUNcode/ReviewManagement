package com.example.reviewmanagement.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.reviewmanagement.Helper.DatabaseHelper;
import com.example.reviewmanagement.Item.QuestionItem;
import com.example.reviewmanagement.R;
import com.example.reviewmanagement.Adapter.QuestionListAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class SearchQuestionsActivity extends AppCompatActivity {

    private EditText searchEditText;
    private ListView searchResultsListView;
    private QuestionListAdapter searchResultsAdapter;
    private DatabaseHelper dbHelper;
    private ArrayList<QuestionItem> searchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_questions);
        dbHelper = new DatabaseHelper(this);
        searchEditText = findViewById(R.id.edittext_search);
        searchResultsListView = findViewById(R.id.list_search_results);

        searchResults = new ArrayList<>();
        searchResultsAdapter = new QuestionListAdapter(this, searchResults, dbHelper);
        searchResultsListView.setAdapter(searchResultsAdapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchQuestions(s.toString());
                searchResultsAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        searchResultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QuestionItem selectedQuestion = searchResultsAdapter.getItem(position);
                Intent intent = new Intent(SearchQuestionsActivity.this, QuestionDetailActivity.class);
                intent.putExtra("question_detail", selectedQuestion.question);
                intent.putExtra("question_answer", selectedQuestion.answer);
                intent.putExtra("question_type_id", selectedQuestion.questionTypeId);
                startActivity(intent);
            }
        });
    }

    private void searchQuestions(String query) {
        searchResults.clear();
        if (!query.isEmpty()) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT id, course_id, question_type_id, question_text, answer FROM Questions WHERE question_text LIKE ? OR answer LIKE ?", new String[]{"%" + query + "%", "%" + query + "%"});
            while (cursor.moveToNext()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") int courseId = cursor.getInt(cursor.getColumnIndex("course_id")); // 确保列名与数据库中的定义一致
                @SuppressLint("Range") int questionTypeId = cursor.getInt(cursor.getColumnIndex("question_type_id"));
                @SuppressLint("Range") String question = cursor.getString(cursor.getColumnIndex("question_text"));
                @SuppressLint("Range") String answer = cursor.getString(cursor.getColumnIndex("answer"));
                searchResults.add(new QuestionItem(id, courseId, questionTypeId, question, answer));
            }
            cursor.close();
        }
        searchResultsAdapter.notifyDataSetChanged();
    }


    public void showEditQuestionDialog(final QuestionItem questionItem) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("编辑问题");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_add_question, null);
        final EditText inputDescription = viewInflated.findViewById(R.id.edittext_question_description);
        final EditText inputAnswer = viewInflated.findViewById(R.id.edittext_question_answer);
        final Spinner spinnerQuestionType = viewInflated.findViewById(R.id.spinner_question_type);

        inputDescription.setText(questionItem.question);
        inputAnswer.setText(questionItem.answer);

        // Populate spinner with question types
        Cursor cursor = dbHelper.getAllQuestionTypes();
        String[] questionTypes = cursorToTypeStringArray(cursor);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, questionTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQuestionType.setAdapter(adapter);
        spinnerQuestionType.setSelection(questionItem.questionTypeId - 1); // Assuming IDs start from 1

        builder.setView(viewInflated);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newDescription = inputDescription.getText().toString().trim();
                String newAnswer = inputAnswer.getText().toString().trim();
                int newQuestionTypeId = spinnerQuestionType.getSelectedItemPosition() + 1; // Adjust based on actual IDs
                if (!newDescription.isEmpty() && !newAnswer.isEmpty()) {
                    dbHelper.updateQuestion(questionItem.id, questionItem.courseId, newQuestionTypeId, newDescription, newAnswer);
                    searchQuestions(searchEditText.getText().toString()); // Refresh search results
                    Toast.makeText(SearchQuestionsActivity.this, "问题已更新", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SearchQuestionsActivity.this, "请填写题目描述和答案", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void showDeleteQuestionDialog(final int questionId) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("确认删除");
        builder.setMessage("您确定要删除这个问题吗？");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbHelper.deleteQuestion(questionId);
                searchQuestions(searchEditText.getText().toString()); // Refresh search results
                Toast.makeText(SearchQuestionsActivity.this, "问题已删除", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private String[] cursorToTypeStringArray(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            return new String[0];
        }

        List<String> typeList = new ArrayList<>();
        int typeNameIndex = cursor.getColumnIndex("type_name");

        if (typeNameIndex != -1) {
            while (cursor.moveToNext()) {
                String typeName = cursor.getString(typeNameIndex);
                if (typeName != null && !typeName.isEmpty()) {
                    typeList.add(typeName);
                }
            }
        } else {
            Log.e("DatabaseHelper", "The 'type_name' column doesn't exist.");
        }
        cursor.close();
        return typeList.toArray(new String[typeList.size()]);
    }

}