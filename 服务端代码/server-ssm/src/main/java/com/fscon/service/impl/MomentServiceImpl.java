package com.fscon.service.impl;

import com.fscon.dao.MomentMapper;
import com.fscon.pojo.Moment;
import com.fscon.service.MomentService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service("momentService")
@Transactional
public class MomentServiceImpl implements MomentService {
    @Autowired
    private MomentService momentService;

    @Autowired
    private MomentMapper momentMapper;


    public int addMoment(Moment moment) {
        return this.momentMapper.addMoment(moment);
    }

    public List<Moment> selectMomentByUserId(List<Integer> userIdList, RowBounds rowBounds) {
        return this.momentMapper.selectMomentByUserId(userIdList,rowBounds);
    }

    public List<Moment> selectMomentByUser(Integer userId, RowBounds rowBounds) {
        return this.momentMapper.selectMomentByUser(userId,rowBounds);
    }

    public Moment selectMomentByMomentId(Integer momentId) {
        return this.momentMapper.selectByPrimaryKey(momentId);
    }

    public int update(Moment moment) {
        return this.momentMapper.updateByPrimaryKeySelective(moment);
    }

    public int delete(Integer momentId) {
        return this.momentMapper.deleteByPrimaryKey(momentId);
    }
}
