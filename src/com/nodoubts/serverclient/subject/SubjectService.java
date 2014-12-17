package com.nodoubts.serverclient.subject;

import com.nodoubts.core.Subject;
import com.nodoubts.exceptions.ApplicationViewException;

public interface SubjectService {
	
	public String addSubject(Subject subject) throws ApplicationViewException; 
	
	public Subject getSubject(String subjectName) throws ApplicationViewException;
	
	public Subject getSubjects() throws ApplicationViewException;
}
