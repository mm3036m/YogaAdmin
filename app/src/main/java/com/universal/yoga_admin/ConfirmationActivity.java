package com.universal.yoga_admin;


import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import Helpers.YogaCourseDbHelper;
import Models.CourseDetails;

public class ConfirmationActivity extends AppCompatActivity {

    private YogaCourseDbHelper dbHelper;
    private CourseDetails courseDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        // Set up the Toolbar as the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Set a custom title for the app bar
        getSupportActionBar().setTitle("Confirmation");

        dbHelper = new YogaCourseDbHelper(this);
        // Retrieve and display details
        Intent intent = getIntent();
        courseDetails = (CourseDetails) intent.getSerializableExtra("details");
        displayDetails(courseDetails);
    }

    private void displayDetails(CourseDetails yogaCourseDetails) {
        if (yogaCourseDetails != null) {
            TextView textViewDetails = findViewById(R.id.textViewDetails);
            textViewDetails.setText(yogaCourseDetails.toString());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Handle the back button's click event
        onBackPressed();
        return true;
    }

    public void editDetails(View view) {
        onBackPressed();
    }

    public void submitChanges(View view) {
        // Save changes to the database

        if (courseDetails != null) {
            // Create a new CourseDetails object
            CourseDetails newCourseDetails = new CourseDetails(
                    // Use getters from your CourseDetails class to retrieve values
                    courseDetails.getDayOfWeek(),
                    courseDetails.getHour(),
                    courseDetails.getMinute(),
                    courseDetails.getCapacity(),
                    courseDetails.getDuration(),
                    courseDetails.getPrice(),
                    courseDetails.getType(),
                    courseDetails.getDescription()
            );

            // Insert the new CourseDetails into the database
            long newRowId = dbHelper.getWritableDatabase().insert(
                    YogaCourseDbHelper.TABLE_NAME,
                    null,
                    getContentValuesFromCourseDetails(newCourseDetails)
            );

            if (newRowId != -1) {
                // Display a success message
                Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity
            } else {
                // Display an error message
                Toast.makeText(this, "Failed to save", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private ContentValues getContentValuesFromCourseDetails(CourseDetails courseDetails) {
        ContentValues values = new ContentValues();
        values.put(YogaCourseDbHelper.COLUMN_DAY_OF_WEEK, courseDetails.getDayOfWeek());
        values.put(YogaCourseDbHelper.COLUMN_HOUR, courseDetails.getHour());
        values.put(YogaCourseDbHelper.COLUMN_MINUTE, courseDetails.getMinute());
        values.put(YogaCourseDbHelper.COLUMN_CAPACITY, courseDetails.getCapacity());
        values.put(YogaCourseDbHelper.COLUMN_DURATION, courseDetails.getDuration());
        values.put(YogaCourseDbHelper.COLUMN_PRICE, courseDetails.getPrice());
        values.put(YogaCourseDbHelper.COLUMN_TYPE, courseDetails.getType());
        values.put(YogaCourseDbHelper.COLUMN_DESCRIPTION, courseDetails.getDescription());
        return values;
    }
}
