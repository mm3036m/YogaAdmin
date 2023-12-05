package com.universal.yoga_admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import Helpers.YogaCourseDbHelper;
import Models.CourseDetails;

public class EditCourseActivity extends AppCompatActivity {
    private long courseId;
    private Spinner spinnerDayOfWeek;
    private NumberPicker numberPickerHour;
    private NumberPicker numberPickerMinute;
    private EditText capacityEditText;
    private EditText durationEditText;
    private EditText priceEditText;
    private Spinner typeSpinner;
    private EditText descriptionEditText;
    private Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        // Set up the Toolbar as the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Set a custom title for the app bar
        getSupportActionBar().setTitle("Edit Yoga Course");

        // Initialize UI elements
        spinnerDayOfWeek = findViewById(R.id.spinnerDayOfWeek);
        numberPickerHour = findViewById(R.id.numberPickerHour);
        numberPickerMinute = findViewById(R.id.numberPickerMinute);
        capacityEditText = findViewById(R.id.editTextCapacity);
        durationEditText = findViewById(R.id.editTextDuration);
        priceEditText = findViewById(R.id.editTextPrice);
        typeSpinner = findViewById(R.id.spinnerType);
        descriptionEditText = findViewById(R.id.editTextDescription2);
        submit = findViewById(R.id.updateCourse);

        ArrayAdapter<CharSequence> dayOfWeekAdapter = ArrayAdapter.createFromResource(
                this, R.array.days_of_week, android.R.layout.simple_spinner_item);
        dayOfWeekAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDayOfWeek.setAdapter(dayOfWeekAdapter);


        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(
                this, R.array.yoga_types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);



        // Get courseId from intent
        courseId = getIntent().getLongExtra("courseId", -1);

        // Retrieve course details from the database based on courseId
        CourseDetails courseDetails = new YogaCourseDbHelper(this).getCourseById(courseId);
        numberPickerHour.setMinValue(0);
        numberPickerHour.setMaxValue(23);

        numberPickerMinute.setMinValue(0);
        numberPickerMinute.setMaxValue(59);

        // Populate UI elements with existing data
        spinnerDayOfWeek.setSelection(getIndex(spinnerDayOfWeek, courseDetails.getDayOfWeek()));
        numberPickerHour.setValue(courseDetails.getHour());
        numberPickerMinute.setValue(courseDetails.getMinute());
        capacityEditText.setText(String.valueOf(courseDetails.getCapacity()));
        durationEditText.setText(courseDetails.getDuration());
        priceEditText.setText(String.valueOf(courseDetails.getPrice()));
        typeSpinner.setSelection(getIndex(typeSpinner, courseDetails.getType()));
        descriptionEditText.setText(courseDetails.getDescription()!=null?courseDetails.getDescription():"");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate input fields
                if (isEmpty(capacityEditText) || isEmpty(durationEditText) || isEmpty(priceEditText)) {
                    Toast.makeText(getApplicationContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Get user input
                String dayOfWeek = spinnerDayOfWeek.getSelectedItem().toString();
                int hour = numberPickerHour.getValue();
                int minute = numberPickerMinute.getValue();
                int capacity = Integer.parseInt(capacityEditText.getText().toString());
                String duration = durationEditText.getText().toString();
                float price = Float.parseFloat(priceEditText.getText().toString());
                String type = typeSpinner.getSelectedItem().toString();
                String description = descriptionEditText != null ? descriptionEditText.getText().toString() : "";


                // Create an instance of CourseDetails
                CourseDetails yogaCourseDetails = new CourseDetails(dayOfWeek, hour, minute,
                        capacity, duration, price, type, description);

                // Update the course details in the database
                new YogaCourseDbHelper(getApplicationContext()).updateCourse(courseId, yogaCourseDetails);

                // Display a success message
                Toast.makeText(getApplicationContext(), "Course updated successfully", Toast.LENGTH_SHORT).show();

                // Finish the activity
                finish();
            }
        });
    }

    private int getIndex(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Handle the back button's click event
        onBackPressed();
        return true;
    }



    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }
}