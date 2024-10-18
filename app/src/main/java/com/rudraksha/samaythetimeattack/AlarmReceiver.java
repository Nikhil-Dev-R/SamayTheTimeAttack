package com.rudraksha.samaythetimeattack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// Start the AlarmActivity
		Log.d("AlarmReceiver", "Alarm received!");
		Intent alarmIntent = new Intent(context, AlarmActivity.class);
		alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(alarmIntent);

		/*
		// Optionally, play the alarm sound
		MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.alarm_sound);
		mediaPlayer.start();

		mediaPlayer.setOnCompletionListener(MediaPlayer::release);*/
	}
}
