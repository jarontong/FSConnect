package com.jaron.fsconnect.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Moment implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column moment.moments_id
     *
     * @mbg.generated
     */
    private Integer momentsId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column moment.teacher_id
     *
     * @mbg.generated
     */
    private Integer teacherId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column moment.parent_id
     *
     * @mbg.generated
     */
    private Integer parentId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column moment.content
     *
     * @mbg.generated
     */
    private String content;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column moment.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    private Teacher teacher;

    private Parent parent;

    private List<MomentImage> momentImageList=null;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table moment
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table moment
     *
     * @mbg.generated
     */
    public Moment(Integer momentsId, Integer teacherId, Integer parentId, String content, Date createTime) {
        this.momentsId = momentsId;
        this.teacherId = teacherId;
        this.parentId = parentId;
        this.content = content;
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table moment
     *
     * @mbg.generated
     */
    public Moment() {
        super();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column moment.moments_id
     *
     * @return the value of moment.moments_id
     *
     * @mbg.generated
     */
    public Integer getMomentsId() {
        return momentsId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column moment.moments_id
     *
     * @param momentsId the value for moment.moments_id
     *
     * @mbg.generated
     */
    public void setMomentsId(Integer momentsId) {
        this.momentsId = momentsId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column moment.teacher_id
     *
     * @return the value of moment.teacher_id
     *
     * @mbg.generated
     */
    public Integer getTeacherId() {
        return teacherId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column moment.teacher_id
     *
     * @param teacherId the value for moment.teacher_id
     *
     * @mbg.generated
     */
    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column moment.parent_id
     *
     * @return the value of moment.parent_id
     *
     * @mbg.generated
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column moment.parent_id
     *
     * @param parentId the value for moment.parent_id
     *
     * @mbg.generated
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column moment.content
     *
     * @return the value of moment.content
     *
     * @mbg.generated
     */
    public String getContent() {
        return content;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column moment.content
     *
     * @param content the value for moment.content
     *
     * @mbg.generated
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column moment.create_time
     *
     * @return the value of moment.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column moment.create_time
     *
     * @param createTime the value for moment.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public List<MomentImage> getMomentImageList() {
        return momentImageList;
    }

    public void setMomentImageList(List<MomentImage> momentImageList) {
        this.momentImageList = momentImageList;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table moment
     *
     * @mbg.generated
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Moment other = (Moment) that;
        return (this.getMomentsId() == null ? other.getMomentsId() == null : this.getMomentsId().equals(other.getMomentsId()))
            && (this.getTeacherId() == null ? other.getTeacherId() == null : this.getTeacherId().equals(other.getTeacherId()))
            && (this.getParentId() == null ? other.getParentId() == null : this.getParentId().equals(other.getParentId()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table moment
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getMomentsId() == null) ? 0 : getMomentsId().hashCode());
        result = prime * result + ((getTeacherId() == null) ? 0 : getTeacherId().hashCode());
        result = prime * result + ((getParentId() == null) ? 0 : getParentId().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table moment
     *
     * @mbg.generated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", momentsId=").append(momentsId);
        sb.append(", teacherId=").append(teacherId);
        sb.append(", parentId=").append(parentId);
        sb.append(", content=").append(content);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}