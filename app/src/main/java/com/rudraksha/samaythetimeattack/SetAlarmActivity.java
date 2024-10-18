package com.rudraksha.samaythetimeattack;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.rudraksha.samaythetimeattack.model.Alarm;
import java.util.Calendar;

public class SetAlarmActivity extends AppCompatActivity {

	private Calendar alarmTime;
	private MaterialTextView alarmText;
	private boolean timeSet = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_alarm);

		alarmText = findViewById(R.id.alarmText);
		alarmTime = Calendar.getInstance();

		alarmText.setText(String.format("%02d:%02d", alarmTime.get(Calendar.HOUR_OF_DAY), alarmTime.get(Calendar.MINUTE)));

		MaterialButton setTimeButton = findViewById(R.id.btnSetTime);
		setTimeButton.setOnClickListener(v -> {
			showTimePicker();
		});

		Button saveButton = findViewById(R.id.btnSaveAlarm);
		saveButton.setOnClickListener(v -> {
			if (timeSet) {
				Intent intent = new Intent();
				intent.putExtra("new_alarm", new Alarm(0, alarmTime.getTimeInMillis(), true)); // Ensure Alarm is Serializable or Parcelable
				setResult(RESULT_OK, intent);
				finish();
			} else {
				// Show error or feedback that time is not set
				alarmText.setText("Please set a time first.");
			}
		});
	}

	private void showTimePicker() {
		int hour = alarmTime.get(Calendar.HOUR_OF_DAY);
		int minute = alarmTime.get(Calendar.MINUTE);

		boolean is24HourFormat = android.text.format.DateFormat.is24HourFormat(this);

		new TimePickerDialog(this, (view, hourOfDay, selectedMinute) -> {
			alarmTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
			alarmTime.set(Calendar.MINUTE, selectedMinute);
			alarmText.setText(String.format("%02d:%02d", hourOfDay, selectedMinute));
			timeSet = true; // Mark that the time has been set
		}, hour, minute, is24HourFormat).show();
	}
}
