package com.rudraksha.samaythetimeattack;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.rudraksha.samaythetimeattack.adapter.AlarmAdapter;
import com.rudraksha.samaythetimeattack.model.Alarm;

import java.util.List;

public class MainActivity extends AppCompatActivity {

	private AlarmAdapter alarmAdapter;
	private List<Alarm> alarmList;
	private AlarmDatabase alarmDatabase;

	// Create an ActivityResultLauncher
	private final ActivityResultLauncher<Intent> alarmLauncher = registerForActivityResult(
			new ActivityResultContracts.StartActivityForResult(), this::onActivityResult);

	private void onActivityResult(ActivityResult result) {
		if (result.getResultCode() == RESULT_OK) {
			Intent data = result.getData();
			if (data != null) {
				Alarm alarm = (Alarm) data.getSerializableExtra("new_alarm");
				if (alarm != null) {
					alarmDatabase.insertAlarm(alarm);
					alarmList.add(alarm);
					alarmAdapter.notifyItemInserted(alarmList.size() - 1);
					alarmList.sort((a1, a2) -> (int) (a1.getTimeInMillis() - a2.getTimeInMillis()));
				}
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		MaterialTextView upcomingAlarm = findViewById(R.id.upcomingAlarm);
		RecyclerView recyclerView = findViewById(R.id.recyclerViewAlarms);
		FloatingActionButton fab = findViewById(R.id.fabAddAlarm);

		alarmDatabase = new AlarmDatabase(this);
		alarmList = alarmDatabase.getAllAlarms();

		if (alarmList != null && !alarmList.isEmpty()) {
			for (Alarm alarm : alarmList) {
				if (alarm.isActive()) {
					String formattedTime = "Upcoming Alarm: " + alarm.getFormattedTime();
					upcomingAlarm.setText(formattedTime);
					break;
				}
			}
		} else {
			upcomingAlarm.setText(R.string.no_upcoming_alarm);
		}

		alarmAdapter = new AlarmAdapter(this, alarmList);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(alarmAdapter);

		fab.setOnClickListener(v -> {
			Intent intent = new Intent(MainActivity.this, SetAlarmActivity.class);
			alarmLauncher.launch(intent); // Use the launcher to start the activity
		});
	}
}