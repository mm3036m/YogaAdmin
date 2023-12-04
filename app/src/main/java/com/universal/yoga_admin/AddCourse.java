package com.universal.yoga_admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import Models.CourseDetails;


public class AddCourse extends AppCompatActivity {
    private Spinner spinnerDayOfWeek;
    private NumberPicker numberPickerHour;
    private NumberPicker numberPickerMinute;
    private EditText capacityEditText;
    private EditText durationEditText;
    private EditText priceEditText;
    private Spinner typeSpinner;
    private EditText descriptionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        // Set up the Toolbar as the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Set a custom title for the app bar
        getSupportActionBar().setTitle("Add Yoga Course");

        // Initialize UI elements
        spinnerDayOfWeek = findViewById(R.id.spinnerDayOfWeek);
        numberPickerHour = findViewById(R.id.numberPickerHour);
        numberPickerMinute = findViewById(R.id.numberPickerMinute);
        capacityEditText = findViewById(R.id.editTextCapacity);
        durationEditText = findViewById(R.id.editTextDuration);
        priceEditText = findViewById(R.id.editTextPrice);
        typeSpinner = findViewById(R.id.spinnerType);
        descriptionEditText = findViewById(R.id.editTextDescription);

        // Set up NumberPicker for the hour and minute
        numberPickerHour.setMinValue(0);
        numberPickerHour.setMaxValue(23);

        numberPickerMinute.setMinValue(0);
        numberPickerMinute.setMaxValue(59);

        // Set up the spinner for the day of the week
        ArrayAdapter<CharSequence> dayOfWeekAdapter = ArrayAdapter.createFromResource(
                this, R.array.days_of_week, android.R.layout.simple_spinner_item);
        dayOfWeekAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDayOfWeek.setAdapter(dayOfWeekAdapter);

        // Set a listener for the spinner
        spinnerDayOfWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        // Set up the spinner for the type of class
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(
                this, R.array.yoga_types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        // Set a listener for the spinner
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Handle the back button's click event
        onBackPressed();
        return true;
    }

    public void submitYogaCourse(View view) {
        // Validate input fields
        if (isEmpty(capacityEditText) || isEmpty(durationEditText) || isEmpty(priceEditText)) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
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
        String description = descriptionEditText.getText().toString();

        // Create an instance of CourseDetails
        CourseDetails yogaCourseDetails = new CourseDetails(dayOfWeek, hour, minute,
                capacity, duration, price, type, description);

        // Launch ConfirmationActivity and pass the CourseDetails object
        Intent intent = new Intent(this, ConfirmationActivity.class);
        intent.putExtra("details", yogaCourseDetails);
        startActivity(intent);

    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }
}