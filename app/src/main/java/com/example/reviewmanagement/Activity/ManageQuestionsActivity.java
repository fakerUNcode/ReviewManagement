package com.example.reviewmanagement.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.reviewmanagement.Adapter.QuestionListAdapter;
import com.example.reviewmanagement.Helper.DatabaseHelper;
import com.example.reviewmanagement.Item.QuestionItem;
import com.example.reviewmanagement.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;



public class ManageQuestionsActivity extends AppCompatActivity {
    public static final int QUESTION_TYPE_ALL = 0;
    private static final int QUESTION_TYPE_SINGLE_CHOICE = 1;
    private static final int QUESTION_TYPE_MULTIPLE_CHOICE = 2;
    private static final int QUESTION_TYPE_JUDGE = 3;
    private static final int QUESTION_TYPE_APPLICATION = 4;
    private ListView questionsListView;
    private QuestionListAdapter questionsAdapter;
    private DatabaseHelper dbHelper;
    private List<QuestionItem> questionsList = new ArrayList<>();
    private long courseId; // Store the course ID



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_questions);

        // Get the course ID from the intent
        Intent intent = getIntent();
        courseId = intent.getLongExtra("COURSE_ID", -1);

        if (courseId == -1) {
            Toast.makeText(this, "无效的课程ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        // Find the RadioGroup and RadioButton
        RadioGroup radioGroup = findViewById(R.id.radio_question_type);
        RadioButton radioAll = findViewById(R.id.radio_all);
        // Set the default selection to "全部"
        radioGroup.check(radioAll.getId());

        Button searchButton = findViewById(R.id.button_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchQuestions();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedTypeId;
                if (checkedId == R.id.radio_all) {
                    selectedTypeId = QUESTION_TYPE_ALL;
                } else if (checkedId == R.id.radio_single_choice) {
                    selectedTypeId = QUESTION_TYPE_SINGLE_CHOICE;
                } else if (checkedId == R.id.radio_multiple_choice) {
                    selectedTypeId = QUESTION_TYPE_MULTIPLE_CHOICE;
                } else if (checkedId == R.id.radio_judge) {
                    selectedTypeId = QUESTION_TYPE_JUDGE;
                } else if (checkedId == R.id.radio_application) {
                    selectedTypeId = QUESTION_TYPE_APPLICATION;
                } else {
                    selectedTypeId = -1; // 需要处理无法识别的 id
                }
                refreshQuestions(selectedTypeId);
            }
        });

        dbHelper = new DatabaseHelper(this);
        questionsListView = findViewById(R.id.list_item_question);
        ImageButton addButton = findViewById(R.id.button_add_question);

        questionsAdapter = new QuestionListAdapter(this, questionsList, dbHelper);
        loadQuestions();
        questionsListView.setAdapter(questionsAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddQuestionDialog();
            }
        });
    }

    private void showTipToast() {
        Toast toast = new Toast(this);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.question_toast, null);
        toast.setView(v);
        toast.show();
    }
    private void showdeleteTipToast() {
        Toast toast = new Toast(this);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.question_delete_toast, null);
        toast.setView(v);
        toast.show();
    }

    private void refreshQuestions(int selectedTypeId) {
        Cursor cursor;
        if (selectedTypeId == QUESTION_TYPE_ALL) {  // 这里应该比较 selectedTypeId 和 QUESTION_TYPE_ALL
            // 显示所有类型的问题
            cursor = dbHelper.getQuestionsByCourseId(courseId);
        } else {
            // 只显示用户选择的类型的问题
            cursor = dbHelper.getQuestionsByCourseIdAndType(courseId, selectedTypeId);
        }
        questionsList.clear();
        if (cursor != null) {
            int idColumnIndex = cursor.getColumnIndex("id");
            int courseIdColumnIndex = cursor.getColumnIndex("course_id");
            int questionTypeIdColumnIndex = cursor.getColumnIndex("question_type_id");
            int questionTextColumnIndex = cursor.getColumnIndex("question_text");

            while (cursor.moveToNext()) {
                int id = cursor.getInt(idColumnIndex);
                int courseId = cursor.getInt(courseIdColumnIndex);
                int questionTypeId = cursor.getInt(questionTypeIdColumnIndex);
                String questionText = cursor.getString(questionTextColumnIndex);
                @SuppressLint("Range") String answer = cursor.getString(cursor.getColumnIndex("answer")); // 假设答案列的名称为"answer"
                questionsList.add(new QuestionItem(id, courseId, questionTypeId, questionText, answer)); // 传递答案字段
            }

            cursor.close();
        }

        questionsAdapter.notifyDataSetChanged();
    }

    private void searchQuestions() {
        // 创建一个 Intent 跳转到 SearchQuestionsActivity
        Intent intent = new Intent(this, SearchQuestionsActivity.class);

        // 获取所有问题信息
        ArrayList<QuestionItem> allQuestions = new ArrayList<>(questionsList);

        // 使用 Parcelable 传递数据
        intent.putParcelableArrayListExtra("ALL_QUESTIONS", allQuestions);

        intent.putExtra("COURSE_ID", courseId);

        // 启动 SearchQuestionsActivity
        startActivity(intent);
    }
    private int getQuestionTypeIdFromRadioButtonId(int radioButtonId) {
        //  RadioButton 的 id 映射到问题类型的 id
        if (radioButtonId == R.id.radio_all) {
            return QUESTION_TYPE_ALL;
        } else if (radioButtonId == R.id.radio_single_choice) {
            return QUESTION_TYPE_SINGLE_CHOICE;
        } else if (radioButtonId == R.id.radio_multiple_choice) {
            return QUESTION_TYPE_MULTIPLE_CHOICE;
        } else if (radioButtonId == R.id.radio_judge) {
            return QUESTION_TYPE_JUDGE;
        } else if (radioButtonId == R.id.radio_application) {
            return QUESTION_TYPE_APPLICATION;
        } else {
            return  -1; // 需要处理无法识别的 id
        }
    }

    public void loadQuestions() {
        Cursor cursor = dbHelper.getQuestionsByCourseId(courseId);
        questionsList.clear();
        if (cursor != null) {
            int idColumnIndex = cursor.getColumnIndex("id");
            int courseIdColumnIndex = cursor.getColumnIndex("course_id");
            int questionTypeIdColumnIndex = cursor.getColumnIndex("question_type_id");
            int questionTextColumnIndex = cursor.getColumnIndex("question_text");

            while (cursor.moveToNext()) {
                int id = cursor.getInt(idColumnIndex);
                int courseId = cursor.getInt(courseIdColumnIndex);
                int questionTypeId = cursor.getInt(questionTypeIdColumnIndex);
                String questionText = cursor.getString(questionTextColumnIndex);
                @SuppressLint("Range") String answer = cursor.getString(cursor.getColumnIndex("answer")); // 假设答案列的名称为"answer"
                questionsList.add(new QuestionItem(id, courseId, questionTypeId, questionText, answer)); // 传递答案字段
            }

            cursor.close();
        }
        questionsAdapter.notifyDataSetChanged();
    }

    public void showAddQuestionDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("添加问题");

        // Inflate the custom layout for the dialog
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_add_question, null);
        final EditText inputDescription = viewInflated.findViewById(R.id.edittext_question_description);
        final EditText inputAnswer = viewInflated.findViewById(R.id.edittext_question_answer);
        final Spinner spinnerQuestionType = viewInflated.findViewById(R.id.spinner_question_type);

        // Populate spinner with question types
        Cursor cursor = dbHelper.getAllQuestionTypes();
        if (cursor != null && cursor.getCount() > 0) {
            String[] questionTypes = cursorToTypeStringArray(cursor);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, questionTypes);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerQuestionType.setAdapter(adapter);
        } else {
            Toast.makeText(this, "没找到题目类型数据", Toast.LENGTH_LONG).show();
        }

        // Set listener for spinner item selection
        spinnerQuestionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = (String) parent.getItemAtPosition(position);
                Toast.makeText(ManageQuestionsActivity.this, "你选择了: " + selectedType, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Set the custom view to the dialog
        builder.setView(viewInflated);

        // Set positive button
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String description = inputDescription.getText().toString().trim();
                String answer = inputAnswer.getText().toString().trim();
                int questionTypeId = spinnerQuestionType.getSelectedItemPosition() + 1; // Adjust based on actual IDs

                if (!description.isEmpty() && !answer.isEmpty()) {
                    dbHelper.addQuestion(courseId, questionTypeId, description, answer); // Adjust courseId as needed
                    showTipToast();
                    RadioGroup radioGroup = findViewById(R.id.radio_question_type);
                    // 获取当前选中的单选按钮ID
                    int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                    int selectedTypeId = getQuestionTypeIdFromRadioButtonId(checkedRadioButtonId);

                    // 根据当前选中的单选按钮ID重新加载问题列表
                    refreshQuestions(selectedTypeId);
                } else {
                    Toast.makeText(ManageQuestionsActivity.this, "请填写题目描述和答案", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set negative button
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Show the dialog
        builder.show();
    }


    public void showEditQuestionDialog(QuestionItem questionItem) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("编辑问题");

        // Inflate the custom layout for the dialog
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_add_question, null);
        final EditText inputDescription = viewInflated.findViewById(R.id.edittext_question_description);
        final EditText inputAnswer = viewInflated.findViewById(R.id.edittext_question_answer);
        final Spinner spinnerQuestionType = viewInflated.findViewById(R.id.spinner_question_type);

        // Pre-fill the fields with the existing question data
        inputDescription.setText(questionItem.question);
        inputAnswer.setText(questionItem.answer);

        // Populate the spinner with question types and set the selected type
        Cursor cursor = dbHelper.getAllQuestionTypes();
        if (cursor != null && cursor.getCount() > 0) {
            String[] questionTypes = cursorToTypeStringArray(cursor);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, questionTypes);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerQuestionType.setAdapter(adapter);
            spinnerQuestionType.setSelection(questionItem.questionTypeId - 1); // Assuming IDs start from 1
        } else {
            Toast.makeText(this, "没找到题目类型数据", Toast.LENGTH_LONG).show();
        }

        // Set the custom view to the dialog
        builder.setView(viewInflated);

        // Set positive button
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newDescription = inputDescription.getText().toString().trim();
                String newAnswer = inputAnswer.getText().toString().trim();
                int newQuestionTypeId = spinnerQuestionType.getSelectedItemPosition() + 1; // Assuming IDs start from 1

                if (!newDescription.isEmpty() && !newAnswer.isEmpty()) {
                    dbHelper.updateQuestion(questionItem.id, questionItem.courseId, newQuestionTypeId, newDescription, newAnswer);
                    showTipToast();

                    // Get the currently selected radio button ID
                    RadioGroup radioGroup = findViewById(R.id.radio_question_type);
                    int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                    int selectedTypeId = getQuestionTypeIdFromRadioButtonId(checkedRadioButtonId);

                    // Refresh the question list based on the selected question type
                    refreshQuestions(selectedTypeId);
                } else {
                    Toast.makeText(ManageQuestionsActivity.this, "请填写题目描述和答案", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set negative button
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Show the dialog
        builder.show();
    }



    public void deleteQuestion(int id) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("确认删除");
        builder.setMessage("您确定要删除这个问题吗？");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbHelper.deleteQuestion(id);
                showdeleteTipToast();
                loadQuestions();
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
