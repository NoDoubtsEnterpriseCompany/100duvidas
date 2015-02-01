package com.nodoubts.serverclient.lecture;

import com.nodoubts.core.Lecture;
import com.nodoubts.exceptions.ApplicationViewException;

public interface LectureService {

	String saveLecture(Lecture lecture) throws ApplicationViewException;
	
}
