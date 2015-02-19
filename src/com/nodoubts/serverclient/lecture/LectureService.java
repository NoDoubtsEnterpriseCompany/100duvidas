package com.nodoubts.serverclient.lecture;

import java.util.List;

import com.nodoubts.core.Lecture;
import com.nodoubts.core.ScheduledLecture;
import com.nodoubts.exceptions.ApplicationViewException;

public interface LectureService {

	public String saveLecture(Lecture lecture) throws ApplicationViewException;

	public List<Lecture> getSubjectLectures(String get_id);

	public String scheduleLecture(ScheduledLecture lecture)
			throws ApplicationViewException;
	
	List<ScheduledLecture> getScheduledLecturesFromTeacher(String teacher_id);
	
	List<ScheduledLecture> getScheduledLecturesFromStudent(String student_id);
}
