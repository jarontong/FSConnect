package com.fscon.service;

import com.fscon.pojo.Parent;

public interface ParentService {
    Parent login(String parentTelephone,String parentPassword);

    int register(Parent parent);

    int update(Parent parent);

    Parent findParentByTelephone(String parentTelephone);

    Parent findParentByID(Integer parentID);

    boolean changePassword(String parentID,String parentPassword);
}
