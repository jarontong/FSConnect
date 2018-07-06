package com.fscon.controller;

import com.fscon.pojo.Context;
import com.fscon.pojo.Moment;
import com.fscon.pojo.MomentImage;
import com.fscon.pojo.ResultBean;
import com.fscon.service.MomentImageService;
import com.fscon.service.MomentService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.fscon.pojo.ResultBean.RESULT_ERROR;
import static com.fscon.pojo.ResultBean.RESULT_SUCCESS;

@Controller
public class MomentController {
    @Autowired
    private MomentService momentService;

    @Autowired
    private MomentImageService momentImageService;

    @RequestMapping(value="/add_moment",method=RequestMethod.POST)
    @ResponseBody
    public ResultBean<Moment> addMoment( @RequestParam(name="teacherId",required = false)Integer teacherId,@RequestParam(name="parentId",required = false)Integer parentId,
                                         @RequestParam(name="content")String content, HttpServletRequest request, HttpServletResponse response) {

        ResultBean<Moment> resultBean = new ResultBean<Moment>();
        Date date = new Date();
        Moment moment = new Moment(null, teacherId, parentId, content, date);
        int code = momentService.addMoment(moment);
        if (code == 0) {
            resultBean.setCode(RESULT_ERROR);
            resultBean.setMessage("动态添加失败！");
        } else {
            List<MomentImage> momentImageList = new ArrayList<MomentImage>();
            String s = request.getSession().getServletContext().getRealPath("/") + "upload/moment";
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
                for (MultipartFile imageFile : imageFiles) {
                    String originalFileName = imageFile.getOriginalFilename();
                    // 新文件名
                    String newFileName = res + originalFileName;
                    // 新文件
                    File newFile = new File(s + File.separator + "momentImage" + File.separator + dateDirs + File.separator + newFileName);
                    if (!newFile.getParentFile().exists()) {
                        // 如果目标文件所在的目录不存在，则创建父目录
                        newFile.getParentFile().mkdirs();
                    }
                    imageFile.transferTo(newFile);
                    MomentImage momentImage = new MomentImage(moment.getMomentsId(), Context.path + "/moment/momentImage/" + calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH)) + "/" + newFileName);
                    momentImageList.add(momentImage);
                }
            } catch (IOException e) {
                resultBean.setCode(RESULT_ERROR);
                resultBean.setMessage("动态添加失败！");
                e.printStackTrace();
                return resultBean;
            }

            if (momentImageList.size() > 0) {
                if (momentImageService.addMomentImage(momentImageList) == 0) {
                    resultBean.setCode(RESULT_ERROR);
                    resultBean.setMessage("动态添加失败！");
                    return resultBean;
                }
            }
            moment = momentService.selectMomentByMomentId(moment.getMomentsId());
            resultBean.setResult(moment);
            resultBean.setCode(RESULT_SUCCESS);
            resultBean.setMessage("动态添加成功！");
        }
        return resultBean;
    }


    @RequestMapping(value="/select_moment",method=RequestMethod.POST)
    @ResponseBody
    public ResultBean<List<Moment>> selectNotice(@RequestParam(name="userId1",required = false) Integer userId1,
                                                 @RequestParam(name="userId2",required = false) Integer userId2,
                                                 @RequestParam(name="userId3",required = false) Integer userId3,
                                                 @RequestParam(name="userId4",required = false) Integer userId4,
                                                 @RequestParam(name="userId5",required = false) Integer userId5,
                                                 @RequestParam(name="userId6",required = false) Integer userId6,
                                                 @RequestParam(name="userId7",required = false) Integer userId7,
                                                 @RequestParam(name="userId8",required = false) Integer userId8,
                                                 @RequestParam(name="userId9",required = false) Integer userId9,
                                                 @RequestParam(name="userId10",required = false) Integer userId10,
                                                 @RequestParam(name="offset")int offset){
        ResultBean<List<Moment>> resultBean=new ResultBean<List<Moment>>();
        RowBounds rowBounds=new RowBounds(offset,offset+10);
        List<Integer>userIdList=new ArrayList<Integer>();
        if(userId1!=null)
            userIdList.add(userId1);
        if(userId2!=null)
            userIdList.add(userId2);
        if(userId3!=null)
            userIdList.add(userId3);
        if(userId4!=null)
            userIdList.add(userId4);
        if(userId5!=null)
            userIdList.add(userId5);
        if(userId6!=null)
            userIdList.add(userId6);
        if(userId7!=null)
            userIdList.add(userId7);
        if(userId8!=null)
            userIdList.add(userId8);
        if(userId9!=null)
            userIdList.add(userId9);
        if(userId10!=null)
            userIdList.add(userId10);
        List<Moment> momentList=momentService.selectMomentByUserId(userIdList,rowBounds);
        resultBean.setResult(momentList);
        resultBean.setCode(RESULT_SUCCESS);
        return resultBean;
    }

    @RequestMapping(value="/select_moment_s",method=RequestMethod.POST)
    @ResponseBody
    public ResultBean<List<Moment>> selectNoticeS(@RequestParam(name = "userId") Integer userId, @RequestParam(name="offset")int offset){
        ResultBean<List<Moment>> resultBean=new ResultBean<List<Moment>>();
        RowBounds rowBounds=new RowBounds(offset,offset+10);
        List<Moment> momentList=momentService.selectMomentByUser(userId,rowBounds);
        resultBean.setResult(momentList);
        resultBean.setCode(RESULT_SUCCESS);
        return resultBean;
    }
}
