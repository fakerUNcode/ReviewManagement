package com.example.reviewmanagement.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.reviewmanagement.Activity.ManageQuestionsActivity;
import com.example.reviewmanagement.Activity.QuestionDetailActivity;
import com.example.reviewmanagement.Activity.SearchQuestionsActivity;
import com.example.reviewmanagement.Helper.DatabaseHelper;
import com.example.reviewmanagement.Item.QuestionItem;
import com.example.reviewmanagement.R;
import java.util.List;

public class QuestionListAdapter extends ArrayAdapter<QuestionItem> {
    private Context context;
    private List<QuestionItem> questionList;
    private DatabaseHelper dbHelper;

    public QuestionListAdapter(Context context, List<QuestionItem> questionList, DatabaseHelper dbHelper) {
        super(context, 0, questionList);
        this.context = context;
        this.questionList = questionList;
        this.dbHelper = dbHelper;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_question, parent, false);
        }

        QuestionItem questionItem = getItem(position);
        TextView questionText = convertView.findViewById(R.id.textview_question);
        ImageButton editButton = convertView.findViewById(R.id.button_edit);
        ImageButton deleteButton = convertView.findViewById(R.id.button_delete);
        Button detailButton = convertView.findViewById(R.id.button_detail);

        questionText.setText(questionItem.question);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof ManageQuestionsActivity) {
                    ((ManageQuestionsActivity) context).showEditQuestionDialog(questionItem);
                } else if (context instanceof SearchQuestionsActivity) {
                    ((SearchQuestionsActivity) context).showEditQuestionDialog(questionItem);
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof ManageQuestionsActivity) {
                    ((ManageQuestionsActivity) context).deleteQuestion(questionItem.id);
                } else if (context instanceof SearchQuestionsActivity) {
                    ((SearchQuestionsActivity) context).showDeleteQuestionDialog(questionItem.id);
                }
            }
        });

        detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到 QuestionDetailActivity，并传递问题、答案和类型
                Intent intent = new Intent(context, QuestionDetailActivity.class);
                intent.putExtra("question_detail", questionItem.question);
                intent.putExtra("question_answer", questionItem.answer);
                // 从数据库获取问题类型名称
                String questionTypeName = dbHelper.getQuestionTypeName(questionItem.questionTypeId);
                intent.putExtra("question_type", "Type: " + questionTypeName);
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
