package com.universal.yoga_admin;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import Helpers.ClassDetailsDbHelper;

public class ClassDetailsCursorAdapter extends CursorAdapter {

    public ClassDetailsCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_class_details, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Extract class details from the cursor
        String dayName = cursor.getString(cursor.getColumnIndexOrThrow(ClassDetailsDbHelper.COLUMN_DAY_NAME));
        String teacher = cursor.getString(cursor.getColumnIndexOrThrow(ClassDetailsDbHelper.COLUMN_TEACHER));
        String date = cursor.getString(cursor.getColumnIndexOrThrow(ClassDetailsDbHelper.COLUMN_DATE));
        String additionalComments = cursor.getString(cursor.getColumnIndexOrThrow(ClassDetailsDbHelper.COLUMN_ADDITIONAL_COMMENTS));

        // Set the extracted data to the views in the list item layout
        TextView textViewDayName = view.findViewById(R.id.textViewDayName);
        TextView textViewTeacher = view.findViewById(R.id.textViewTeacher);
        TextView textViewDate = view.findViewById(R.id.textViewDate);
        TextView textViewAdditionalComments = view.findViewById(R.id.textViewAdditionalComments);

        textViewDayName.setText("Day Name: " + dayName);
        textViewTeacher.setText("Teacher: " + teacher);
        textViewDate.setText("Date: " + date);
        textViewAdditionalComments.setText("Additional Comments: " + additionalComments);
    }
}