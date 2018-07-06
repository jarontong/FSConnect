package com.fscon.service.impl;

import com.fscon.dao.NoticeImageMapper;
import com.fscon.pojo.NoticeImage;
import com.fscon.service.NoticeImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("noticeImageService")
@Transactional
public class NoticeImageServiceImpl implements NoticeImageService {

    @Autowired
    private NoticeImageMapper noticeImageMapper;

    public int addNoticeImage(List<NoticeImage> noticeImageList) {
        return this.noticeImageMapper.addNoticeImage(noticeImageList);
    }

    public List<NoticeImage> selectNoticeImageByNoticeId(Integer noticeId) {
        return this.noticeImageMapper.selectNoticeImageByNoticeId(noticeId);
    }

    public NoticeImage selectNoticeByNoticeImageId(Integer noticeImageId) {
        return this.noticeImageMapper.selectByPrimaryKey(noticeImageId);
    }

    public int update(NoticeImage noticeImage) {
        return this.noticeImageMapper.updateByPrimaryKeySelective(noticeImage);
    }

    public int delete(Integer noticeImageId) {
        return this.noticeImageMapper.deleteByPrimaryKey(noticeImageId);
    }

    public int updateBatch(List<NoticeImage> noticeImageList) {
        return this.noticeImageMapper.updateBatch(noticeImageList);
    }

    public int deleteBatch(List<NoticeImage> noticeImageList) {
        return this.noticeImageMapper.deleteBatch(noticeImageList);
    }

}
