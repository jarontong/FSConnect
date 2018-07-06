package com.fscon.service;

import com.fscon.dao.MomentImageMapper;
import com.fscon.dao.MomentMapper;
import com.fscon.pojo.NoticeImage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface NoticeImageService {

    int addNoticeImage(List<NoticeImage> noticeImageList);

    List<NoticeImage> selectNoticeImageByNoticeId(Integer noticeId);

    NoticeImage selectNoticeByNoticeImageId(Integer noticeImageId);

    int update(NoticeImage noticeImage);

    int delete(Integer noticeImageId);

    int updateBatch(List<NoticeImage> noticeImageList);

    int deleteBatch(List<NoticeImage> noticeImageList);

}
