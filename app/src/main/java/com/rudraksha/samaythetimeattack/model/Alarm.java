package com.rudraksha.samaythetimeattack.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Alarm implements Serializable {

	private int id;
	private long timeInMillis;
	private boolean isActive;

	public Alarm(int id, long timeInMillis, boolean isActive) {
		this.id = id;
		this.timeInMillis = timeInMillis;
		this.isActive = isActive;
	}

	public int getId() {
		return id;
	}

	public long getTimeInMillis() {
		return timeInMillis;
	}

	public boolean isActive() {
		return isActive;
	}

	public String getFormattedTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeInMillis);
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
		return sdf.format(calendar.getTime());
	}

	public void setActive(boolean isChecked) {
		isActive = true;
	}
}
