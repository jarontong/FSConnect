package com.fscon.controller;

import com.fscon.pojo.Context;
import com.fscon.pojo.Parent;
import com.fscon.pojo.ResultBean;
import com.fscon.pojo.Student;
import com.fscon.service.ParentService;
import com.fscon.service.StudentService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

import static com.fscon.pojo.ResultBean.*;

@Controller
public class ParentController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private ParentService parentService;

    @Autowired
    private StudentService studentService;

    @RequestMapping(value="/parent_login",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<Parent> login(@RequestParam(name="parentTelephone") String parentTelephone, @RequestParam(name="parentPassword") String parentPassword){
        ResultBean<Parent> resultBean=new ResultBean<Parent>();
        Parent parent=parentService.login(parentTelephone,parentPassword);
        parent.setStudent(studentService.findStudentByStudentID(parent.getStudentId()));

        if(parent!=null){
            resultBean.setCode(RESULT_SUCCESS);
            resultBean.setResult(parent);
            Cookie cookie1=new Cookie("teacherTelephone",parentTelephone);
            Cookie cookie2=new Cookie("teacherPassword",parentPassword);
            response.addCookie(cookie1);
            response.addCookie(cookie2);

        }
        else{
            resultBean.setCode(RESULT_NOT_LOGIN);
            resultBean.setMessage("用户名或密码错误！");
        }
        return resultBean;
    }

    @RequestMapping(value="/parent_register",method=RequestMethod.POST)
    @ResponseBody
    public ResultBean<Parent> register(@RequestParam(name="parentTelephone") String parentTelephone, @RequestParam(name="parentPassword") String parentPassword, @RequestParam(name="parentTruename")String parentTruename){
        ResultBean<Parent> resultBean=new ResultBean<Parent>();

        if(parentService.findParentByTelephone(parentTelephone)!=null){
            resultBean.setCode(218);
            resultBean.setMessage("该号码已注册！");
        }
        else {
            Parent parent = new Parent(parentTelephone, parentPassword,parentTruename);
            parent.setParentUsername(parentTruename);
            int code = parentService.register(parent);
            System.out.print("code:"+code+"\n");
            if(code==0){
                resultBean.setCode(219);
                resultBean.setMessage("注册失败，请重试！");
            }
            else {
                System.out.print("parent:"+parent);
                resultBean.setCode(RESULT_SUCCESS);
                resultBean.setResult(parent);
                resultBean.setMessage("注册成功");
                Cookie cookie1 = new Cookie("parentTelephone", parentTelephone);
                Cookie cookie2 = new Cookie("parentPassword", parentPassword);
                response.addCookie(cookie1);
                response.addCookie(cookie2);
            }
        }
        return resultBean;
    }

    @RequestMapping(value="/parent_update",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean<Parent> update(@RequestParam(name="parentID") Integer parentID,@RequestParam(name="parentTelephone", required = false) String parentTelephone,@RequestParam(name="parentUsername", required = false) String parentUsername,@RequestParam(name="parentTruename", required = false) String parentTruename,@RequestParam(name="parentSex", required = false) Integer parentSex){
        ResultBean<Parent> resultBean=new ResultBean<Parent>();
        Parent parent=new Parent(parentID,parentTelephone,parentUsername,null,parentTruename,parentSex,null,null);

        if(parentService.update(parent)!=0){
            parent=parentService.findParentByID(parentID);
            resultBean.setCode(RESULT_SUCCESS);
            resultBean.setResult(parent);
            resultBean.setMessage("修改成功！");
        }
        else {
            resultBean.setCode(RESULT_UNKNOW);
            resultBean.setMessage("修改失败！");
        }
        return resultBean;
    }

    @RequestMapping(value="/parent_change_password",method = RequestMethod.POST)
    @ResponseBody
    public ResultBean changePassword(@RequestParam(name="parentID") Integer parentID,@RequestParam(name="parentPassword") String parentPassword){
        ResultBean resultBean=new ResultBean();
        Parent parent=new Parent(parentID,null,null,parentPassword,null,null,null,null);
        if(parentService.update(parent)!=0){
            //parent=parentService.findParentByID(parentID);
            resultBean.setCode(RESULT_SUCCESS);
            resultBean.setMessage("密码修改成功！");
        }
        else {
            resultBean.setCode(RESULT_UNKNOW);
            resultBean.setMessage("密码修改失败！");
        }
        return resultBean;
    }

    @RequestMapping(value="/parent_change_icon",method=RequestMethod.POST)
    @ResponseBody
    public ResultBean<Parent> changeIcon(@RequestParam(name="parentID") Integer parentID,HttpServletRequest request, HttpServletResponse response){
        ResultBean<Parent> resultBean=new ResultBean<Parent>();
        Parent parent;
        String s = request.getSession().getServletContext().getRealPath("/")+"upload/icon";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
        String res = sdf.format(new Date());
        Calendar calendar = Calendar.getInstance();
        File dateDirs = new File(calendar.get(Calendar.YEAR) + File.separator + (calendar.get(Calendar.MONTH)));
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        try {
            //获取文件集合
            MultipartFile imageFile = multipartRequest.getFile("headIcon");
            String originalFileName = imageFile.getOriginalFilename();
            // 新文件名
            String newFileName = res + originalFileName;
            File newFile = new File(s + File.separator +"parentIcon" + File.separator + dateDirs + File.separator + newFileName);
            if( !newFile.getParentFile().exists()) {
                // 如果目标文件所在的目录不存在，则创建父目录
                newFile.getParentFile().mkdirs();
            }
            imageFile.transferTo(newFile);
            parent=new Parent(parentID,null,null,null,null,null,Context.path+"/icon/parentIcon/"+calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH)) + "/"+ newFileName,null);
        }catch(IOException e){
            resultBean.setCode(RESULT_ERROR);
            resultBean.setMessage("头像修改失败！");
            e.printStackTrace();
            return resultBean;
        }

        if(parentService.update(parent)!=0){
            parent=parentService.findParentByID(parentID);
            resultBean.setResult(parent);
            resultBean.setCode(RESULT_SUCCESS);
            resultBean.setMessage("头像修改成功！");
        }else {
            resultBean.setCode(RESULT_UNKNOW);
            resultBean.setMessage("头像修改失败！");
        }
        return resultBean;
    }

    @RequestMapping(value="/associate_student",method=RequestMethod.POST)
    @ResponseBody
    public ResultBean<Parent> associateStudent(@RequestParam(name="parentTelephone") String parentTelephone,  @RequestParam(name="parentTruename")String parentTruename,@RequestParam(name="studentNum")String studentNum,@RequestParam(name="studentName")String studentName){
        ResultBean<Parent> resultBean=new ResultBean<Parent>();
        Student student= new Student(studentNum,studentName,parentTruename,parentTelephone);
        student=studentService.findStudentByParams(student);
        if(student!=null) {
            Parent parent = parentService.findParentByTelephone(parentTelephone);
            parent.setStudent(student);
            parent.setStudentId(student.getStudentId());
            parentService.update(parent);
            resultBean.setResult(parent);
            resultBean.setCode(RESULT_SUCCESS);
            resultBean.setMessage("关联成功！");

        }
        else {
            resultBean.setCode(RESULT_UNKNOW);
            resultBean.setMessage("关联失败！");
        }
        return resultBean;
    }

}
