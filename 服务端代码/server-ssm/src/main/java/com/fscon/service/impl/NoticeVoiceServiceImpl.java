package com.fscon.service.impl;

import com.fscon.dao.NoticeVoiceMapper;
import com.fscon.pojo.NoticeVoice;
import com.fscon.service.NoticeVoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("noticeVoiceService")
@Transactional
public class NoticeVoiceServiceImpl implements NoticeVoiceService {

    @Autowired
    private NoticeVoiceMapper noticeVoiceMapper;

    public int addNoticeVoice(List<NoticeVoice> noticeVoiceList) {
        return this.noticeVoiceMapper.addNoticeVoice(noticeVoiceList);
    }

    public List<NoticeVoice> selectNoticeVoiceByNoticeId(Integer noticeId) {
        return this.noticeVoiceMapper.selectNoticeVoiceByNoticeId(noticeId);
    }

    public NoticeVoice selectNoticeByNoticeVoiceId(Integer noticeVoiceId) {
        return this.noticeVoiceMapper.selectByPrimaryKey(noticeVoiceId);
    }

    public int update(NoticeVoice noticeVoice) {
        return this.noticeVoiceMapper.updateByPrimaryKeySelective(noticeVoice);
    }

    public int delete(Integer noticeVoiceId) {
        return this.noticeVoiceMapper.deleteByPrimaryKey(noticeVoiceId);
    }

    public int updateBatch(List<NoticeVoice> noticeVoiceList) {
        return this.noticeVoiceMapper.updateBatch(noticeVoiceList);
    }

    public int deleteBatch(List<NoticeVoice> noticeVoiceList) {
        return this.noticeVoiceMapper.deleteBatch(noticeVoiceList);
    }
}
