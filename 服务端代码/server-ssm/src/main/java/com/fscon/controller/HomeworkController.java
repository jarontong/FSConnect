package com.fscon.controller;

import com.fscon.pojo.*;
import com.fscon.service.HomeworkImageService;
import com.fscon.service.HomeworkService;
import com.fscon.service.HomeworkVoiceService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.fscon.pojo.ResultBean.RESULT_ERROR;
import static com.fscon.pojo.ResultBean.RESULT_SUCCESS;

@Controller
public class HomeworkController {
    @Autowired
    private HomeworkService homeworkService;

    @Autowired
    private HomeworkImageService homeworkImageService;

    @Autowired
    private HomeworkVoiceService homeworkVoiceService;

    @RequestMapping(value="/add_homework",method=RequestMethod.POST)
    @ResponseBody
    public ResultBean<Homework> addHomework(@RequestParam(name="subjectId")Integer subjectId, @RequestParam(name="teacherId")Integer teacherId,
                                            @RequestParam(name="classId")Integer classId, @RequestParam(name="content")String content,
                                            HttpServletRequest request, HttpServletResponse response){

        ResultBean<Homework> resultBean=new ResultBean<Homework>();

        Date date=new Date();
        Homework homework=new Homework(null,subjectId,teacherId,classId,content,date);
        int code=homeworkService.addHomework(homework);
        if(code==0){
            resultBean.setCode(RESULT_ERROR);
            resultBean.setMessage("作业添加失败！");
        }
        else {
            List<HomeworkImage> homeworkImageList=new ArrayList<HomeworkImage>();
            List<HomeworkVoice> homeworkVoiceList=new ArrayList<HomeworkVoice>();
            String s = request.getSession().getServletContext().getRealPath("/")+"upload/homework";

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
            String res = sdf.format(date);
            // 创建年月文件夹
            Calendar calendar = Calendar.getInstance();
            File dateDirs = new File(calendar.get(Calendar.YEAR) + File.separator + (calendar.get(Calendar.MONTH)));

            /*for(CommonsMultipartFile imageFile:imageFiles){
                String originalFileName = imageFile.getOriginalFilename();
                // 新文件名
                String newFileName = res + originalFileName;

                // 新文件
                File newFile = new File(s + File.separator +"homeworkIimage" + File.separator + dateDirs + File.separator + newFileName);
                if( !newFile.getParentFile().exists()) {
                    // 如果目标文件所在的目录不存在，则创建父目录
                    newFile.getParentFile().mkdirs();
                }
                try {
                    imageFile.transferTo(newFile);
                }catch(IOException e){
                    resultBean.setCode(RESULT_ERROR);
                    resultBean.setMessage("作业添加失败！");
                    e.printStackTrace();
                    return resultBean;
                }
                HomeworkImage homeworkImage=new HomeworkImage(homeworkId,newFile.getAbsolutePath());
                homeworkImageList.add(homeworkImage);
            }
            if(homeworkImageService.addHomeworkImage(homeworkImageList)==0){
                resultBean.setCode(RESULT_ERROR);
                resultBean.setMessage("作业添加失败！");
                return resultBean;
            }

            for(CommonsMultipartFile voiceFile:voiceFiles){
                String originalFileName = voiceFile.getOriginalFilename();
                // 新文件名
                String newFileName = res + originalFileName;

                // 新文件
                File newFile = new File(s + File.separator +"voiceIimage" + File.separator + dateDirs + File.separator + newFileName);
                if( !newFile.getParentFile().exists()) {
                    // 如果目标文件所在的目录不存在，则创建父目录
                    newFile.getParentFile().mkdirs();
                }
                try {
                    voiceFile.transferTo(newFile);
                }catch(IOException e){
                    resultBean.setCode(RESULT_ERROR);
                    resultBean.setMessage("作业添加失败！");
                    e.printStackTrace();
                    return resultBean;
                }
                HomeworkVoice homeworkVoice=new HomeworkVoice(homeworkId,newFile.getAbsolutePath());
                homeworkVoiceList.add(homeworkVoice);
            }
            if(homeworkVoiceService.addHomeworkVoice(homeworkVoiceList)==0){
                resultBean.setCode(RESULT_ERROR);
                resultBean.setMessage("作业添加失败！");
                return resultBean;
            }
            homework.setHomeworkImageList(homeworkImageList);
            homework.setHomeworkVoiceList(homeworkVoiceList);*/
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            try {
                //获取文件集合
                List<MultipartFile> imageFiles = multipartRequest.getFiles("imageFiles");
                for(MultipartFile imageFile:imageFiles){
                    String originalFileName = imageFile.getOriginalFilename();
                    // 新文件名
                    String newFileName = res + originalFileName;
                    // 新文件
                    File newFile = new File(s + File.separator +"homeworkImage" + File.separator + dateDirs + File.separator + newFileName);
                    if( !newFile.getParentFile().exists()) {
                        // 如果目标文件所在的目录不存在，则创建父目录
                        newFile.getParentFile().mkdirs();
                    }
                    imageFile.transferTo(newFile);
                    HomeworkImage homeworkImage=new HomeworkImage(homework.getHomeworkId(),Context.path+"/homework/homeworkImage/"+calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH)) + "/"+ newFileName);
                    homeworkImageList.add(homeworkImage);
                }
            }catch(IOException e){
                resultBean.setCode(RESULT_ERROR);
                resultBean.setMessage("作业添加失败！");
                e.printStackTrace();
                return resultBean;
            }

            if(homeworkImageList.size()>0) {
                if (homeworkImageService.addHomeworkImage(homeworkImageList) == 0) {
                    resultBean.setCode(RESULT_ERROR);
                    resultBean.setMessage("作业添加失败！");
                    return resultBean;
                }
            }

            try {
                //获取文件集合
                List<MultipartFile> voiceFiles = multipartRequest.getFiles("voiceFiles");
                for(MultipartFile voiceFile:voiceFiles){
                    String originalFileName = voiceFile.getOriginalFilename();
                    // 新文件名
                    String newFileName = res + originalFileName;

                    // 新文件
                    File newFile = new File(s + File.separator +"homeworkVoice" + File.separator + dateDirs + File.separator + newFileName);
                    if( !newFile.getParentFile().exists()) {
                        // 如果目标文件所在的目录不存在，则创建父目录
                        newFile.getParentFile().mkdirs();
                    }
                    voiceFile.transferTo(newFile);
                    HomeworkVoice homeworkVoice=new HomeworkVoice(homework.getHomeworkId(),Context.path+"/homework/homeworkVoice/"+calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH)) + "/"+ newFileName);
                    homeworkVoiceList.add(homeworkVoice);
                }
            }catch(IOException e){
                resultBean.setCode(RESULT_ERROR);
                resultBean.setMessage("作业添加失败！");
                e.printStackTrace();
                return resultBean;
            }

            if(homeworkVoiceList.size()>0) {
                if (homeworkVoiceService.addHomeworkVoice(homeworkVoiceList) == 0) {
                    resultBean.setCode(RESULT_ERROR);
                    resultBean.setMessage("作业添加失败！");
                    return resultBean;
                }
            }
            homework=homeworkService.selectHomeworkByHomeworkId(homework.getHomeworkId());
            resultBean.setResult(homework);
            resultBean.setCode(RESULT_SUCCESS);
            resultBean.setMessage("作业添加成功！");
        }
        return resultBean;
    }

    @RequestMapping(value="/delete_homework",method=RequestMethod.POST)
    @ResponseBody
    public ResultBean deleteHomework(@RequestParam(name="homeworkId")Integer homeworkId){
        ResultBean resultBean=new ResultBean();

        Homework homework=homeworkService.selectHomeworkByHomeworkId(homeworkId);

        List<HomeworkImage> homeworkImageList=homework.getHomeworkImageList();
        List<HomeworkVoice> homeworkVoiceList=homework.getHomeworkVoiceList();

        if(homeworkImageList!=null) {
            String imageParentPath=homeworkImageList.get(0).getHomeworkImagePath();
            imageParentPath=imageParentPath.substring(26);
            imageParentPath="D:/IdeaProjects/server-ssm/target/"+imageParentPath;
            System.out.print(imageParentPath);
            File imageParentFile=(new File(imageParentPath)).getParentFile();
            for (HomeworkImage homeworkImage : homeworkImageList) {
                String homeworkImagePath = homeworkImage.getHomeworkImagePath();
                homeworkImagePath=homeworkImagePath.substring(26);
                homeworkImagePath="D:/IdeaProjects/server-ssm/target/"+homeworkImagePath;
                File file = new File(homeworkImagePath);
                if (!file.exists()) {
                    System.out.println("删除文件失败:" + homeworkImagePath + "不存在！");
                    continue;
                } else {
                    file.delete();
                }
                homeworkImageService.delete(homeworkImage.getHomeworkImageId());
            }
            if(imageParentFile.listFiles().length==0){
                imageParentFile.delete();
            }
        }

        /*if(homeworkVoiceList!=null) {
            String voiceParentPath=homeworkVoiceList.get(0).getHomeworkVoicePath();
            File voiceParentFile=(new File(voiceParentPath)).getParentFile();
            for (HomeworkVoice homeworkVoice : homeworkVoiceList) {
                String homeworkVoicePath = homeworkVoice.getHomeworkVoicePath();
                File file = new File(homeworkVoicePath);
                if (!file.exists()) {
                    System.out.println("删除文件失败:" + homeworkVoicePath + "不存在！");
                    continue;
                } else {
                    file.delete();
                }
                homeworkImageService.delete(homeworkVoice.getHomeworkVoiceId());
            }
            if(voiceParentFile.listFiles().length==0){
                voiceParentFile.delete();
            }
        }*/

        int code=homeworkService.delete(homeworkId);
        if(code==0){
            resultBean.setCode(RESULT_ERROR);
            resultBean.setMessage("作业删除失败！");
            return resultBean;
        }else{
            resultBean.setCode(RESULT_SUCCESS);
            resultBean.setMessage("作业删除成功！");
            return resultBean;
        }
    }

    @RequestMapping(value="/select_homework",method=RequestMethod.POST)
    @ResponseBody
    public ResultBean<List<Homework>> selectHomework(@RequestParam(name="classId")Integer classId,@RequestParam(name="offset")int offset){
        ResultBean<List<Homework>> resultBean=new ResultBean<List<Homework>>();
        RowBounds rowBounds=new RowBounds(offset,offset+10);
        List<Homework> homeworkList=homeworkService.selectHomeworkByClassId(classId,rowBounds);
        resultBean.setResult(homeworkList);
        resultBean.setCode(RESULT_SUCCESS);
        return resultBean;
    }

    @RequestMapping(value="/select_homework_by_teacher",method=RequestMethod.POST)
    @ResponseBody
    public ResultBean<List<Homework>> selectNoticeByTeacher(@RequestParam(name="teacherId")Integer teacherId,@RequestParam(name="offset")int offset){
        ResultBean<List<Homework>> resultBean=new ResultBean<List<Homework>>();
        RowBounds rowBounds=new RowBounds(offset,offset+10);
        List<Homework> homeworkList=homeworkService.selectHomeworkByTeacherId(teacherId,rowBounds);

        resultBean.setResult(homeworkList);
        resultBean.setCode(RESULT_SUCCESS);
        return resultBean;
    }

    @RequestMapping(value="/update_homework",method=RequestMethod.POST)
    @ResponseBody
    public ResultBean<Homework> updateNotice(@RequestParam(name="homeworkId")Integer homeworkId,@RequestParam(name="subjectId")Integer subjectId, @RequestParam(name="teacherId")Integer teacherId,
                                             @RequestParam(name="classId")Integer classId, @RequestParam(name="content")String content,
                                           HttpServletRequest request, HttpServletResponse response){
        ResultBean<Homework> resultBean=new ResultBean<Homework>();

        if(deleteHomework(homeworkId).getCode()!=RESULT_SUCCESS){
            resultBean.setCode(RESULT_ERROR);
            resultBean.setMessage("作业修改失败");
            resultBean.setResult(null);
            return  resultBean;
        }

        Date date=new Date();
        Homework homework=new Homework(null,subjectId,teacherId,classId,content,date);
        int code=homeworkService.addHomework(homework);
        if(code==0){
            resultBean.setCode(RESULT_ERROR);
            resultBean.setMessage("通知修改失败！");
        }
        else {
            List<HomeworkImage> homeworkImageList=new ArrayList<HomeworkImage>();
            List<HomeworkVoice> homeworkVoiceList=new ArrayList<HomeworkVoice>();
            String s = request.getSession().getServletContext().getRealPath("/")+"upload/homework";
            System.out.print(s);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
            String res = sdf.format(date);
            // 创建年月文件夹
            Calendar calendar = Calendar.getInstance();
            File dateDirs = new File(calendar.get(Calendar.YEAR) + File.separator + (calendar.get(Calendar.MONTH)));
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            try {
                //获取文件集合
                List<MultipartFile> imageFiles = multipartRequest.getFiles("imageFiles");
                for(MultipartFile imageFile:imageFiles){
                    String originalFileName = imageFile.getOriginalFilename();
                    // 新文件名
                    String newFileName = res + originalFileName;
                    // 新文件
                    File newFile = new File(s + File.separator +"homeworkImage" + File.separator + dateDirs + File.separator + newFileName);
                    if( !newFile.getParentFile().exists()) {
                        // 如果目标文件所在的目录不存在，则创建父目录
                        newFile.getParentFile().mkdirs();
                    }
                    imageFile.transferTo(newFile);
                    HomeworkImage homeworkImage=new HomeworkImage(homework.getHomeworkId(),Context.path+"/homework/homeworkImage/"+calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH)) + "/"+ newFileName);
                    homeworkImageList.add(homeworkImage);
                }
            }catch(IOException e){
                resultBean.setCode(RESULT_ERROR);
                resultBean.setMessage("作业修改失败！");
                e.printStackTrace();
                return resultBean;
            }

            if(homeworkImageList.size()>0) {
                if (homeworkImageService.addHomeworkImage(homeworkImageList) == 0) {
                    resultBean.setCode(RESULT_ERROR);
                    resultBean.setMessage("作业修改失败！");
                    return resultBean;
                }
            }

            try {
                //获取文件集合
                List<MultipartFile> voiceFiles = multipartRequest.getFiles("voiceFiles");
                for(MultipartFile voiceFile:voiceFiles){
                    String originalFileName = voiceFile.getOriginalFilename();
                    // 新文件名
                    String newFileName = res + originalFileName;

                    // 新文件
                    File newFile = new File(s + File.separator +"homeworkVoice" + File.separator + dateDirs + File.separator + newFileName);
                    if( !newFile.getParentFile().exists()) {
                        // 如果目标文件所在的目录不存在，则创建父目录
                        newFile.getParentFile().mkdirs();
                    }
                    voiceFile.transferTo(newFile);
                    HomeworkVoice homeworkVoice=new HomeworkVoice(homework.getHomeworkId(),"http://192.168.0.102:8080/server-ssm/upload/homework/homeworkVoice/"+calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH)) + "/"+ newFileName);
                    homeworkVoiceList.add(homeworkVoice);
                }
            }catch(IOException e){
                resultBean.setCode(RESULT_ERROR);
                resultBean.setMessage("作业修改失败！");
                e.printStackTrace();
                return resultBean;
            }

            if(homeworkVoiceList.size()>0) {
                if (homeworkVoiceService.addHomeworkVoice(homeworkVoiceList) == 0) {
                    resultBean.setCode(RESULT_ERROR);
                    resultBean.setMessage("作业修改失败！");
                    return resultBean;
                }
            }
            homework=homeworkService.selectHomeworkByHomeworkId(homework.getHomeworkId());
            resultBean.setResult(homework);
            resultBean.setCode(RESULT_SUCCESS);
            resultBean.setMessage("作业修改成功！");
        }

        return resultBean;
    }
}
