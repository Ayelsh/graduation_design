package com.ykj.graduation_design.module.file.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ykj.graduation_design.common.RestResult;
import com.ykj.graduation_design.common.utils.FileUploadUtil;
import com.ykj.graduation_design.common.utils.MinioUtil;
import com.ykj.graduation_design.module.Blog.entity.Article;
import com.ykj.graduation_design.module.file.entity.Resources;
import com.ykj.graduation_design.module.file.services.ResourcesService;
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
import java.util.Date;

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

    private final MinioUtil minioUtil;

    private final ResourceLoader resourceLoader;


    @Autowired
    private ResourcesService resourcesService;

    @Autowired
    public FileController(@Qualifier("webApplicationContext") ResourceLoader resourceLoader,@Autowired MinioUtil minioUtil) {
        this.resourceLoader = resourceLoader;
        this.minioUtil = minioUtil;
    }

    @PostMapping("uploadAvatar")
    public void uploadAvatar(HttpServletResponse response, @RequestParam("file") MultipartFile file) {
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

    @PostMapping("uploadAvatarMinio")
    public void uploadAvatarMinio(HttpServletResponse response, @RequestParam("file") MultipartFile file) {
        try {

            RestResult.responseJson(response, new RestResult<>(200, "上传成功！",  minioUtil.uploadOne(file)));
        } catch (Exception e) {
            RestResult.responseJson(response, new RestResult<>(600, "上传失败！", e.getMessage()));
        }

    }

    @PostMapping("uploadFile")
    public void uploadFiles(HttpServletResponse response, @RequestParam("file") MultipartFile file) {
        try {


            RestResult.responseJson(response, new RestResult<>(200, "上传成功！",  minioUtil.uploadOne(file)));
        } catch (Exception e) {
            RestResult.responseJson(response, new RestResult<>(600, "上传失败！", e.getMessage()));
        }

    }

    @PostMapping("submit")
    public void submitFiles(HttpServletResponse response, @RequestBody Resources resources) {
        try {
            resources.setCreatedTime(new Date());
            resources.setUpdatedTime(new Date());
            RestResult.responseJson(response, new RestResult<>(200, "上传成功！",  resourcesService.save(resources)));
        } catch (Exception e) {
            RestResult.responseJson(response, new RestResult<>(600, "上传失败！", e.getMessage()));
        }

    }


    @GetMapping("/{filename:.+}")
    public void getAvatar(@PathVariable String filename, HttpServletResponse response) {
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

    @PostMapping
    public void newResource(HttpServletResponse response, @RequestParam("file") MultipartFile file,@RequestParam("description ")String description) {
        try {
            String filePath = file.getOriginalFilename();
            String fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
            String extension = "";

            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
                extension = fileName.substring(dotIndex + 1);
                fileName = IdUtil.getSnowflakeNextIdStr() + "_" + "resource." + extension;//重新生成文件名（根据具体情况生成对应文件名）
            } else {
                fileName = IdUtil.getSnowflakeNextIdStr() + "_" + "resource";//重新生成文件名（根据具体情况生成对应文件名）
            }

            //获取jar包所在目录
            ApplicationHome applicationHome = new ApplicationHome(getClass());
            File homePath = applicationHome.getSource();
            //在jar包所在目录下生成一个upload文件夹用来存储上传的图片
            String dirPath = homePath.getParentFile().toString() + "\\resource\\";
            log.info(dirPath);

            FileUploadUtil.uploadToServer(file, dirPath, fileName);
            resourcesService.save(new Resources(fileName,description));
            RestResult.responseJson(response, new RestResult<>(200, "上传成功！", fileName));
        } catch (Exception e) {
            RestResult.responseJson(response, new RestResult<>(600, "上传失败！", e.getMessage()));
        }
    }



    @GetMapping("list")
    public void listFile(HttpServletResponse response,Integer pageNumber, Integer pageSize){
        try {
            Page<Resources> page = new Page<>(pageNumber,pageSize);
            page = resourcesService.page(page);
            RestResult.responseJson(response, new RestResult<>(200, "成功！",page ));
        }catch (Exception e){
            RestResult.responseJson(response, new RestResult<>(600, "失败！", e.getMessage()));
        }
    }



    @GetMapping("/resources/{id}")
    public void getFileUrl(HttpServletResponse response,@PathVariable("id") Long id){
        try{
            RestResult.responseJson(response, new RestResult<>(200, "获取Url成功",minioUtil.downloadUrl(resourcesService.getById(id).getFilename())));
        }catch (Exception e){
            log.info("文件异常：{}",e.getMessage());
            RestResult.responseJson(response, new RestResult<>(600, "获取文件失败",e.getMessage()));
        }
    }

    @DeleteMapping("/resources/{id}")
    public void deleteResource(HttpServletResponse response,@PathVariable("id") Long id){
        try{
            RestResult.responseJson(response, new RestResult<>(200, "获取Url成功",resourcesService.removeById(id)));
        }catch (Exception e){
            log.info("文件异常：{}",e.getMessage());
            RestResult.responseJson(response, new RestResult<>(600, "获取文件失败",e.getMessage()));
        }
    }


}
