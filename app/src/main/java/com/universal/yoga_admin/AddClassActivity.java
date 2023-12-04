package com.universal.yoga_admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Helpers.ClassDetailsDbHelper;

public class AddClassActivity extends AppCompatActivity {


    private EditText editTextDate;
    private DatePicker datePicker;
    private EditText editTextTeacher;
    private EditText editTextAdditionalComments;
    private Button buttonSubmit;
    private String dayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Set a custom title for the app bar
        getSupportActionBar().setTitle("Add Class");

        editTextDate = findViewById(R.id.editTextDate);
        datePicker = findViewById(R.id.datePicker);
        editTextTeacher = findViewById(R.id.editTextTeacher);
        editTextAdditionalComments = findViewById(R.id.editTextAdditionalComments);
        dayName= getIntent().getStringExtra("dayName");
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Set current date to the date picker
        setCurrentDate();

        // Handle the click on the date EditText
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        // Handle the click on the date picker icon
        ImageView imageViewDatePicker = findViewById(R.id.imageViewDatePicker);
        imageViewDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitClass();
            }
        });
    }
    private void showDatePicker() {
        datePicker.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String selectedDate = String.format("%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                    String selectedDayName = getDayName(selectedDate);

                    if (selectedDayName.equals(dayName)) {
                        // Update the date and dayName in the EditText
                        editTextDate.setText(selectedDate);
                        // Hide the date picker after the user selects a date
                        datePicker.setVisibility(View.GONE);
                    } else {
                        // Display a message or reset DatePicker to the current date
                        Toast.makeText(AddClassActivity.this, "Please select a "+dayName, Toast.LENGTH_SHORT).show();
                        setCurrentDate();
                    }
                }
            });
        }
    }


    private void setCurrentDate() {
        Calendar calendar = Calendar.getInstance();

        // Get the current day of the week
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Get the target day of the week
        int targetDayOfWeek = getDayOfWeek(dayName);

        // Calculate the number of days to the next occurrence of the target day
        int daysToAdd = (targetDayOfWeek - currentDayOfWeek + 7) % 7;

        // Add the days to the current date
        calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);

        // Get the new year, month, and day
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Format and set the new date
        String currentDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, day);
        editTextDate.setText(currentDate);
    }

    private void submitClass() {
        if(isEmpty(editTextDate) || isEmpty(editTextTeacher)){
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if(dayName!=null){
            String date = editTextDate.getText().toString();
            String teacher = editTextTeacher.getText().toString();
            String additionalComments = editTextAdditionalComments.getText().toString();
            dayName = getDayName(date);
            long courseId = getIntent().getLongExtra("courseId", -1);
            // Save the data to the database
            ClassDetailsDbHelper dbHelper = new ClassDetailsDbHelper(this);
            long newRowId = dbHelper.insertClassDetails(dayName, teacher, date, additionalComments, courseId);

            // Check if the data was successfully inserted
            if (newRowId != -1) {
                Toast.makeText(this, "Class added successfully", Toast.LENGTH_SHORT).show();
                dbHelper.close();
                finish();
            } else {
                dbHelper.close();
                Toast.makeText(this, "Error adding class", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private String getDayName(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = sdf.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            String[] days = {"", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
            return days[dayOfWeek];
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    private int getDayOfWeek(String dayName) {
        switch (dayName.toLowerCase()) {
            case "sunday":
                return Calendar.SUNDAY;
            case "monday":
                return Calendar.MONDAY;
            case "tuesday":
                return Calendar.TUESDAY;
            case "wednesday":
                return Calendar.WEDNESDAY;
            case "thursday":
                return Calendar.THURSDAY;
            case "friday":
                return Calendar.FRIDAY;
            case "saturday":
                return Calendar.SATURDAY;
            default:
                // Handle an unknown dayName
                return -1;
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        // Handle the back button's click event
        onBackPressed();
        return true;
    }


}