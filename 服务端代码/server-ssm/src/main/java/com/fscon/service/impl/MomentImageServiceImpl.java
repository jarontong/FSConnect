package com.fscon.service.impl;

import com.fscon.dao.MomentImageMapper;
import com.fscon.pojo.MomentImage;
import com.fscon.service.MomentImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service("momentImageService")
@Transactional
public class MomentImageServiceImpl implements MomentImageService {

    @Autowired
    private MomentImageMapper momentImageMapper;

    public int addMomentImage(List<MomentImage> momentImageList) {
        return this.momentImageMapper.addMomentImage(momentImageList);
    }

    public List<MomentImage> selectMomentImageByMomentId(Integer momentId) {
        return this.momentImageMapper.selectMomentImageByMomentId(momentId);
    }

    public MomentImage selectMomentByMomentImageId(Integer momentImageId) {
        return this.momentImageMapper.selectByPrimaryKey(momentImageId);
    }

    public int update(MomentImage momentImage) {
        return this.momentImageMapper.updateByPrimaryKeySelective(momentImage);
    }

    public int delete(Integer momentImageId) {
        return this.momentImageMapper.deleteByPrimaryKey(momentImageId);
    }

    public int updateBatch(List<MomentImage> momentImageList) {
        return this.momentImageMapper.updateBatch(momentImageList);
    }

    public int deleteBatch(List<MomentImage> momentImageList) {
        return this.momentImageMapper.deleteBatch(momentImageList);
    }
}
