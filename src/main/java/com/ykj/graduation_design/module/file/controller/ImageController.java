package com.ykj.graduation_design.module.file.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Ayelsh
 * @Date: 2024/03/29/21:32
 * @Description:
 */

@RestController
public class ImageController {




//    @Value("file:D:/Gre/toB/graduation_design/target/upload/") // 配置文件上传路径
//    private Resource uploadDirectory;
//
//    @GetMapping("/api/getImage/{imageName}")
//    public String getImage(@PathVariable String imageName) throws IOException {
//        return MvcUriComponentsBuilder.fromMethodName(ImageController.class,
//                "serveFile", imageName).build().toString();
//    }
//
//    @GetMapping("/api/image/{imageName:.+}")
//    public Resource serveFile(@PathVariable String imageName) {
//        return uploadDirectory.resolve(imageName);
//    }
}

