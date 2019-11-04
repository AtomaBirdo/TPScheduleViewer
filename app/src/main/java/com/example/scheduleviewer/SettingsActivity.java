package com.example.scheduleviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    ArrayList<AutoCompleteTextView> subjects = new ArrayList<AutoCompleteTextView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ToggleButton tb = findViewById(R.id.typeOfWeek);

        tb.setChecked(Period.isBWeek);

        subjects.add((AutoCompleteTextView)findViewById(R.id.subject1));
        subjects.add((AutoCompleteTextView)findViewById(R.id.subject2));
        subjects.add((AutoCompleteTextView)findViewById(R.id.subject3));
        subjects.add((AutoCompleteTextView)findViewById(R.id.subject4));
        subjects.add((AutoCompleteTextView)findViewById(R.id.subject5));
        subjects.add((AutoCompleteTextView)findViewById(R.id.subject6));
        subjects.add((AutoCompleteTextView)findViewById(R.id.subject7));

        /*String[] subjectList = {"AP Biology","AP Calculus AB","AP Calculus BC",
                "AP English Literature & Composition",
                "AP English Language & Composition",
                "AP Chemistry","AP Computer Science A",
                "AP Computer Science Principles",
                "AP Microeconomics","AP Macroeconomics",
                "AP Environmental Science","AP European History",
                "AP Music Theory","AP Physics 1","AP Physics C: M",
                "AP Physics C: E/M","AP Statistics","AP U.S. History",
                "Advanced Art 1 H", "Advanced Art 2 H", "Advance Art 3 H",
                "Advanced Pre-calculus H", "Advanced Spanish H",
                "Advanced Spanish Literature H",
                "Advanced Spanish Language H", "Algebra 2 H",
                "American Experience History H",
                "American Experience Literature H",
                "Biology H", "Calculus H", "Chemistry H", "Chinese 3 H",
                "Chinese 4 H", "Chinese 5 H", "Creation Modern World H",
                "English 1 H", "English 2 H", "French 3 H", "French 4 H",
                "French 5 H", "Geometry H", "Latin 3 H", "Latin 4 H", "Latin 5 H",
                "Latin Language and Literature 1 H",
                "Latin Language and Literature 2 H",
                "Modern World H", "Multi-Variable Calculus H", "Physics First H",
                "Spanish 2 H", "Spanish 3 H", "Spanish 4 H", "Spanish 5 H",
                "Spanish Language & Culture H",
                "Advanced APP Development"};*/

        ArrayList<String> subjectList = new ArrayList<>();

        for (Course temp : Course.courseList){
            subjectList.add(temp.combine());
            Log.i("Course", temp.toString());
        }

        ArrayAdapter subjectAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, subjectList.toArray());

        for (int i = 0; i < subjects.size(); i++){
            subjects.get(i).setText(Period.subjects.get(i));
            subjects.get(i).setAdapter(subjectAdapter);
        }
    }

    public void weekType(View view){
        ToggleButton tb = (ToggleButton)view;
        if (tb.isChecked()) Period.loadBPeriods();
        else Period.loadAPeriods();
    }

    public void changeSubject(View view){
        String sub = "";
        for (int i = 0; i < subjects.size(); i++){
            sub = subjects.get(i).getText().toString();
            if (!sub.equals("")){
                Period.subjects.set(i, sub);
            }
        }
    }
}
