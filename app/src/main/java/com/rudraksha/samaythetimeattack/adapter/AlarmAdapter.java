package com.rudraksha.samaythetimeattack.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textview.MaterialTextView;
import com.rudraksha.samaythetimeattack.AlarmDatabase;
import com.rudraksha.samaythetimeattack.AlarmManagerHelper;
import com.rudraksha.samaythetimeattack.R;
import com.rudraksha.samaythetimeattack.model.Alarm;

import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

	private final List<Alarm> alarmList;
	private final Context context;
	private final AlarmDatabase alarmDatabase;

	public AlarmAdapter(Context context, List<Alarm> alarmList) {
		this.alarmList = alarmList;
		this.context = context;
		this.alarmDatabase = new AlarmDatabase(context);
	}

	@NonNull
	@Override
	public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(context).inflate(R.layout.alarm_list_item, parent, false);
		return new AlarmViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
		Alarm alarm = alarmList.get(position);

		holder.alarmTime.setText(alarm.getFormattedTime());
		holder.alarmSwitch.setChecked(alarm.isActive());
		AlarmManagerHelper.setAlarm(context, alarm);

		holder.alarmSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
			alarm.setActive(isChecked);
			alarmDatabase.updateAlarmStatus(alarm.getId(), isChecked);

			if (isChecked) {
				AlarmManagerHelper.setAlarm(context, alarm);
			} else {
				AlarmManagerHelper.cancelAlarm(context, alarm.getId());
			}

			holder.alarmSwitch.setChecked(alarm.isActive());
		});

		/*// Long Press to delete alarm
		holder.itemLayout.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				alarmDatabase.deleteAlarm(alarm.getId());
				alarmList.remove(alarm);
				notifyItemChanged(holder.getAdapterPosition());
				return false;
			}
		});*/
	}

	@Override
	public int getItemCount() {
		return alarmList.size();
	}

	public static class AlarmViewHolder extends RecyclerView.ViewHolder {

		ConstraintLayout itemLayout;
		MaterialTextView alarmTime;
		MaterialSwitch alarmSwitch;

		public AlarmViewHolder(@NonNull View itemView) {
			super(itemView);
			itemLayout = itemView.findViewById(R.id.itemLayout);
			alarmTime = itemView.findViewById(R.id.alarmTime);
			alarmSwitch = itemView.findViewById(R.id.alarmSwitch);
		}
	}
}