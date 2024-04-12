package com.ykj.graduation_design.module.file.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import com.ykj.graduation_design.common.RestResult;
import com.ykj.graduation_design.common.utils.FileUploadUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Ayelsh
 * @Date: 2024/03/29/16:29
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/File")
public class FileController {

    private final ResourceLoader resourceLoader;

    @Autowired
    public FileController(@Qualifier("webApplicationContext") ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @PostMapping("uploadAvatar")
    public void uploadFile(HttpServletResponse response, @RequestParam("file") MultipartFile file) {
        try {
            String filePath = file.getOriginalFilename();
            String fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
            String extension = "";

            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
                extension = fileName.substring(dotIndex + 1);
                fileName = IdUtil.getSnowflakeNextIdStr() + "_" + "avatar." + extension;//重新生成文件名（根据具体情况生成对应文件名）
            } else fileName = IdUtil.getSnowflakeNextIdStr() + "_" + "avatar";//重新生成文件名（根据具体情况生成对应文件名）


            //获取jar包所在目录
            ApplicationHome applicationHome = new ApplicationHome(getClass());
            File homePath = applicationHome.getSource();
            //在jar包所在目录下生成一个upload文件夹用来存储上传的图片
            String dirPath = homePath.getParentFile().toString() + "\\upload\\";
            log.info(dirPath);


            FileUploadUtil.uploadToServer(file, dirPath, fileName);
            RestResult.responseJson(response, new RestResult<>(200, "上传成功！", fileName));
        } catch (Exception e) {
            RestResult.responseJson(response, new RestResult<>(600, "上传失败！", e.getMessage()));
        }

    }

    @GetMapping("/{filename:.+}")
    public void getFile(@PathVariable String filename, HttpServletResponse response) {
        try{
            ApplicationHome applicationHome = new ApplicationHome(getClass());
            String dirPath = applicationHome.getSource().getParentFile().toString() + "\\upload\\";
            log.info("文件名：{}", dirPath + filename);
            Resource file = resourceLoader.getResource("file:" + dirPath + filename);
            log.info("文件上传：{}", file.getURI());
            RestResult.responseBlob(response, file);
        }catch (Exception e){
            log.info("文件异常：{}",e.getMessage());
            RestResult.responseJson(response, new RestResult<>(600, "读取失败！"));
        }


    }

}
