package com.fscon.service.impl;

import com.fscon.dao.ClassMapper;
import com.fscon.pojo.Class;
import com.fscon.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("classService")
@Transactional
public class ClassServiceimpl implements ClassService {

    @Autowired
    private ClassMapper classMapper;
    public Class selectClassByClassId(Integer classId) {
        return this.classMapper.selectByPrimaryKey(classId);
    }
}
