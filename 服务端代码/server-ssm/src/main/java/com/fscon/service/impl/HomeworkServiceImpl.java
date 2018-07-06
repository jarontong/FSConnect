package com.fscon.service.impl;

import com.fscon.dao.HomeworkMapper;
import com.fscon.pojo.Homework;
import com.fscon.service.HomeworkService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("homeworkService")
@Transactional
public class HomeworkServiceImpl implements HomeworkService {

    @Autowired
    private HomeworkMapper homeworkMapper;

    public int addHomework(Homework homework) {
        return this.homeworkMapper.addHomework(homework);
    }

    public List<Homework> selectHomeworkByClassId(Integer classId,RowBounds rowBounds) {
        return this.homeworkMapper.selectHomeworkByClassId(classId,rowBounds);
    }

    public List<Homework> selectHomeworkByTeacherId(Integer teacherId,RowBounds rowBounds) {
        return this.homeworkMapper.selectHomeworkByTeacherId(teacherId,rowBounds);
    }

    public Homework selectHomeworkByHomeworkId(Integer homeworkId) {
        return this.homeworkMapper.selectByPrimaryKey(homeworkId);
    }

    public int update(Homework homework) {
        return this.homeworkMapper.updateByPrimaryKeySelective(homework);
    }

    public int delete(Integer homeworkId) {
        return this.homeworkMapper.deleteByPrimaryKey(homeworkId);
    }
}
