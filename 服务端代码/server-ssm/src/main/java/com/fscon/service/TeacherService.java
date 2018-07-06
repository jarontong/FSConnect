package com.fscon.service;

import com.fscon.pojo.Teacher;

public interface TeacherService {
    Teacher login(String teacherTelephone,String teacherPassword);

    int update(Teacher teacher);

    Teacher findTeacherByTelephone(String teacherTelephone);

    Teacher findTeacherByID(Integer teacherID);

}
