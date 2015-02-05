package com.nodoubts.core;

import java.util.Date;

public class ModuleFactory {
	public static ScheduledLecture createScheduledFactory(
			String subject,
			User teacher,
			Double price,
			Date date,
			String address,
			String student){
		ScheduledLecture scheduledLecture = new ScheduledLecture();
		scheduledLecture.setDate(date);
		scheduledLecture.setTeacher(teacher);
		scheduledLecture.setPrice(price);
		scheduledLecture.setSubject(subject);
		scheduledLecture.setStudent(student);
		scheduledLecture.setAddress(address);
		return scheduledLecture;
	}
}
