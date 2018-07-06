package com.fscon.service;

import com.fscon.pojo.Homework;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface HomeworkService {
    int addHomework(Homework homework);

    List<Homework> selectHomeworkByClassId(Integer classId,RowBounds rowBounds);

    List<Homework> selectHomeworkByTeacherId(Integer teacherId,RowBounds rowBounds);

    Homework selectHomeworkByHomeworkId(Integer homeworkId);

    int update(Homework homework);

    int delete(Integer homeworkId);
}
