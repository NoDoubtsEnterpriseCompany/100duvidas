package com.nodoubts.serverclient.lecture;

import java.util.List;

import com.nodoubts.core.Lecture;
import com.nodoubts.exceptions.ApplicationViewException;

public interface LectureService {

	public String saveLecture(Lecture lecture) throws ApplicationViewException;

	public List<Lecture> getSubjectLectures(String get_id);
	
}