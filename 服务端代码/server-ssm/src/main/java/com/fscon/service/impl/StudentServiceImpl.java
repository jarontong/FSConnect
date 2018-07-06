package com.fscon.service.impl;

import com.fscon.dao.StudentMapper;
import com.fscon.pojo.Student;
import com.fscon.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("studentService")
@Transactional
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;
    public Student findStudentByStudentID(Integer studentID) {
        return this.studentMapper.selectByPrimaryKey(studentID);
    }

    public Student findStudentByParams(Student student) {
        return this.studentMapper.selectByParams(student);
    }
}
