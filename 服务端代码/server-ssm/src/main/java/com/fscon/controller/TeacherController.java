package com.fscon.controller;

import com.fscon.pojo.Context;
import com.fscon.pojo.ResultBean;
import com.fscon.pojo.Teacher;
import com.fscon.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.fscon.pojo.ResultBean.*;

@Controller
public class TeacherController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private TeacherService teacherService;

    @RequestMapping(value="/user_login",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<Teacher> login( @RequestParam(name="teacherTelephone") String teacherTelephone, @RequestParam(name="teacherPassword") String teacherPassword){
        ResultBean<Teacher> resultBean=new ResultBean<Teacher>();
        Teacher teacher=teacherService.login(teacherTelephone,teacherPassword);
        Cookie cookie1=new Cookie("teacherTelephone",teacherTelephone);
        Cookie cookie2=new Cookie("teacherPassword",teacherPassword);

        if(teacher!=null){
            resultBean.setCode(RESULT_SUCCESS);
            resultBean.setResult(teacher);
            response.addCookie(cookie1);
            response.addCookie(cookie2);

        }
        else{
            resultBean.setCode(RESULT_NOT_LOGIN);
            resultBean.setMessage("用户名或密码错误！");
        }
        return resultBean;
    }

    @RequestMapping(value="/teacher_update",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<Teacher> update(@RequestParam(name="teacherId") Integer teacherId,@RequestParam(name="teacherTelephone", required = false) String teacherTelephone,@RequestParam(name="teacherUsername", required = false) String teacherUsername,@RequestParam(name="teacherTruename", required = false) String teacherTruename,@RequestParam(name="teacherSex", required = false) Integer teacherSex,@RequestParam(name="major",required = false)String major){
        ResultBean<Teacher> resultBean=new ResultBean<Teacher>();
        Teacher teacher=new Teacher(teacherId,teacherTelephone,teacherUsername,null,teacherTruename,teacherSex,major,null);
System.out.print(123);
        if(teacherService.update(teacher)!=0){
            teacher=teacherService.findTeacherByID(teacherId);
            resultBean.setCode(RESULT_SUCCESS);
            resultBean.setResult(teacher);
            resultBean.setMessage("修改成功！");
        }
        else {
            resultBean.setCode(RESULT_UNKNOW);
            resultBean.setMessage("修改失败！");
        }
        return resultBean;
    }

    @RequestMapping(value="/teacher_change_password",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean changePassword(@RequestParam(name="teacherID") Integer teacherID,@RequestParam(name="teacherPassword") String teacherPassword){
        ResultBean resultBean=new ResultBean();
        Teacher teacher=new Teacher(teacherID,null,null,teacherPassword,null,null,null,null);
        if(teacherService.update(teacher)!=0){
            //teacher=teacherService.findParentByID(teacherID);
            resultBean.setCode(RESULT_SUCCESS);
            resultBean.setMessage("密码修改成功！");
        }
        else {
            resultBean.setCode(RESULT_UNKNOW);
            resultBean.setMessage("密码修改失败！");
        }
        return resultBean;
    }

    @RequestMapping(value="/teacher_change_icon",method=RequestMethod.POST)
    @ResponseBody
    public ResultBean<Teacher> changeIcon(@RequestParam(name="teacherID") Integer teacherID,HttpServletRequest request, HttpServletResponse response){
        ResultBean<Teacher> resultBean=new ResultBean<Teacher>();
        Teacher teacher;
        String s = request.getSession().getServletContext().getRealPath("/")+"upload/icon";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
        String res = sdf.format(new Date());
        // 创建年月文件夹
        Calendar calendar = Calendar.getInstance();
        File dateDirs = new File(calendar.get(Calendar.YEAR) + File.separator + (calendar.get(Calendar.MONTH)));
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        try {
            //获取文件集合
            MultipartFile imageFile = multipartRequest.getFile("headIcon");
            String originalFileName = imageFile.getOriginalFilename();
                // 新文件名
            String newFileName = res + originalFileName;
                // 新文件
            File newFile = new File(s + File.separator +"teacherIcon" + File.separator + dateDirs + File.separator + newFileName);
            if( !newFile.getParentFile().exists()) {
                    // 如果目标文件所在的目录不存在，则创建父目录
                newFile.getParentFile().mkdirs();
            }
            imageFile.transferTo(newFile);
            teacher=new Teacher(teacherID,null,null,null,null,null,null,Context.path+"/icon/teacherIcon/"+calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH)) + "/"+ newFileName);
        }catch(IOException e){
            resultBean.setCode(RESULT_ERROR);
            resultBean.setMessage("头像修改失败！");
            e.printStackTrace();
            return resultBean;
        }


        if(teacherService.update(teacher)!=0){
            teacher=teacherService.findTeacherByID(teacherID);
            resultBean.setResult(teacher);
            resultBean.setCode(RESULT_SUCCESS);
            resultBean.setMessage("头像修改成功！");
        }else {
            resultBean.setCode(RESULT_UNKNOW);
            resultBean.setMessage("头像修改失败！");
        }
        return resultBean;
    }


}
