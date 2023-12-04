package com.universal.yoga_admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import Helpers.ClassDetailsDbHelper;
import Helpers.YogaCourseDbHelper;
import Models.CourseDetails;

public class CourseDetailsActivity extends AppCompatActivity {
    private YogaCourseDbHelper courseDbHelper;
    private ClassDetailsDbHelper dbHelper;
    private TextView textViewType;
    private TextView textViewDay;
    private TextView textViewDuration;
    private TextView textViewCapacity;
    private TextView textViewPrice;
    private TextView textViewDescription;
    private TextView listLabel;
    private Button addClass;
    private ListView listViewClassDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        // Set up the Toolbar as the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Set a custom title for the app bar
        getSupportActionBar().setTitle("Course details");
        // Initialize dbHelper
        courseDbHelper = new YogaCourseDbHelper(this);
        dbHelper = new ClassDetailsDbHelper(this);
        // Initialize TextViews
        textViewType = findViewById(R.id.textViewType);
        textViewDay = findViewById(R.id.textViewDay);
        textViewDuration = findViewById(R.id.textViewDuration);
        textViewCapacity = findViewById(R.id.textViewCapacity);
        textViewPrice = findViewById(R.id.textViewPrice);
        textViewDescription = findViewById(R.id.textViewDescription);
        listLabel = findViewById(R.id.list_label);
        listLabel.setText("");
        addClass = findViewById(R.id.btnAddClass);
        // Initialize ListView
        listViewClassDetails = findViewById(R.id.listViewClassDetails);

        // Retrieve courseId from the intent
        long courseId = getIntent().getLongExtra("courseId", -1);

        // Load course details using dbHelper
        CourseDetails courseDetails = courseDbHelper.getCourseById(courseId);

        // Display course details in TextViews
        if (courseDetails != null) {
            textViewType.setText("Type: " + courseDetails.getType());
            textViewDay.setText("Day: " + courseDetails.getDayOfWeek());
            textViewDuration.setText("Duration: " + courseDetails.getDuration());
            textViewCapacity.setText("Capacity: " + courseDetails.getCapacity());
            textViewPrice.setText("Price: " + courseDetails.getPrice());
            textViewDescription.setText("Description: " + courseDetails.getDescription());
            loadAndDisplayClassDetails(courseId);
        }
        addClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseDetailsActivity.this, AddClassActivity.class);
                intent.putExtra("dayName", courseDetails.getDayOfWeek());
                intent.putExtra("courseId", courseDetails.getCourseId());

                startActivity(intent);
            }
        });
    }
    private void loadAndDisplayClassDetails(long courseId) {
        // Use dbHelper to get a Cursor containing class details for the given courseId
        Cursor cursor = dbHelper.getClassDetailsByCourseId(courseId);
        if(cursor.getCount()>0){
            listLabel.setText("Class List:");
        }
        // Create an adapter and set it to the ListView
        ClassDetailsCursorAdapter adapter = new ClassDetailsCursorAdapter(this, cursor);
        listViewClassDetails.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Retrieve courseId from the intent
        long courseId = getIntent().getLongExtra("courseId", -1);

        // Load course details using dbHelper
        CourseDetails courseDetails = courseDbHelper.getCourseById(courseId);

        // Display course details in TextViews
        if (courseDetails != null) {
            loadAndDisplayClassDetails(courseId);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Handle the back button's click event
        onBackPressed();
        return true;
    }

}