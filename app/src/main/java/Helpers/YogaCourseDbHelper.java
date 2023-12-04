package Helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import Models.CourseDetails;

public class YogaCourseDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "yoga_courses.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "yoga_courses";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DAY_OF_WEEK = "day_of_week";
    public static final String COLUMN_HOUR = "hour";
    public static final String COLUMN_MINUTE = "minute";
    public static final String COLUMN_CAPACITY = "capacity";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_DESCRIPTION = "description";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_DAY_OF_WEEK + " TEXT," +
                    COLUMN_HOUR + " INTEGER," +
                    COLUMN_MINUTE + " INTEGER," +
                    COLUMN_CAPACITY + " INTEGER," +
                    COLUMN_DURATION + " INTEGER," +
                    COLUMN_PRICE + " REAL," +
                    COLUMN_TYPE + " TEXT," +
                    COLUMN_DESCRIPTION + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public YogaCourseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public ArrayList<CourseDetails> getAllCourses() {
        ArrayList<CourseDetails> courseList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                COLUMN_ID,
                COLUMN_DAY_OF_WEEK,
                COLUMN_HOUR,
                COLUMN_MINUTE,
                COLUMN_CAPACITY,
                COLUMN_DURATION,
                COLUMN_PRICE,
                COLUMN_TYPE,
                COLUMN_DESCRIPTION
        };

        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            // Create a CourseDetails object for each row
            CourseDetails courseDetails = new CourseDetails(
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DAY_OF_WEEK)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HOUR)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MINUTE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CAPACITY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION)),
                    cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
            );

            // Set the courseId
            courseDetails.setCourseId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)));

            courseList.add(courseDetails);
        }

        cursor.close();
        return courseList;
    }
    public void deleteCourse(long courseId) {
        SQLiteDatabase db = getWritableDatabase();
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(courseId)};
        db.delete(TABLE_NAME, selection, selectionArgs);
    }

    public CourseDetails getCourseById(long courseId) {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                COLUMN_ID,
                COLUMN_DAY_OF_WEEK,
                COLUMN_HOUR,
                COLUMN_MINUTE,
                COLUMN_CAPACITY,
                COLUMN_DURATION,
                COLUMN_PRICE,
                COLUMN_TYPE,
                COLUMN_DESCRIPTION
                // Add other columns as needed
        };

        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(courseId)};

        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        CourseDetails courseDetails = null;

        if (cursor.moveToFirst()) {
            // Retrieve course details from the cursor
            courseDetails = new CourseDetails(
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DAY_OF_WEEK)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HOUR)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MINUTE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CAPACITY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION)),
                    cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                    // Retrieve other columns as needed
            );
            courseDetails.setCourseId(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        }

        cursor.close();
        return courseDetails;
    }


}