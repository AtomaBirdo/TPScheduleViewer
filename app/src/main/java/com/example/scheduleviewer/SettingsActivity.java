package com.example.scheduleviewer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SettingsActivity extends AppCompatActivity {

    ArrayList<AutoCompleteTextView> subjects = new ArrayList<AutoCompleteTextView>();

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String COURSE1 = "COURSE1";
    public static final String COURSE2 = "COURSE2";
    public static final String COURSE3 = "COURSE3";
    public static final String COURSE4 = "COURSE4";
    public static final String COURSE5 = "COURSE5";
    public static final String COURSE6 = "COURSE6";
    public static final String COURSE7 = "COURSE7";

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

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        subjects.get(0).setText(sharedPreferences.getString(COURSE1, ""));
        subjects.get(1).setText(sharedPreferences.getString(COURSE2, ""));
        subjects.get(2).setText(sharedPreferences.getString(COURSE3, ""));
        subjects.get(3).setText(sharedPreferences.getString(COURSE4, ""));
        subjects.get(4).setText(sharedPreferences.getString(COURSE5, ""));
        subjects.get(5).setText(sharedPreferences.getString(COURSE6, ""));
        subjects.get(6).setText(sharedPreferences.getString(COURSE7, ""));

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
            if (!subjectList.contains((temp.combine()))) subjectList.add(temp.combine());
            //Log.i("Course", temp.toString());
        }

        ArrayAdapter subjectAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, subjectList.toArray());

        for (int i = 0; i < subjects.size(); i++){
            //subjects.get(i).setText(Period.subjects.get(i));
            subjects.get(i).setAdapter(subjectAdapter);
        }
    }

    public void weekType(View view){
        ToggleButton tb = (ToggleButton)view;
        if (tb.isChecked()) Period.loadBPeriods();
        else Period.loadAPeriods();
    }

    public void changeSubject(View view){
        ArrayList<String> courses = new ArrayList<>();

        for (int i = 0; i < subjects.size(); i++){
            String sub = subjects.get(i).getText().toString();
            if (!sub.equals("")){
                Course course = Course.findCourse(sub);
                if (course.getName() == null) Period.subjects.set(i, sub);
                else Period.subjects.set(i, course.getName() + " " + course.getSection());
            }
            courses.add(sub);
        }

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(COURSE1, courses.get(0));
        editor.putString(COURSE2, courses.get(1));
        editor.putString(COURSE3, courses.get(2));
        editor.putString(COURSE4, courses.get(3));
        editor.putString(COURSE5, courses.get(4));
        editor.putString(COURSE6, courses.get(5));
        editor.putString(COURSE7, courses.get(6));

        editor.commit();
    }
}
