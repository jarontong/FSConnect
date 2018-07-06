package com.fscon.service;

import com.fscon.pojo.Notice;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface NoticeService {
    int addNotice(Notice notice);

    List<Notice> selectNoticeByClassId(Integer classId,RowBounds rowBounds);

    List<Notice> selectNoticeByTeacherId(Integer teacherId,RowBounds rowBounds);

    Notice selectNoticeByNoticeId(Integer noticeId);

    int update(Notice notice);

    int delete(Integer noticeId);
}
