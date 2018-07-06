package com.fscon.service.impl;

import com.fscon.dao.NoticeMapper;
import com.fscon.pojo.Notice;
import com.fscon.service.NoticeService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("noticeService")
@Transactional
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    public int addNotice(Notice notice) {
        return this.noticeMapper.addNotice(notice);
    }

    public List<Notice> selectNoticeByClassId(Integer classId, RowBounds rowBounds) {
        return this.noticeMapper.selectNoticeByClassId(classId,rowBounds);
    }

    public List<Notice> selectNoticeByTeacherId(Integer teacherId, RowBounds rowBounds) {
        return this.noticeMapper.selectNoticeByTeacherId(teacherId,rowBounds);
    }

    public Notice selectNoticeByNoticeId(Integer noticeId) {
        return this.noticeMapper.selectByPrimaryKey(noticeId);
    }

    public int update(Notice notice) {
        return this.noticeMapper.updateByPrimaryKeySelective(notice);
    }

    public int delete(Integer noticeId) {
        return this.noticeMapper.deleteByPrimaryKey(noticeId);
    }


}
