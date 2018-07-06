package com.fscon.service;

import com.fscon.pojo.Student;

public interface StudentService {
    Student findStudentByStudentID(Integer studentID);
    Student findStudentByParams(Student student);

}
