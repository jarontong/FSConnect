package com.fscon.service.impl;

import com.fscon.dao.TeacherMapper;
import com.fscon.pojo.Teacher;
import com.fscon.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("teacherService")
@Transactional
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    public Teacher login(String teacherTelephone, String teacherPassword) {
        return this.teacherMapper.login(teacherTelephone,teacherPassword);
    }

    public int update(Teacher teacher) {
        return this.teacherMapper.updateByPrimaryKeySelective(teacher);
    }

    public Teacher findTeacherByTelephone(String teacherTelephone) {
        return this.teacherMapper.selectByTeacherTelephone(teacherTelephone);
    }

    public Teacher findTeacherByID(Integer teacherID) {
        return this.teacherMapper.selectByPrimaryKey(teacherID);
    }


}
