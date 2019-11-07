package com.example.scheduleviewer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Spliterator;

public class MainActivity extends AppCompatActivity {

    public void weekPress(View view) {
        Intent myIntent = new Intent(MainActivity.this, WeekView.class); //Create a new intent
        MainActivity.this.startActivity(myIntent);
    }

    public void settingPress(View view) {
        Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class); //Create a new intent
        MainActivity.this.startActivity(myIntent);
    }

    public void dayPress(View view) {
        Intent myIntent = new Intent(MainActivity.this, DayView.class); //Create a new intent
        MainActivity.this.startActivity(myIntent);
    }

    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference conditionRef = rootRef.child("Course");
    DatabaseReference courseRef; //Get the reference of the course
    static ArrayList<DatabaseReference> dr = new ArrayList<DatabaseReference>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //for (Course a : Course.courseList) Log.i("COURSE", a.toString());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> sub = new ArrayList<>();

        //Get the Shared Preference from the app
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        //SettingsActivity.sharedPreferences = getSharedPreferences(SettingsActivity.SHARED_PREFS, MODE_PRIVATE);
        sub.add(sharedPreferences.getString("COURSE1", ""));
        sub.add(sharedPreferences.getString("COURSE2", ""));
        sub.add(sharedPreferences.getString("COURSE3", ""));
        sub.add(sharedPreferences.getString("COURSE4", ""));
        sub.add(sharedPreferences.getString("COURSE5", ""));
        sub.add(sharedPreferences.getString("COURSE6", ""));
        sub.add(sharedPreferences.getString("COURSE7", ""));

        Period.loadAPeriods();
        Period.loadSubjects(sub); //Load week A period and subjects

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Period.ratioX = (float)(width / 2560.0);
        Period.ratioY = (float)(height / 1704.0);

        //SettingsActivity.changeSubject(findViewById(R.id.change));
        //SettingsActivity sa = new SettingsActivity();

        //sa.changeSubject(findViewById(R.id.change));

        PeriodView.MyView wv = new PeriodView.MyView();
        ImageView image = findViewById(R.id.image);
        image.setImageDrawable(wv);

        Log.i("Width", "" + width); //2560
        Log.i("height", "" + height); //1704
    }

    protected void onStart() { //The whole method is used to get data from firebase
        super.onStart();

        conditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    //String key = child.getKey();
                    //Log.i("KEY", key);
                    //courseRef = conditionRef.child(key);
                    courseRef = child.getRef();
                    courseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Course temp = new Course();
                            int k = 0;

                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                Object text = data.getValue(Object.class);

                                switch (k) {
                                    case 0:
                                        temp.setName(text.toString());
                                        break;
                                    case 1:
                                        temp.setClassroom(text.toString());
                                        break;
                                    case 2:
                                        temp.setCourseNumber(Integer.parseInt(text.toString()));
                                        break;
                                    case 3:
                                        temp.setPeriod(Integer.parseInt(text.toString()));
                                        break;
                                    case 4:
                                        temp.setSection(Integer.parseInt(text.toString()));
                                        break;
                                    case 5:
                                        temp.setTeacher(text.toString());
                                        break;
                                }
                                k++;
                            }
                            Course.courseList.add(temp); //Add loaded course to courseList
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    dr.add(courseRef);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
