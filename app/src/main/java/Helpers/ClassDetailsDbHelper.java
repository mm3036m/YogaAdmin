package Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ClassDetailsDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "class_details.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "class_details";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DAY_NAME = "day_name";
    public static final String COLUMN_TEACHER = "teacher";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_ADDITIONAL_COMMENTS = "additional_comments";
    public static final String COLUMN_COURSE_ID = "course_id";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_DAY_NAME + " TEXT," +
                    COLUMN_TEACHER + " TEXT," +
                    COLUMN_DATE + " TEXT," +
                    COLUMN_ADDITIONAL_COMMENTS + " TEXT," +
                    COLUMN_COURSE_ID + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public ClassDetailsDbHelper(Context context) {
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

    public long insertClassDetails(String dayName, String teacher, String date, String additionalComments, long courseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DAY_NAME, dayName);
        values.put(COLUMN_TEACHER, teacher);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_ADDITIONAL_COMMENTS, additionalComments);
        values.put(COLUMN_COURSE_ID, courseId);

        long newRowId = db.insert(TABLE_NAME, null, values);
        db.close();
        return newRowId;
    }

    public Cursor getAllClassDetails() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                COLUMN_ID,
                COLUMN_DAY_NAME,
                COLUMN_TEACHER,
                COLUMN_DATE,
                COLUMN_ADDITIONAL_COMMENTS
        };

        return db.query(
                TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
    }

    public Cursor getClassDetailsByCourseId(long courseId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_ID,
                COLUMN_DAY_NAME,
                COLUMN_TEACHER,
                COLUMN_DATE,
                COLUMN_ADDITIONAL_COMMENTS
        };

        String selection = COLUMN_COURSE_ID + "=?";
        String[] selectionArgs = {String.valueOf(courseId)};

        return db.query(
                TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }


}