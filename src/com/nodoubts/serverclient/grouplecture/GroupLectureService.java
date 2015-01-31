package com.nodoubts.serverclient.grouplecture;

import com.nodoubts.core.GroupLecture;
import com.nodoubts.exceptions.ApplicationViewException;

public interface GroupLectureService {

	String saveGroupLecture(GroupLecture groupLecture) throws ApplicationViewException;
	
}
