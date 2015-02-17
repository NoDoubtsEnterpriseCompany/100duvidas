package com.nodoubts.serverclient.grouplecture;

import java.util.List;

import com.nodoubts.core.GroupLecture;
import com.nodoubts.core.User;
import com.nodoubts.exceptions.ApplicationViewException;

public interface GroupLectureService {

	String saveGroupLecture(GroupLecture groupLecture) throws ApplicationViewException;
	
	String registerStudent(String groupLectureId, User user) throws ApplicationViewException;
	
	List<GroupLecture> getGroupLecturesBySubject(String subjectId);
	
	List<GroupLecture> getGroupLecturesByUser(String username);
}
