package com.fscon.service;

import com.fscon.pojo.Moment;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface MomentService {
    int addMoment(Moment moment);

    List<Moment> selectMomentByUserId(List<Integer> userIdList,RowBounds rowBounds);

    List<Moment> selectMomentByUser(Integer userId,RowBounds rowBounds);

    Moment selectMomentByMomentId(Integer momentId);

    int update(Moment moment);

    int delete(Integer momentId);
}
