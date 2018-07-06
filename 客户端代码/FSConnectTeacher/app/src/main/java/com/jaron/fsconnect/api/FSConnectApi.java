package com.jaron.fsconnect.api;

import android.util.Log;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.util.List;

import static com.jaron.fsconnect.api.ApiHttpClient.get;
import static com.jaron.fsconnect.api.ApiHttpClient.post;

/**
 * Created by Jaron on 2017/5/8.
 */

public class FSConnectApi {

    public static final int REGISTER_INTENT = 1;
    public static final int RESET_PWD_INTENT = 2;

    /**
     * login account
     *
     * @param username username
     * @param password password
     * @param handler  handler
     */
    public static void login(String username, String password, TextHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("teacherTelephone", username);
        params.put("teacherPassword", password);

        post("user_login", params, handler);//改action
    }


    public static void register(String parentTelephone, String parentPassword,String parentTruename, TextHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("parentTelephone", parentTelephone);
        params.put("parentPassword",parentPassword);
        params.put("parentTruename",parentTruename);
        post("parent_register", params, handler);
    }

    /**
     * validate and get phone token
     *
     * @param phoneNumber phoneNumber
     * @param smsCode     smsCode
     * @param handler     handler
     */
    public static void retrieve(String phoneNumber, String smsCode, TextHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("phone", phoneNumber);
        params.put("code", smsCode);
        post("action/apiv2/phone_validate", params, handler);
    }

    public static void bindStudent(String parentTelephone, String parentTruename,String studentNum,String studentName, TextHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("parentTelephone", parentTelephone);
        params.put("parentTruename", parentTruename);
        params.put("studentNum", studentNum);
        params.put("studentName", studentName);
        post("associate_student", params, handler);
    }

    public static void resetPwd( Integer teacherID,String password
            , TextHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("teacherID", teacherID);
        params.put("teacherPassword", password);
        post("teacher_change_password", params, handler);
    }

    public static void addNotice(String title,String content,
                                 Integer teacherId, Integer classId,
                                 File[] imageFiles,
                                 File[] voiceFiles,TextHttpResponseHandler handler){
        try {
            RequestParams params = new RequestParams();
            params.put("title", title);
            params.put("content", content);
            params.put("teacherId", teacherId);
            params.put("classId", classId);
            params.put("imageFiles", imageFiles);
            params.put("voiceFiles", voiceFiles);
            Log.d("im",imageFiles.toString());
            post("add_notice", params, handler);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void updateNotice(Integer noticeId,String title,String content,
                                 Integer teacherId, Integer classId,
                                 File[] imageFiles,
                                 File[] voiceFiles,TextHttpResponseHandler handler){
        try {
            RequestParams params = new RequestParams();
            params.put("noticeId", noticeId);
            params.put("title", title);
            params.put("content", content);
            params.put("teacherId", teacherId);
            params.put("classId", classId);
            params.put("imageFiles", imageFiles);
            params.put("voiceFiles", voiceFiles);
            Log.d("im",imageFiles.toString());
            post("update_notice", params, handler);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void getNoticesByTeacher(Integer teacherId,int offset,TextHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("teacherId",teacherId);
        params.put("offset",offset);
        post("select_notice_by_teacher",params,handler);
    }

    public static void getNoticesByClass(Integer classId,int offset,TextHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("classId",classId);
        params.put("offset",offset);
        post("select_notice",params,handler);
    }

    public static void addHomework(Integer subjectId, Integer teacherId,
                                   Integer classId, String content,
                                 File[] imageFiles,
                                 File[] voiceFiles,TextHttpResponseHandler handler){
        try {
            RequestParams params = new RequestParams();
            params.put("subjectId", subjectId);
            params.put("teacherId", teacherId);
            params.put("classId", classId);
            params.put("content", content);
            params.put("imageFiles", imageFiles);
            params.put("voiceFiles", voiceFiles);
            Log.d("im",imageFiles.toString());
            post("add_homework", params, handler);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void updateHomework(Integer homeworkId,Integer subjectId, Integer teacherId,
                                      Integer classId, String content,
                                    File[] imageFiles,
                                    File[] voiceFiles,TextHttpResponseHandler handler){
        try {
            RequestParams params = new RequestParams();
            params.put("homeworkId", homeworkId);
            params.put("subjectId", subjectId);
            params.put("teacherId", teacherId);
            params.put("classId", classId);
            params.put("content", content);
            params.put("imageFiles", imageFiles);
            params.put("voiceFiles", voiceFiles);
            Log.d("im",imageFiles.toString());
            post("update_notice", params, handler);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void getHomeworkByTeacher(Integer teacherId,int offset,TextHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("teacherId",teacherId);
        params.put("offset",offset);
        post("select_homework_by_teacher",params,handler);
    }

    public static void getHomeworkByClass(Integer classId,int offset,TextHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("classId",classId);
        params.put("offset",offset);
        post("select_homework",params,handler);
    }

    public static void changeUsername(Integer teacherId,String teacherUsername,TextHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("teacherId",teacherId);
        params.put("teacherUsername",teacherUsername);
        post("teacher_update",params,handler);
    }

    public static void changePhone(Integer teacherId,String teacherTelephone,TextHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("teacherId",teacherId);
        params.put("teacherTelephone",teacherTelephone);
        post("teacher_update",params,handler);
    }

    public static void changeIcon(Integer teacherId,File headIcon,TextHttpResponseHandler handler) {
        try {
            RequestParams params = new RequestParams();
            params.put("teacherID", teacherId);
            params.put("headIcon", headIcon);
            post("teacher_change_icon", params, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void changeGender(Integer teacherId,Integer teacherSex,TextHttpResponseHandler handler){
        RequestParams params = new RequestParams();
        params.put("teacherId",teacherId);
        params.put("teacherSex",teacherSex);
        post("teacher_update",params,handler);
    }

    public static void addMoment(Integer teacherId, Integer parentId,
                                 String content, File[] imageFiles,TextHttpResponseHandler handler){
        try {
            RequestParams params = new RequestParams();
            params.put("teacherId", teacherId);
            params.put("parentId", parentId);
            params.put("content", content);
            params.put("imageFiles", imageFiles);
            post("add_moment", params, handler);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void getMoment(List<Integer> userIdList, int offset, TextHttpResponseHandler handler){

        RequestParams params = new RequestParams();
        for(int i=0;i<userIdList.size();i++){
            params.put("userId"+String.valueOf(i+1),userIdList.get(i));
        }
        //params.put("userId",userId);
        params.put("offset",offset);
        post("select_moment",params,handler);
    }

}
