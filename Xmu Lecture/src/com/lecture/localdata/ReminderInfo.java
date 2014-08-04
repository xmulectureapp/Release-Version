package com.lecture.localdata;

import java.io.Serializable;

public class ReminderInfo implements Serializable {
	private long eventId;
	private long reminderId;

	public ReminderInfo(long eID, long rID) {
		eventId = eID;
		reminderId = rID;
	}

	public long getEventId() {
		return eventId;
	}

	public long getReminderId() {
		return reminderId;
	}
}
