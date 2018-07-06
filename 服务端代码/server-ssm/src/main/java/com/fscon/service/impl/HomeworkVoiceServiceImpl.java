package com.fscon.service.impl;

import com.fscon.dao.HomeworkVoiceMapper;
import com.fscon.pojo.HomeworkVoice;
import com.fscon.service.HomeworkVoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("homeworkVoiceService")
@Transactional
public class HomeworkVoiceServiceImpl implements HomeworkVoiceService {

    @Autowired
    private HomeworkVoiceMapper homeworkVoiceMapper;

    public int addHomeworkVoice(List<HomeworkVoice> homeworkVoiceList) {
        return this.homeworkVoiceMapper.addHomeworkVoice(homeworkVoiceList);
    }

    public List<HomeworkVoice> selectHomeworkVoiceByHomeworkId(Integer homeworkId) {
        return this.homeworkVoiceMapper.selectHomeworkVoiceByHomeworkId(homeworkId);
    }

    public HomeworkVoice selectHomeworkByHomeworkVoiceId(Integer homeworkVoiceId) {
        return this.homeworkVoiceMapper.selectByPrimaryKey(homeworkVoiceId);
    }

    public int update(HomeworkVoice homeworkVoice) {
        return this.homeworkVoiceMapper.updateByPrimaryKeySelective(homeworkVoice);
    }

    public int delete(Integer homeworkVoiceId) {
        return this.homeworkVoiceMapper.deleteByPrimaryKey(homeworkVoiceId);
    }

    public int updateBatch(List<HomeworkVoice> homeworkVoiceList) {
        return this.homeworkVoiceMapper.updateBatch(homeworkVoiceList);
    }

    public int deleteBatch(List<HomeworkVoice> homeworkVoiceList) {
        return this.homeworkVoiceMapper.deleteBatch(homeworkVoiceList);
    }
}
