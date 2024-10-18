package com.rudraksha.samaythetimeattack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textview.MaterialTextView;

public class AlarmActivity extends AppCompatActivity {

	private MaterialTextView messageTextView;
	private ImageButton snoozeButton;
	private ImageButton stopButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);

		messageTextView = findViewById(R.id.messageTextView);
		snoozeButton = findViewById(R.id.snoozeButton);
		stopButton = findViewById(R.id.stopButton);

		messageTextView.setText("Alarm is ringing!");

		// Start the AlarmService
		Intent serviceIntent = new Intent(this, AlarmService.class);
		startService(serviceIntent);

		snoozeButton.setOnClickListener(v -> {
			// Send snooze request to the service
			Intent snoozeIntent = new Intent(this, AlarmService.class);
			snoozeIntent.putExtra("snooze", true);
			startService(snoozeIntent);
			finish(); // Close the activity
		});

		stopButton.setOnClickListener(v -> {
			// Stop the alarm and close the activity
			stopService(new Intent(this, AlarmService.class));
			finish(); // Close the activity
		});
	}
}
