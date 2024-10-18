package com.rudraksha.samaythetimeattack;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class AlarmService extends Service {

	private MediaPlayer mediaPlayer;
	private Handler handler;
	private boolean isSnoozed = false;

	@Override
	public void onCreate() {
		super.onCreate();
		handler = new Handler();
		playAudio();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("AlarmService", "Service started");
		// Check if snooze was requested
		if (intent != null && intent.getBooleanExtra("snooze", false)) {
			snoozeAlarm();
		}
		return START_STICKY; // Restart the service if killed
	}

	private void playAudio() {
		if (mediaPlayer != null) {
			mediaPlayer.release(); // Release previous instance if exists
		}
		mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound);
		mediaPlayer.start();

		// Release resources when audio completes
		mediaPlayer.setOnCompletionListener(mp -> {
			mp.release(); // Release media player after completion
			// Repeat the audio infinitely
			while(!isSnoozed) {
				// Restart audio after 5 minutes if not snoozed
				handler.postDelayed(this::playAudio, 5 * 60 * 1000);
			}
		});
	}

	private void snoozeAlarm() {
		isSnoozed = true;
		long snoozeTime = System.currentTimeMillis() + 5 * 60 * 1000; // 5 minutes
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		Intent intent = new Intent(this, AlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, snoozeTime, pendingIntent);
		stopAudio(); // Stop the audio
		stopSelf(); // Stop the service
	}

	private void stopAudio() {
		if (mediaPlayer != null) {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
			}
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null); // Clean up any pending messages
		stopAudio(); // Ensure audio stops if service is destroyed
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null; // We don't provide binding
	}
}
