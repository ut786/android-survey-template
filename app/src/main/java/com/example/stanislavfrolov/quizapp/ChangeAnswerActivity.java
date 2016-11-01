package com.example.stanislavfrolov.quizapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class ChangeAnswerActivity extends ListActivity {

    AnswerDatabaseHelper answerDatabaseHelper;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        String[] allAnswers = getAllAnswersAsStrings();

        // TODO: 01.11.2016 Create separate activity and refactor
        if (allAnswers.length == 0) {
            Intent intent = new Intent(this, ThankYouActivity.class);
            startActivity(intent);
            finish();
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.activity_manual_input, R.id.label, allAnswers);
            setListAdapter(adapter);
        }
    }

    public String[] getAllAnswersAsStrings() {
        answerDatabaseHelper = new AnswerDatabaseHelper(this);
        List<Answer> allAnswers = answerDatabaseHelper.getAllAnsweredQuestions();

        return convertToStrings(allAnswers);
    }

    @NonNull
    private String[] convertToStrings(List<Answer> allAnswers) {
        String[] allAnswersAsStrings = new String[allAnswers.size()];

        for (int i = 0; i < allAnswers.size(); i++) {
            Answer answer = allAnswers.get(i);
            allAnswersAsStrings[i] = getAnswerAsString(answer);
        }

        return allAnswersAsStrings;
    }

    @NonNull
    private String getAnswerAsString(Answer answer) {
        return answer.getTimestamp() + " " + answer.getQuestion() + " " + answer.getAnswer();
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        String item = (String) getListAdapter().getItem(position);

        String questionID = getQuestionIdFromItem(item);
        String timestamp = getTimestampFromItem(item);

        Bundle bundle = new Bundle();
        putExtrasToBundle(bundle, questionID, timestamp);

        Intent intent = new Intent(this, SingleQuestionActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);
        finish();
    }

    private String getQuestionIdFromItem(String item) {
        String[] parts = item.split(" ");

        return parts[2];
    }

    @NonNull
    private String getTimestampFromItem(String item) {
        String[] parts = item.split(" ");

        return parts[0] + " " + parts[1];
    }

    @NonNull
    private Bundle putExtrasToBundle(Bundle bundle, String questionID, String timestamp) {
        bundle.putInt("questionID", Integer.parseInt(questionID) - 1);
        bundle.putString("timestamp", timestamp);

        return bundle;
    }

}
