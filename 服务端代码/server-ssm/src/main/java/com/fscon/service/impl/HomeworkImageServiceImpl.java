package com.fscon.service.impl;

import com.fscon.dao.HomeworkImageMapper;
import com.fscon.pojo.HomeworkImage;
import com.fscon.service.HomeworkImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("homeworkImageService")
@Transactional
public class HomeworkImageServiceImpl implements HomeworkImageService {

    @Autowired
    private HomeworkImageMapper homeworkImageMapper;

    public int addHomeworkImage(List<HomeworkImage> homeworkImageList) {
        return this.homeworkImageMapper.addHomeworkImage(homeworkImageList);
    }

    public List<HomeworkImage> selectHomeworkImageByHomeworkId(Integer homeworkId) {
        return this.homeworkImageMapper.selectHomeworkImageByHomeworkId(homeworkId);
    }

    public HomeworkImage selectHomeworkByHomeworkImageId(Integer homeworkImageId) {
        return this.homeworkImageMapper.selectByPrimaryKey(homeworkImageId);
    }

    public int update(HomeworkImage homeworkImage) {
        return this.homeworkImageMapper.updateByPrimaryKeySelective(homeworkImage);
    }

    public int delete(Integer homeworkImageId) {
        return this.homeworkImageMapper.deleteByPrimaryKey(homeworkImageId);
    }

    public int updateBatch(List<HomeworkImage> homeworkImageList) {
        return this.homeworkImageMapper.updateBatch(homeworkImageList);
    }

    public int deleteBatch(List<HomeworkImage> homeworkImageList) {
        return this.homeworkImageMapper.deleteBatch(homeworkImageList);
    }
}
