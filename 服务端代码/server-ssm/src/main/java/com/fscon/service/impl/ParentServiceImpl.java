package com.fscon.service.impl;

import com.fscon.dao.ParentMapper;
import com.fscon.pojo.Parent;
import com.fscon.service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("parentService")
@Transactional
public class ParentServiceImpl implements ParentService {

    @Autowired
    private ParentMapper parentMapper;

    public Parent login(String parentTelephone, String parentPassword) {
        return this.parentMapper.login(parentTelephone,parentPassword);
    }

    public int register( Parent parent) {
        return this.parentMapper.register(parent);
    }

    public int update(Parent parent) {
        return this.parentMapper.updateByPrimaryKeySelective(parent);
    }

    public Parent findParentByTelephone(String parentTelephone) {
        return this.parentMapper.selectByParentTelephone(parentTelephone);
    }

    public Parent findParentByID(Integer parentID) {
        return this.parentMapper.selectByPrimaryKey(parentID);
    }

    public boolean changePassword(String parentID, String parentPassword) {
        return false;
    }


}
