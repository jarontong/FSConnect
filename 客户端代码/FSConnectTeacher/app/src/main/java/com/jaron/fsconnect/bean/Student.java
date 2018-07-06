package com.jaron.fsconnect.bean;

import java.io.Serializable;

public class Student implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column student.student_id
     *
     * @mbg.generated
     */
    private Integer studentId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column student.student_num
     *
     * @mbg.generated
     */
    private String studentNum;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column student.student_name
     *
     * @mbg.generated
     */
    private String studentName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column student.student_sex
     *
     * @mbg.generated
     */
    private String studentSex;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column student.class_id
     *
     * @mbg.generated
     */
    private Integer classId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column student.responsible_parent
     *
     * @mbg.generated
     */
    private String responsibleParent;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column student.responsible_parent_telephone
     *
     * @mbg.generated
     */
    private String responsibleParentTelephone;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table student
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;


    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table student
     *
     * @mbg.generated
     */
    public Student(Integer studentId, String studentNum, String studentName, String studentSex, Integer classId, String responsibleParent, String responsibleParentTelephone) {
        this.studentId = studentId;
        this.studentNum = studentNum;
        this.studentName = studentName;
        this.studentSex = studentSex;
        this.classId = classId;
        this.responsibleParent = responsibleParent;
        this.responsibleParentTelephone = responsibleParentTelephone;
    }

    public Student(String studentNum, String studentName, String responsibleParent, String responsibleParentTelephone) {
        this.studentNum = studentNum;
        this.studentName = studentName;
        this.responsibleParent = responsibleParent;
        this.responsibleParentTelephone = responsibleParentTelephone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table student
     *
     * @mbg.generated
     */
    public Student() {
        super();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column student.student_id
     *
     * @return the value of student.student_id
     *
     * @mbg.generated
     */
    public Integer getStudentId() {
        return studentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column student.student_id
     *
     * @param studentId the value for student.student_id
     *
     * @mbg.generated
     */
    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column student.student_num
     *
     * @return the value of student.student_num
     *
     * @mbg.generated
     */
    public String getStudentNum() {
        return studentNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column student.student_num
     *
     * @param studentNum the value for student.student_num
     *
     * @mbg.generated
     */
    public void setStudentNum(String studentNum) {
        this.studentNum = studentNum == null ? null : studentNum.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column student.student_name
     *
     * @return the value of student.student_name
     *
     * @mbg.generated
     */
    public String getStudentName() {
        return studentName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column student.student_name
     *
     * @param studentName the value for student.student_name
     *
     * @mbg.generated
     */
    public void setStudentName(String studentName) {
        this.studentName = studentName == null ? null : studentName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column student.student_sex
     *
     * @return the value of student.student_sex
     *
     * @mbg.generated
     */
    public String getStudentSex() {
        return studentSex;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column student.student_sex
     *
     * @param studentSex the value for student.student_sex
     *
     * @mbg.generated
     */
    public void setStudentSex(String studentSex) {
        this.studentSex = studentSex == null ? null : studentSex.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column student.class_id
     *
     * @return the value of student.class_id
     *
     * @mbg.generated
     */
    public Integer getClassId() {
        return classId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column student.class_id
     *
     * @param classId the value for student.class_id
     *
     * @mbg.generated
     */
    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column student.responsible_parent
     *
     * @return the value of student.responsible_parent
     *
     * @mbg.generated
     */
    public String getResponsibleParent() {
        return responsibleParent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column student.responsible_parent
     *
     * @param responsibleParent the value for student.responsible_parent
     *
     * @mbg.generated
     */
    public void setResponsibleParent(String responsibleParent) {
        this.responsibleParent = responsibleParent == null ? null : responsibleParent.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column student.responsible_parent_telephone
     *
     * @return the value of student.responsible_parent_telephone
     *
     * @mbg.generated
     */
    public String getResponsibleParentTelephone() {
        return responsibleParentTelephone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column student.responsible_parent_telephone
     *
     * @param responsibleParentTelephone the value for student.responsible_parent_telephone
     *
     * @mbg.generated
     */
    public void setResponsibleParentTelephone(String responsibleParentTelephone) {
        this.responsibleParentTelephone = responsibleParentTelephone == null ? null : responsibleParentTelephone.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table student
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
        Student other = (Student) that;
        return (this.getStudentId() == null ? other.getStudentId() == null : this.getStudentId().equals(other.getStudentId()))
                && (this.getStudentNum() == null ? other.getStudentNum() == null : this.getStudentNum().equals(other.getStudentNum()))
                && (this.getStudentName() == null ? other.getStudentName() == null : this.getStudentName().equals(other.getStudentName()))
                && (this.getStudentSex() == null ? other.getStudentSex() == null : this.getStudentSex().equals(other.getStudentSex()))
                && (this.getClassId() == null ? other.getClassId() == null : this.getClassId().equals(other.getClassId()))
                && (this.getResponsibleParent() == null ? other.getResponsibleParent() == null : this.getResponsibleParent().equals(other.getResponsibleParent()))
                && (this.getResponsibleParentTelephone() == null ? other.getResponsibleParentTelephone() == null : this.getResponsibleParentTelephone().equals(other.getResponsibleParentTelephone()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table student
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getStudentId() == null) ? 0 : getStudentId().hashCode());
        result = prime * result + ((getStudentNum() == null) ? 0 : getStudentNum().hashCode());
        result = prime * result + ((getStudentName() == null) ? 0 : getStudentName().hashCode());
        result = prime * result + ((getStudentSex() == null) ? 0 : getStudentSex().hashCode());
        result = prime * result + ((getClassId() == null) ? 0 : getClassId().hashCode());
        result = prime * result + ((getResponsibleParent() == null) ? 0 : getResponsibleParent().hashCode());
        result = prime * result + ((getResponsibleParentTelephone() == null) ? 0 : getResponsibleParentTelephone().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table student
     *
     * @mbg.generated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", studentId=").append(studentId);
        sb.append(", studentNum=").append(studentNum);
        sb.append(", studentName=").append(studentName);
        sb.append(", studentSex=").append(studentSex);
        sb.append(", classId=").append(classId);
        sb.append(", responsibleParent=").append(responsibleParent);
        sb.append(", responsibleParentTelephone=").append(responsibleParentTelephone);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}