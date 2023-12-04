package com.universal.yoga_admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Helpers.ApiHelper;
import Helpers.ClassDetailsDbHelper;
import Helpers.YogaCourseDbHelper;
import Models.CourseDetails;

public class CourseListActivity extends AppCompatActivity {

    private YogaCourseDbHelper courseDbHelper;
    private ClassDetailsDbHelper classDetailsDbHelper;
    private CourseListAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar ;

    private TextView noDataLabel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        // Set up the Toolbar as the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Set a custom title for the app bar
        getSupportActionBar().setTitle("Yoga - Admin App ");
        courseDbHelper = new YogaCourseDbHelper(this);
        classDetailsDbHelper = new ClassDetailsDbHelper(this);
        progressBar = findViewById(R.id.progressBar);
        noDataLabel = findViewById(R.id.no_data);
        // Initialize SwipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Reload data when swiped
                refreshData();
            }
        });

        // Initialize ListView and set the adapter
        ListView listView = findViewById(R.id.listViewCourses);
        adapter = new CourseListAdapter(this, getAllCourses());
        listView.setAdapter(adapter);
        if(adapter.getCount()==0){
            noDataLabel.setText("No courses added yet");
            noDataLabel.setVisibility(View.VISIBLE);
        }
        else{
            noDataLabel.setVisibility(View.INVISIBLE);
        }

    }

    private ArrayList<CourseDetails> getAllCourses() {
        // Retrieve all course details from the database
        return courseDbHelper.getAllCourses();
    }

    // Handle the click event for the Add Course button
    public void addCourse(View view) {
        Intent intent = new Intent(this, AddCourse.class);
        startActivity(intent);
    }

    public void uploadToCloud(View view){
        Map<String, Object> apiData = prepareApiData();
        Gson gson = new Gson();
        String jsonData = gson.toJson(apiData);


        ApiHelper uploadTask = new ApiHelper(new ApiHelper.Callback() {
            @Override
            public void onTaskComplete(String result) {
                // Display a Toast message based on the result
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            }
        }, progressBar);

        uploadTask.execute(jsonData);
    }

    // Handle the delete button click
    public void deleteCourse(View view) {
        int position = (int) view.getTag();
        CourseDetails selectedCourse = adapter.getItem(position);
        if (selectedCourse != null) {
            // Delete course from the database
            courseDbHelper.deleteCourse(selectedCourse.getCourseId());

            // Update the ListView
            adapter.remove(selectedCourse);
            adapter.notifyDataSetChanged();

            Toast.makeText(this, "Delete course: " + selectedCourse.getType(), Toast.LENGTH_SHORT).show();
        }
    }
    // Refresh data method
    private void refreshData() {
        // Show the ProgressBar
        progressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Reload data
                adapter.clear();
                adapter.addAll(getAllCourses());
                adapter.notifyDataSetChanged();

                // Hide the ProgressBar
                progressBar.setVisibility(View.INVISIBLE);

                if(adapter.getCount()==0){
                    noDataLabel.setText("No courses added yet");
                    noDataLabel.setVisibility(View.VISIBLE);
                }
                else{
                    noDataLabel.setVisibility(View.INVISIBLE);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    public  Map<String, Object> prepareApiData() {
        String userId="mm3036m";
        Map<String, Object> apiData = new HashMap<>();
        apiData.put("userId", userId);

        List<Map<String, Object>> detailList = new ArrayList<>();

        // Get all courses from the database
        ArrayList<CourseDetails> courses = courseDbHelper.getAllCourses();

        for (CourseDetails course : courses) {
            Map<String, Object> courseMap = new HashMap<>();
            courseMap.put("dayOfWeek", course.getDayOfWeek());
            courseMap.put("timeOfDay", String.format(Locale.US, "%02d:%02d", course.getHour(), course.getMinute()));

            List<Map<String, Object>> classList = new ArrayList<>();

            // Get all class details for the current course
            Cursor classDetailsCursor = classDetailsDbHelper.getClassDetailsByCourseId(course.getCourseId());

            while (classDetailsCursor.moveToNext()) {
                Map<String, Object> classDetailsMap = new HashMap<>();
                classDetailsMap.put("date", classDetailsCursor.getString(classDetailsCursor.getColumnIndexOrThrow(ClassDetailsDbHelper.COLUMN_DATE)));
                classDetailsMap.put("teacher", classDetailsCursor.getString(classDetailsCursor.getColumnIndexOrThrow(ClassDetailsDbHelper.COLUMN_TEACHER)));

                classList.add(classDetailsMap);
            }

            classDetailsCursor.close();

            courseMap.put("classList", classList);

            detailList.add(courseMap);
        }

        apiData.put("detailList", detailList);

        return apiData;
    }

}