package Models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class CourseDetails implements Serializable {
    private long courseId;
    private String dayOfWeek;
    private int hour;
    private int minute;
    private int capacity;
    private String duration;
    private float price;
    private String type;
    private String description;

    public CourseDetails(String dayOfWeek, int hour, int minute, int capacity,
                             String duration, float price, String type, String description) {
        this.dayOfWeek = dayOfWeek;
        this.hour = hour;
        this.minute = minute;
        this.capacity = capacity;
        this.duration = duration;
        this.price = price;
        this.type = type;
        this.description = description;
    }


    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public long getCourseId() {
        return courseId;
    }
    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    @NonNull
    @Override
    public String toString() {
        return "Day of the week: " + dayOfWeek +
                "\nTime: " + String.format("%02d:%02d", hour, minute) +
                "\nCapacity: " + capacity +
                "\nDuration: " + duration +
                "\nPrice per class: " + price +
                "\nType of class: " + type +
                "\nDescription: " + description;
    }


}



