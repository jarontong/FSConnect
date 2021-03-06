package com.fscon.dao;

import com.fscon.pojo.HomeworkImage;

import java.util.List;

public interface HomeworkImageMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table homework_image
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer homeworkImageId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table homework_image
     *
     * @mbg.generated
     */
    int insert(HomeworkImage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table homework_image
     *
     * @mbg.generated
     */
    int insertSelective(HomeworkImage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table homework_image
     *
     * @mbg.generated
     */
    HomeworkImage selectByPrimaryKey(Integer homeworkImageId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table homework_image
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(HomeworkImage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table homework_image
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(HomeworkImage record);

    int addHomeworkImage(List<HomeworkImage> homeworkImageList);

    List<HomeworkImage> selectHomeworkImageByHomeworkId(Integer homeworkId);

    int updateBatch(List<HomeworkImage> homeworkImageList);

    int deleteBatch(List<HomeworkImage> homeworkImageList);
}