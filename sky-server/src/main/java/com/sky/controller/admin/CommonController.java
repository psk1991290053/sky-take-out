package com.sky.controller.admin;


import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Slf4j
public class CommonController {

    @Autowired
    AliOssUtil aliOssUtil;

    //文件上传
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
        log.info("文件上传 :{}",file);

        //提取原文件拓展名
        String originalFileName = file.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        //创建新文件名
        String objectName = UUID.randomUUID().toString() + extension;
        //上传阿里云oss
        String filePath = aliOssUtil.upload(file.getBytes(),objectName);
        //返回阿里云文件路径
        return Result.success(filePath);
    }



}
