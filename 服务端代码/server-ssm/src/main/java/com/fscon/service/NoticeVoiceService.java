package com.fscon.service;

import com.fscon.pojo.NoticeVoice;

import java.util.List;

public interface NoticeVoiceService {
    int addNoticeVoice(List<NoticeVoice> noticeVoiceList);

    List<NoticeVoice> selectNoticeVoiceByNoticeId(Integer noticeId);

    NoticeVoice selectNoticeByNoticeVoiceId(Integer noticeVoiceId);

    int update(NoticeVoice noticeVoice);

    int delete(Integer noticeVoiceId);

    int updateBatch(List<NoticeVoice> noticeVoiceList);

    int deleteBatch(List<NoticeVoice> noticeVoiceList);
}
