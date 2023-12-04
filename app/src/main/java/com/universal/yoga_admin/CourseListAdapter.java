package com.universal.yoga_admin;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;

import Models.CourseDetails;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class CourseListAdapter extends ArrayAdapter<CourseDetails> {

    public CourseListAdapter(Context context, ArrayList<CourseDetails> courses) {
        super(context, 0, courses);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CourseDetails course = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_course, parent, false);
        }

        TextView textViewType = convertView.findViewById(R.id.textViewType);
        TextView textViewDay = convertView.findViewById(R.id.textViewDay);
        TextView textViewDuration = convertView.findViewById(R.id.textViewDuration);

        ImageButton buttonDelete = convertView.findViewById(R.id.buttonDelete);

        if (course != null) {
            textViewType.setText(course.getType());
            textViewDay.setText(course.getDayOfWeek());
            textViewDuration.setText(course.getDuration());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("CourseListAdapter", "Item clicked at position: " + position);
                    Intent intent = new Intent(getContext(), CourseDetailsActivity.class);
                    intent.putExtra("courseId", course.getCourseId());
                    getContext().startActivity(intent);

                }
            });


            buttonDelete.setTag(position);
        }

        return convertView;
    }
}