package com.example.reviewmanagement.Helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    //数据库版本号
    private static final int DATABASE_VERSION=3;

    //数据库名称
    private static final String DATABASE_NAME="reviewQuestionBank.db";

    public DatabaseHelper(Context context ) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }


    //插入新的课程
    public void addCourse(String courseName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", courseName);
        db.insert("Courses", null, values);
        db.close();
    }

    //获取所有的课程
    public Cursor getAllCourses() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Courses", null);
    }

    //更新课程
    public void updateCourse(int id, String newCourseName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", newCourseName);
        db.update("Courses", values, "id = ?", new String[] { String.valueOf(id) });
        db.close();
    }

    //删除课程

    public int deleteCourse(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // 先删除该课程下的所有题目
        db.delete("Questions", "course_id = ?", new String[] { String.valueOf(id) });
        // 然后删除课程
        return db.delete("Courses", "id = ?", new String[] { String.valueOf(id) });
    }
    public String getCourseNameById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Courses", new String[]{"name"}, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String courseName = cursor.getString(cursor.getColumnIndex("name"));
            cursor.close();
            return courseName;
        }
        return null; // 如果找不到对应的课程，返回null
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建课程表
        String CREATE_TABLE_COURSES="CREATE TABLE Courses ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL)";
        db.execSQL(CREATE_TABLE_COURSES);

        //创建题目类型表
        String CREATE_TABLE_QUESTIONTYPES="CREATE TABLE QuestionTypes ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "type_name TEXT NOT NULL)";
        db.execSQL(CREATE_TABLE_QUESTIONTYPES);

        //创建题目表
        String CREATE_TABLE_QUESTIONS="CREATE TABLE Questions ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "course_id INTEGER, " +
                "question_type_id INTEGER, " +
                "question_text TEXT NOT NULL, " +
                "answer TEXT NOT NULL, " +
                "FOREIGN KEY(course_id) REFERENCES Courses(id), " +
                "FOREIGN KEY(question_type_id) REFERENCES QuestionTypes(id))";
        db.execSQL(CREATE_TABLE_QUESTIONS);
        // 初始化题目类型数据
        this.addInitialQuestionTypes(db);

    }
    private void addInitialQuestionTypes(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("type_name", "单选题");
        db.insert("QuestionTypes", null, values);
        values.put("type_name", "多选题");
        db.insert("QuestionTypes", null, values);
        values.put("type_name", "判断题");
        db.insert("QuestionTypes", null, values);
        values.put("type_name", "应用题");
        db.insert("QuestionTypes", null, values);
    }


    //插入新的问题类型
    public void addQuestionType(SQLiteDatabase db, String typeName) {
        ContentValues values = new ContentValues();
        values.put("type_name", typeName);
        db.insert("QuestionTypes", null, values);
        // 你不需要在这里调用 db.close();
    }

    //获取所有的问题类型
    public Cursor getAllQuestionTypes() {
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("DatabaseHelper", "Inserted new question type" );
        Log.d("DatabaseHelper", "Fetching all question types");
        return db.rawQuery("SELECT * FROM QuestionTypes", null);
    }

    //更新问题类型
    public void updateQuestionType(int id, String newTypeName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("type_name", newTypeName);
        db.update("QuestionTypes", values, "id = ?", new String[] { String.valueOf(id) });
        db.close();
    }

    //删除问题类型
    public void deleteQuestionType(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("QuestionTypes", "id = ?", new String[] { String.valueOf(id) });
        db.close();
    }

    //插入新的问题
    public void addQuestion(long courseId, int questionTypeId, String questionText,String answer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("course_id", courseId);
        values.put("question_type_id", questionTypeId);
        values.put("question_text", questionText);
        values.put("answer", answer);
        Log.d("DatabaseHelper", "Adding question: " + questionText);  // 添加日志输出
        db.insert("Questions", null, values);
        Log.d("DatabaseHelper", "Question added.");  // 添加日志输出
        db.close();
    }

    //获取所有的问题
    public Cursor getAllQuestions() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Questions", null);
    }
    // 获取特定课程的所有问题
    public Cursor getQuestionsByCourseId(long courseId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Questions WHERE course_id = ?";
        return db.rawQuery(query, new String[]{String.valueOf(courseId)});
    }
    public Cursor getQuestionsByCourseIdAndType(long courseId, int questionTypeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Questions WHERE course_id = ? AND question_type_id = ?";
        return db.rawQuery(query, new String[]{String.valueOf(courseId), String.valueOf(questionTypeId)});
    }

    @SuppressLint("Range")
    public String getQuestionTypeName(int questionTypeId) {
        Log.d("DatabaseHelper", "Getting question type name for id: " + questionTypeId); // Add this
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("QuestionTypes", new String[]{"type_name"}, "id=?", new String[]{String.valueOf(questionTypeId)}, null, null, null);
        String typeName = "";
        if (cursor != null && cursor.moveToFirst()) {
            typeName = cursor.getString(cursor.getColumnIndex("type_name"));
        }
        Log.d("DatabaseHelper", "Question type name: " + typeName); // And this
        cursor.close();
        return typeName;
    }

    public int getQuestionTypeById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("QuestionTypes", new String[]{"id"}, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") int typeId = cursor.getInt(cursor.getColumnIndex("id"));
            cursor.close();
            return typeId;
        }
        return -1; // 如果找不到对应的题目类型，返回-1
    }




    //更新问题
    public void updateQuestion(int id, int courseId, int questionTypeId, String newQuestionText, String newAnswer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("course_id", courseId);
        values.put("question_type_id", questionTypeId);
        values.put("question_text", newQuestionText);
        values.put("answer", newAnswer); // 更新答案字段
        db.update("Questions", values, "id = ?", new String[] { String.valueOf(id) });
        db.close();
    }


    //删除问题
    public void deleteQuestion(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Questions", "id = ?", new String[] { String.valueOf(id) });
        db.close();
    }

    public Cursor searchQuestions(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = "question_text LIKE ? OR answer LIKE ?";
        String[] selectionArgs = new String[]{"%" + query + "%", "%" + query + "%"};
        return db.query("questions", null, selection, selectionArgs, null, null, null);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //删除旧版本的表
        db.execSQL("DROP TABLE IF EXISTS Courses");
        db.execSQL("DROP TABLE IF EXISTS QuestionTypes");
        db.execSQL("DROP TABLE IF EXISTS Questions");
        // 添加 "判断题" 类型
        addQuestionType(db, "判断题");
        //创建新版本的表
        this.onCreate(db);
    }
}