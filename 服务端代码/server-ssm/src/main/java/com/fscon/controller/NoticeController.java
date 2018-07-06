package com.fscon.controller;

import com.fscon.pojo.*;
import com.fscon.service.NoticeImageService;
import com.fscon.service.NoticeService;
import com.fscon.service.NoticeVoiceService;
import org.apache.commons.io.FileUtils;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.fscon.pojo.ResultBean.RESULT_ERROR;
import static com.fscon.pojo.ResultBean.RESULT_SUCCESS;

@Controller
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    @Autowired
    private NoticeImageService noticeImageService;

    @Autowired
    private NoticeVoiceService noticeVoiceService;

   /* @RequestMapping(value="/add_notice",method=RequestMethod.POST)
    @ResponseBody
    public ResultBean<Notice> addNotice(@RequestParam(name="title")String title, @RequestParam(name="content")String content,
                                        @RequestParam(name="teacherId")Integer teacherId, @RequestParam(name="classId")Integer classId,
                                        @RequestParam(name="imageFiles")CommonsMultipartFile[] imageFiles,
                                        @RequestParam(name="voiceFiles")CommonsMultipartFile[] voiceFiles){

        ResultBean<Notice> resultBean=new ResultBean<Notice>();

        //Date creaTime =
        Notice notice=new Notice(null,title,content,teacherId,classId,null);
        int noticeId=noticeService.addNotice(notice);
        System.out.print("code:"+noticeId+"\n");
        if(noticeId==0){
            resultBean.setCode(RESULT_ERROR);
            resultBean.setMessage("通知添加失败！");
        }
        else {
            List<NoticeImage> noticeImageList=new ArrayList<NoticeImage>();
            List<NoticeVoice> noticeVoiceList=new ArrayList<NoticeVoice>();

            String s = "D:/IdeaProjects/server-ssm/upload/notice";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
            String res = sdf.format(new Date());
            // 创建年月文件夹
            Calendar date = Calendar.getInstance();
            File dateDirs = new File(date.get(Calendar.YEAR) + File.separator + (date.get(Calendar.MONTH)));

            for(CommonsMultipartFile imageFile:imageFiles){
                String originalFileName = imageFile.getOriginalFilename();
                // 新文件名
                String newFileName = res + originalFileName;

                // 新文件
                File newFile = new File(s + File.separator +"noticeIimage" + File.separator + dateDirs + File.separator + newFileName);
                if( !newFile.getParentFile().exists()) {
                    // 如果目标文件所在的目录不存在，则创建父目录
                    newFile.getParentFile().mkdirs();
                }
                try {
                    imageFile.transferTo(newFile);
                }catch(IOException e){
                    resultBean.setCode(RESULT_ERROR);
                    resultBean.setMessage("通知添加失败！");
                    e.printStackTrace();
                    return resultBean;
                }
                NoticeImage noticeImage=new NoticeImage(noticeId,newFile.getAbsolutePath());
                noticeImageList.add(noticeImage);
            }
            if(noticeImageService.addNoticeImage(noticeImageList)==0){
                resultBean.setCode(RESULT_ERROR);
                resultBean.setMessage("通知添加失败！");
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
                    resultBean.setMessage("通知添加失败！");
                    e.printStackTrace();
                    return resultBean;
                }
                NoticeVoice noticeVoice=new NoticeVoice(noticeId,newFile.getAbsolutePath());
                noticeVoiceList.add(noticeVoice);
            }
            if(noticeVoiceService.addNoticeVoice(noticeVoiceList)==0){
                resultBean.setCode(RESULT_ERROR);
                resultBean.setMessage("通知添加失败！");
                return resultBean;
            }
            notice.setNoticeImageList(noticeImageList);
            notice.setNoticeVoiceList(noticeVoiceList);
            resultBean.setResult(notice);
            resultBean.setCode(RESULT_SUCCESS);
            resultBean.setMessage("通知添加成功！");
        }

        return resultBean;
    }*/

    @RequestMapping(value="/add_notice",method=RequestMethod.POST)
    @ResponseBody
    public ResultBean<Notice> addNotice(@RequestParam(name="title")String title, @RequestParam(name="content")String content,
                                        @RequestParam(name="teacherId")Integer teacherId, @RequestParam(name="classId")Integer classId,
                                        HttpServletRequest request, HttpServletResponse response){

        ResultBean<Notice> resultBean=new ResultBean<Notice>();
        Date date=new Date();
        Notice notice=new Notice(null,title,content,teacherId,classId,date);
        int code=noticeService.addNotice(notice);
        if(code==0){
            resultBean.setCode(RESULT_ERROR);
            resultBean.setMessage("通知添加失败！");
        }
        else {
            List<NoticeImage> noticeImageList=new ArrayList<NoticeImage>();
            List<NoticeVoice> noticeVoiceList=new ArrayList<NoticeVoice>();
            String s = request.getSession().getServletContext().getRealPath("/")+"upload/notice";
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
                    File newFile = new File(s + File.separator +"noticeImage" + File.separator + dateDirs + File.separator + newFileName);
                    if( !newFile.getParentFile().exists()) {
                        // 如果目标文件所在的目录不存在，则创建父目录
                        newFile.getParentFile().mkdirs();
                    }
                    imageFile.transferTo(newFile);
                    NoticeImage noticeImage=new NoticeImage(notice.getNoticeId(),Context.path+"/notice/noticeImage/"+calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH)) + "/"+ newFileName);
                    noticeImageList.add(noticeImage);
                }
            }catch(IOException e){
                resultBean.setCode(RESULT_ERROR);
                resultBean.setMessage("通知添加失败！");
                e.printStackTrace();
                return resultBean;
            }

            if(noticeImageList.size()>0) {
                if (noticeImageService.addNoticeImage(noticeImageList) == 0) {
                    resultBean.setCode(RESULT_ERROR);
                    resultBean.setMessage("通知添加失败！");
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
                    File newFile = new File(s + File.separator +"noticeVoice" + File.separator + dateDirs + File.separator + newFileName);
                    if( !newFile.getParentFile().exists()) {
                        // 如果目标文件所在的目录不存在，则创建父目录
                        newFile.getParentFile().mkdirs();
                    }
                    voiceFile.transferTo(newFile);
                    NoticeVoice noticeVoice=new NoticeVoice(notice.getNoticeId(),newFile.getAbsolutePath());
                    noticeVoiceList.add(noticeVoice);
                }
            }catch(IOException e){
                resultBean.setCode(RESULT_ERROR);
                resultBean.setMessage("通知添加失败！");
                e.printStackTrace();
                return resultBean;
            }

            if(noticeVoiceList.size()>0) {
                if (noticeVoiceService.addNoticeVoice(noticeVoiceList) == 0) {
                    resultBean.setCode(RESULT_ERROR);
                    resultBean.setMessage("通知添加失败！");
                    return resultBean;
                }
            }
            notice=noticeService.selectNoticeByNoticeId(notice.getNoticeId());
            resultBean.setResult(notice);
            resultBean.setCode(RESULT_SUCCESS);
            resultBean.setMessage("通知添加成功！");
        }
        return resultBean;
    }

    @RequestMapping(value="/delete_notice",method=RequestMethod.POST)
    @ResponseBody
    public ResultBean deleteNotice(@RequestParam(name="noticeId")Integer noticeId){
        ResultBean resultBean=new ResultBean();

        Notice notice=noticeService.selectNoticeByNoticeId(noticeId);

        List<NoticeImage> noticeImageList=notice.getNoticeImageList();
        List<NoticeVoice> noticeVoiceList=notice.getNoticeVoiceList();

        if(noticeImageList!=null) {
            String imageParentPath=noticeImageList.get(0).getNoticeImagePath();
            imageParentPath=imageParentPath.substring(26);
            imageParentPath="D:/IdeaProjects/server-ssm/target/"+imageParentPath;
            System.out.print(imageParentPath);
            File imageParentFile=(new File(imageParentPath)).getParentFile();
            for (NoticeImage noticeImage : noticeImageList) {
                String noticeImagePath = noticeImage.getNoticeImagePath();
                noticeImagePath=noticeImagePath.substring(26);
                noticeImagePath="D:/IdeaProjects/server-ssm/target/"+noticeImagePath;
                File file = new File(noticeImagePath);
                if (!file.exists()) {
                    System.out.println("删除文件失败:" + noticeImagePath + "不存在！");
                    continue;
                } else {
                    file.delete();
                }
                noticeImageService.delete(noticeImage.getNoticeImageId());
            }
            if(imageParentFile.listFiles().length==0){
                imageParentFile.delete();
            }
        }

        /*if(noticeVoiceList!=null) {
            String voiceParentPath=noticeVoiceList.get(0).getNoticeVoicePath();
            File voiceParentFile=(new File(voiceParentPath)).getParentFile();
            for (NoticeVoice noticeVoice : noticeVoiceList) {
                String noticeVoicePath = noticeVoice.getNoticeVoicePath();
                File file = new File(noticeVoicePath);
                if (!file.exists()) {
                    System.out.println("删除文件失败:" + noticeVoicePath + "不存在！");
                    continue;
                } else {
                    file.delete();
                }
                noticeImageService.delete(noticeVoice.getNoticeVoiceId());
            }
            if(voiceParentFile.listFiles().length==0){
                voiceParentFile.delete();
            }
        }*/

        int code=noticeService.delete(noticeId);
        if(code==0){
            resultBean.setCode(RESULT_ERROR);
            resultBean.setMessage("通知删除失败！");
            return resultBean;
        }else{
            resultBean.setCode(RESULT_SUCCESS);
            resultBean.setMessage("通知删除成功！");
            return resultBean;
        }
    }

    @RequestMapping(value="/select_notice",method=RequestMethod.POST)
    @ResponseBody
    public ResultBean<List<Notice>> selectNotice(@RequestParam(name="classId")Integer classId,@RequestParam(name="offset")int offset){
        ResultBean<List<Notice>> resultBean=new ResultBean<List<Notice>>();
        RowBounds rowBounds=new RowBounds(offset,offset+10);
        List<Notice> noticeList=noticeService.selectNoticeByClassId(classId,rowBounds);
        resultBean.setResult(noticeList);
        resultBean.setCode(RESULT_SUCCESS);
        return resultBean;
    }

    @RequestMapping(value="/select_notice_by_teacher",method=RequestMethod.POST)
    @ResponseBody
    public ResultBean<List<Notice>> selectNoticeByTeacher(@RequestParam(name="teacherId")Integer teacherId,@RequestParam(name="offset")int offset){
        ResultBean<List<Notice>> resultBean=new ResultBean<List<Notice>>();
        RowBounds rowBounds=new RowBounds(offset,offset+10);
        List<Notice> noticeList=noticeService.selectNoticeByTeacherId(teacherId,rowBounds);

        resultBean.setResult(noticeList);
        resultBean.setCode(RESULT_SUCCESS);
        return resultBean;
    }

    @RequestMapping(value="/update_notice",method=RequestMethod.POST)
    @ResponseBody
    public ResultBean<Notice> updateNotice(@RequestParam(name="noticeId")Integer noticeId,@RequestParam(name="title")String title, @RequestParam(name="content")String content,
                                        @RequestParam(name="teacherId")Integer teacherId, @RequestParam(name="classId")Integer classId,
                                        HttpServletRequest request, HttpServletResponse response){
        ResultBean<Notice> resultBean=new ResultBean<Notice>();

        if(deleteNotice(noticeId).getCode()!=RESULT_SUCCESS){
            resultBean.setCode(RESULT_ERROR);
            resultBean.setMessage("通知修改失败");
            resultBean.setResult(null);
            return  resultBean;
        }

        Date date=new Date();
        Notice notice=new Notice(null,title,content,teacherId,classId,date);
        int code=noticeService.addNotice(notice);
        if(code==0){
            resultBean.setCode(RESULT_ERROR);
            resultBean.setMessage("通知修改失败！");
        }
        else {
            List<NoticeImage> noticeImageList=new ArrayList<NoticeImage>();
            List<NoticeVoice> noticeVoiceList=new ArrayList<NoticeVoice>();
            String s = request.getSession().getServletContext().getRealPath("/")+"upload/notice";
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
                    File newFile = new File(s + File.separator +"noticeImage" + File.separator + dateDirs + File.separator + newFileName);
                    if( !newFile.getParentFile().exists()) {
                        // 如果目标文件所在的目录不存在，则创建父目录
                        newFile.getParentFile().mkdirs();
                    }
                    imageFile.transferTo(newFile);
                    NoticeImage noticeImage=new NoticeImage(notice.getNoticeId(),Context.path+"/notice/noticeImage/"+calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH)) + "/"+ newFileName);
                    noticeImageList.add(noticeImage);
                }
            }catch(IOException e){
                resultBean.setCode(RESULT_ERROR);
                resultBean.setMessage("通知修改失败！");
                e.printStackTrace();
                return resultBean;
            }

            if(noticeImageList.size()>0) {
                if (noticeImageService.addNoticeImage(noticeImageList) == 0) {
                    resultBean.setCode(RESULT_ERROR);
                    resultBean.setMessage("通知修改失败！");
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
                    File newFile = new File(s + File.separator +"noticeVoice" + File.separator + dateDirs + File.separator + newFileName);
                    if( !newFile.getParentFile().exists()) {
                        // 如果目标文件所在的目录不存在，则创建父目录
                        newFile.getParentFile().mkdirs();
                    }
                    voiceFile.transferTo(newFile);
                    NoticeVoice noticeVoice=new NoticeVoice(notice.getNoticeId(),newFile.getAbsolutePath());
                    noticeVoiceList.add(noticeVoice);
                }
            }catch(IOException e){
                resultBean.setCode(RESULT_ERROR);
                resultBean.setMessage("通知修改失败！");
                e.printStackTrace();
                return resultBean;
            }

            if(noticeVoiceList.size()>0) {
                if (noticeVoiceService.addNoticeVoice(noticeVoiceList) == 0) {
                    resultBean.setCode(RESULT_ERROR);
                    resultBean.setMessage("通知修改失败！");
                    return resultBean;
                }
            }
            notice=noticeService.selectNoticeByNoticeId(notice.getNoticeId());
            resultBean.setResult(notice);
            resultBean.setCode(RESULT_SUCCESS);
            resultBean.setMessage("通知修改成功！");
        }

        return resultBean;
    }

}
