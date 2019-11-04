package com.example.scheduleviewer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
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
    DatabaseReference courseRef;
    static ArrayList<DatabaseReference> dr = new ArrayList<DatabaseReference>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        for (Course a : Course.courseList) Log.i("COURSE", a.toString());
        super.onCreate(savedInstanceState);
        Period.loadAPeriods();
        Period.loadSubjects();
        setContentView(R.layout.activity_main);

        PeriodView.MyView wv = new PeriodView.MyView();
        ImageView image = findViewById(R.id.image);
        image.setImageDrawable(wv);
    }

    protected void onStart() {
        super.onStart();

        conditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String key = child.getKey();
                    Log.i("KEY", key);
                    courseRef = conditionRef.child(key);
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
                            Course.courseList.add(temp);
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
