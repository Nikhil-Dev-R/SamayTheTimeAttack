package com.rudraksha.samaythetimeattack;

import static com.rudraksha.samaythetimeattack.schema.Schema.COLUMN_ACTIVE;
import static com.rudraksha.samaythetimeattack.schema.Schema.COLUMN_ID;
import static com.rudraksha.samaythetimeattack.schema.Schema.COLUMN_TIME;
import static com.rudraksha.samaythetimeattack.schema.Schema.DATABASE_NAME;
import static com.rudraksha.samaythetimeattack.schema.Schema.DATABASE_VERSION;
import static com.rudraksha.samaythetimeattack.schema.Schema.TABLE_ALARM;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.rudraksha.samaythetimeattack.model.Alarm;

import java.util.ArrayList;
import java.util.List;

public class AlarmDatabase extends SQLiteOpenHelper {

	public AlarmDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String createTable = "CREATE TABLE " + TABLE_ALARM + "("
				+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COLUMN_TIME + " INTEGER, "
				+ COLUMN_ACTIVE + " INTEGER)";
		db.execSQL(createTable);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARM);
		onCreate(db);
	}

	// Insert a new alarm into the database
	public void insertAlarm(Alarm alarm) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_TIME, alarm.getTimeInMillis());
		values.put(COLUMN_ACTIVE, alarm.isActive() ? 1 : 0);
		db.insert(TABLE_ALARM, null, values);
		db.close();
	}

	// Get all alarms from the database
	public List<Alarm> getAllAlarms() {
		List<Alarm> alarmList = new ArrayList<>();
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT * FROM " + TABLE_ALARM + " ORDER BY " + COLUMN_TIME + " ASC";
		Cursor cursor = db.rawQuery(selectQuery, null);

		try {
			if (cursor.moveToFirst()) {
				// Get column indices outside the loop to avoid repeated lookups
				int idColumnIndex = cursor.getColumnIndex(COLUMN_ID);
				int timeColumnIndex = cursor.getColumnIndex(COLUMN_TIME);
				int activeColumnIndex = cursor.getColumnIndex(COLUMN_ACTIVE);

				// Proceed only if all columns are found
				if (idColumnIndex != -1 && timeColumnIndex != -1 && activeColumnIndex != -1) {
					do {
						int id = cursor.getInt(idColumnIndex);
						long timeInMillis = cursor.getLong(timeColumnIndex);
						boolean isActive = cursor.getInt(activeColumnIndex) == 1;
						Alarm alarm = new Alarm(id, timeInMillis, isActive);
						alarmList.add(alarm);
					} while (cursor.moveToNext());
				} else {
					// Handle case where one or more columns are not found
					Log.e("DatabaseHelper", "Missing columns in cursor");
				}
			}
		} finally {
			cursor.close();
			db.close();
		}
		return alarmList;
	}

	// Delete an alarm by ID
	public void deleteAlarm(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_ALARM, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
		db.close();
	}

	// Update the activation status of an alarm
	public void updateAlarmStatus(int id, boolean isActive) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_ACTIVE, isActive ? 1 : 0);
		db.update(TABLE_ALARM, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
		db.close();
	}
}

