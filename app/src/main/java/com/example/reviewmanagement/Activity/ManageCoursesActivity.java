package com.example.reviewmanagement.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reviewmanagement.Helper.DatabaseHelper;
import com.example.reviewmanagement.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;


public class ManageCoursesActivity extends AppCompatActivity {

    private Button addCourseButton;
    private DatabaseHelper dbHelper;
    private ListView coursesListView;
    private ArrayAdapter<String> coursesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_courses);

        dbHelper = new DatabaseHelper(this);
        coursesListView = findViewById(R.id.list_courses);
        addCourseButton = findViewById(R.id.button_add_course);

        loadCourses();

        coursesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long courseId = (long) view.getTag();
                editCourseName(courseId);
            }
        });

        coursesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                long courseId = (long) view.getTag();
                confirmDeleteCourse(courseId);
                return true;
            }
        });

        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCourseDialog();
            }
        });
    }

    private void showTipToast() {
        Toast toast = new Toast(this);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.course_toast, null);
        toast.setView(v);
        toast.show();
    }
    private void showdeleteTipToast() {
        Toast toast = new Toast(this);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.course_delete_toast, null);
        toast.setView(v);
        toast.show();
    }

    private void loadCourses() {
        Cursor cursor = dbHelper.getAllCourses();
        List<String> courseList = new ArrayList<>();
        final List<Long> courseIdList = new ArrayList<>(); // Store course IDs

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") long courseId = cursor.getLong(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String courseName = cursor.getString(cursor.getColumnIndex("name"));
                courseList.add(courseName);
                courseIdList.add(courseId); // Store course ID
            } while (cursor.moveToNext());
        }

        // Close the cursor
        if (cursor != null) {
            cursor.close();
        }

        // Update the ListView
        coursesAdapter = new ArrayAdapter<String>(this, R.layout.list_course, R.id.textview_course_name, courseList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                long courseId = courseIdList.get(position);
                view.setTag(courseId); // Set the tag for each item in the ListView to store its course ID

                Button editButton = view.findViewById(R.id.button_edit_course);
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editCourseName(courseId);
                    }
                });

                Button deleteButton = view.findViewById(R.id.button_delete_course);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirmDeleteCourse(courseId);
                    }
                });

                Button enterQuestionBankButton = view.findViewById(R.id.button_manage_questions);
                enterQuestionBankButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ManageCoursesActivity.this, ManageQuestionsActivity.class);
                        intent.putExtra("COURSE_ID", courseId);
                        startActivity(intent);
                    }
                });

                return view;
            }
        };
        coursesListView.setAdapter(coursesAdapter);
    }


    private void showAddCourseDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("添加课程");

        // Set up the input
        final EditText input = new EditText(this);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String courseName = input.getText().toString().trim();
                if (!courseName.isEmpty()) {
                    addCourse(courseName);
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);

        builder.show();
    }


    private void addCourse(String courseName) {
        dbHelper.addCourse(courseName);
        showTipToast();
        loadCourses(); // Refresh the list after adding
    }


    private void editCourseName(final long courseId) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("编辑课程");

        // Set up the input
        final EditText input = new EditText(this);
        input.setText(dbHelper.getCourseNameById(courseId)); // 获取课程名
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newCourseName = input.getText().toString().trim();
                if (!newCourseName.isEmpty()) {
                    dbHelper.updateCourse((int) courseId, newCourseName);
                    showTipToast();
                    loadCourses();
                }
            }
        });
        builder.setNegativeButton("取消", null);

        builder.show();
    }


    private void confirmDeleteCourse(long courseId) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("确认删除");
        builder.setMessage("确定要删除课程 " + dbHelper.getCourseNameById(courseId) + " 吗？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteCourse(courseId);
            }
        });
        builder.setNegativeButton("否", null);
        builder.show();
    }


    private void deleteCourse(long courseId) {
        int rowsAffected = dbHelper.deleteCourse(courseId);
        if (rowsAffected > 0) {
            showdeleteTipToast();
            loadCourses();
        } else {
            Toast.makeText(this, "课程删除失败", Toast.LENGTH_SHORT).show();
        }
    }
}