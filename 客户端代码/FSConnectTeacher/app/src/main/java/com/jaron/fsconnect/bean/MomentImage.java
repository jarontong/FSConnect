package com.jaron.fsconnect.bean;

import java.io.Serializable;

public class MomentImage implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column moment_image.moment_image_id
     *
     * @mbg.generated
     */
    private Integer momentImageId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column moment_image.moment_id
     *
     * @mbg.generated
     */
    private Integer momentsId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column moment_image.moment_image_path
     *
     * @mbg.generated
     */
    private String momentImagePath;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table moment_image
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table moment_image
     *
     * @mbg.generated
     */
    public MomentImage(Integer momentImageId, Integer momentsId, String momentImagePath) {
        this.momentImageId = momentImageId;
        this.momentsId = momentsId;
        this.momentImagePath = momentImagePath;
    }

    public MomentImage(Integer momentsId, String momentImagePath) {
        this.momentsId = momentsId;
        this.momentImagePath = momentImagePath;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table moment_image
     *
     * @mbg.generated
     */
    public MomentImage() {
        super();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column moment_image.moment_image_id
     *
     * @return the value of moment_image.moment_image_id
     *
     * @mbg.generated
     */
    public Integer getMomentImageId() {
        return momentImageId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column moment_image.moment_image_id
     *
     * @param momentImageId the value for moment_image.moment_image_id
     *
     * @mbg.generated
     */
    public void setMomentImageId(Integer momentImageId) {
        this.momentImageId = momentImageId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column moment_image.moment_id
     *
     * @return the value of moment_image.moment_id
     *
     * @mbg.generated
     */
    public Integer getMomentsId() {
        return momentsId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column moment_image.moment_id
     *
     * @param momentsId the value for moment_image.moment_id
     *
     * @mbg.generated
     */
    public void setMomentsId(Integer momentsId) {
        this.momentsId = momentsId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column moment_image.moment_image_path
     *
     * @return the value of moment_image.moment_image_path
     *
     * @mbg.generated
     */
    public String getMomentImagePath() {
        return momentImagePath;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column moment_image.moment_image_path
     *
     * @param momentImagePath the value for moment_image.moment_image_path
     *
     * @mbg.generated
     */
    public void setMomentImagePath(String momentImagePath) {
        this.momentImagePath = momentImagePath == null ? null : momentImagePath.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table moment_image
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
        MomentImage other = (MomentImage) that;
        return (this.getMomentImageId() == null ? other.getMomentImageId() == null : this.getMomentImageId().equals(other.getMomentImageId()))
            && (this.getMomentsId() == null ? other.getMomentsId() == null : this.getMomentsId().equals(other.getMomentsId()))
            && (this.getMomentImagePath() == null ? other.getMomentImagePath() == null : this.getMomentImagePath().equals(other.getMomentImagePath()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table moment_image
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getMomentImageId() == null) ? 0 : getMomentImageId().hashCode());
        result = prime * result + ((getMomentsId() == null) ? 0 : getMomentsId().hashCode());
        result = prime * result + ((getMomentImagePath() == null) ? 0 : getMomentImagePath().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table moment_image
     *
     * @mbg.generated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", momentImageId=").append(momentImageId);
        sb.append(", momentId=").append(momentsId);
        sb.append(", momentImagePath=").append(momentImagePath);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}