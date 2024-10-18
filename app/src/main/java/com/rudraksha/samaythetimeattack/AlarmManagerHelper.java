package com.rudraksha.samaythetimeattack;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import com.rudraksha.samaythetimeattack.model.Alarm;

public class AlarmManagerHelper {

	public static void setAlarm(Context context, Alarm alarm) {
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AlarmReceiver.class);

		// Use PendingIntent.FLAG_IMMUTABLE or FLAG_MUTABLE based on your needs
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
		);

		if (alarmManager != null) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
				// Check if the app can schedule exact alarms
				if (alarmManager.canScheduleExactAlarms()) {
					// Set the exact alarm
					alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm.getTimeInMillis(), pendingIntent);
				} else {
					// Notify the user to grant permission
					Intent requestIntent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
					context.startActivity(requestIntent);
				}
			} else {
				// For API levels below S, directly set the exact alarm
				alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm.getTimeInMillis(), pendingIntent);
			}
		}
	}

	public static void cancelAlarm(Context context, int alarmId) {
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmId, intent,
				PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
		alarmManager.cancel(pendingIntent);
	}
}

