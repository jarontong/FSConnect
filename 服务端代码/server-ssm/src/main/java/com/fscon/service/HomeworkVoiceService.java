package com.fscon.service;

import com.fscon.pojo.HomeworkVoice;

import java.util.List;

public interface HomeworkVoiceService {
    int addHomeworkVoice(List<HomeworkVoice> homeworkVoiceList);

    List<HomeworkVoice> selectHomeworkVoiceByHomeworkId(Integer homeworkId);

    HomeworkVoice selectHomeworkByHomeworkVoiceId(Integer homeworkVoiceId);

    int update(HomeworkVoice homeworkVoice);

    int delete(Integer homeworkVoiceId);

    int updateBatch(List<HomeworkVoice> homeworkVoiceList);

    int deleteBatch(List<HomeworkVoice> homeworkVoiceList);
}
