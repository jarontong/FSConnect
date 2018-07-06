package com.fscon.service;

import com.fscon.pojo.HomeworkImage;

import java.util.List;

public interface HomeworkImageService {
    int addHomeworkImage(List<HomeworkImage> homeworkImageList);

    List<HomeworkImage> selectHomeworkImageByHomeworkId(Integer homeworkId);

    HomeworkImage selectHomeworkByHomeworkImageId(Integer homeworkImageId);

    int update(HomeworkImage homeworkImage);

    int delete(Integer homeworkImageId);

    int updateBatch(List<HomeworkImage> homeworkImageList);

    int deleteBatch(List<HomeworkImage> homeworkImageList);
}
