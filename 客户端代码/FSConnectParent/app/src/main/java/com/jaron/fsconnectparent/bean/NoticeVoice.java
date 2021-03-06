package com.jaron.fsconnectparent.bean;

import java.io.Serializable;

public class NoticeVoice implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column notice_voice.notice_voice_id
     *
     * @mbg.generated
     */
    private Integer noticeVoiceId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column notice_voice.notice_id
     *
     * @mbg.generated
     */
    private Integer noticeId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column notice_voice.notice_voice_path
     *
     * @mbg.generated
     */
    private String noticeVoicePath;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table notice_voice
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table notice_voice
     *
     * @mbg.generated
     */
    public NoticeVoice(Integer noticeVoiceId, Integer noticeId, String noticeVoicePath) {
        this.noticeVoiceId = noticeVoiceId;
        this.noticeId = noticeId;
        this.noticeVoicePath = noticeVoicePath;
    }

    public NoticeVoice(Integer noticeId, String noticeVoicePath) {
        this.noticeId = noticeId;
        this.noticeVoicePath = noticeVoicePath;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table notice_voice
     *
     * @mbg.generated
     */
    public NoticeVoice() {
        super();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column notice_voice.notice_voice_id
     *
     * @return the value of notice_voice.notice_voice_id
     *
     * @mbg.generated
     */
    public Integer getNoticeVoiceId() {
        return noticeVoiceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column notice_voice.notice_voice_id
     *
     * @param noticeVoiceId the value for notice_voice.notice_voice_id
     *
     * @mbg.generated
     */
    public void setNoticeVoiceId(Integer noticeVoiceId) {
        this.noticeVoiceId = noticeVoiceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column notice_voice.notice_id
     *
     * @return the value of notice_voice.notice_id
     *
     * @mbg.generated
     */
    public Integer getNoticeId() {
        return noticeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column notice_voice.notice_id
     *
     * @param noticeId the value for notice_voice.notice_id
     *
     * @mbg.generated
     */
    public void setNoticeId(Integer noticeId) {
        this.noticeId = noticeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column notice_voice.notice_voice_path
     *
     * @return the value of notice_voice.notice_voice_path
     *
     * @mbg.generated
     */
    public String getNoticeVoicePath() {
        return noticeVoicePath;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column notice_voice.notice_voice_path
     *
     * @param noticeVoicePath the value for notice_voice.notice_voice_path
     *
     * @mbg.generated
     */
    public void setNoticeVoicePath(String noticeVoicePath) {
        this.noticeVoicePath = noticeVoicePath == null ? null : noticeVoicePath.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table notice_voice
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
        NoticeVoice other = (NoticeVoice) that;
        return (this.getNoticeVoiceId() == null ? other.getNoticeVoiceId() == null : this.getNoticeVoiceId().equals(other.getNoticeVoiceId()))
            && (this.getNoticeId() == null ? other.getNoticeId() == null : this.getNoticeId().equals(other.getNoticeId()))
            && (this.getNoticeVoicePath() == null ? other.getNoticeVoicePath() == null : this.getNoticeVoicePath().equals(other.getNoticeVoicePath()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table notice_voice
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getNoticeVoiceId() == null) ? 0 : getNoticeVoiceId().hashCode());
        result = prime * result + ((getNoticeId() == null) ? 0 : getNoticeId().hashCode());
        result = prime * result + ((getNoticeVoicePath() == null) ? 0 : getNoticeVoicePath().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table notice_voice
     *
     * @mbg.generated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", noticeVoiceId=").append(noticeVoiceId);
        sb.append(", noticeId=").append(noticeId);
        sb.append(", noticeVoicePath=").append(noticeVoicePath);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}