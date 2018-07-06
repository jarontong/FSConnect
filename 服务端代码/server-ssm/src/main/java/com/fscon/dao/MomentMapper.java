package com.fscon.dao;

import com.fscon.pojo.Moment;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface MomentMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table moment
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer momentsId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table moment
     *
     * @mbg.generated
     */
    int insert(Moment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table moment
     *
     * @mbg.generated
     */
    int insertSelective(Moment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table moment
     *
     * @mbg.generated
     */
    Moment selectByPrimaryKey(Integer momentsId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table moment
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(Moment record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table moment
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Moment record);

    int addMoment(Moment record);

    List<Moment> selectMomentByUserId(List<Integer> userIdList, RowBounds rowBounds);
    List<Moment> selectMomentByUser(Integer userId, RowBounds rowBounds);
}