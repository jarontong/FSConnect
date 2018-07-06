package com.fscon.service;

import com.fscon.pojo.MomentImage;

import java.util.List;

public interface MomentImageService {

    int addMomentImage(List<MomentImage> momentImageList);

    List<MomentImage> selectMomentImageByMomentId(Integer momentId);

    MomentImage selectMomentByMomentImageId(Integer momentImageId);

    int update(MomentImage momentImage);

    int delete(Integer momentImageId);

    int updateBatch(List<MomentImage> momentImageList);

    int deleteBatch(List<MomentImage> momentImageList);
}
