package com.zzb.AutomaArticle.controller;

import com.obs.services.model.PutObjectResult;
import com.zzb.AutomaArticle.utils.HwYunUtils;
import com.zzb.AutomaArticle.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("upload")
public class UploadController {

    @Autowired
    private HwYunUtils hwYunUtils;

    @PostMapping
    public Result upload(@RequestParam("image") MultipartFile file){
//        原始文件名称 xxx.png
        String originalFilename = file.getOriginalFilename();
//        唯一的文件名称
        String fileName = UUID.randomUUID().toString() + "." + StringUtils.substringAfterLast(originalFilename, '.');
//       上传文件
        PutObjectResult putObjectResult = hwYunUtils.uploadFile(file, fileName);
        return Result.success(putObjectResult);
    }
}
