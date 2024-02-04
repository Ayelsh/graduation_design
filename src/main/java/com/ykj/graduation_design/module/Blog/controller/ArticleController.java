package com.ykj.graduation_design.module.Blog.controller;

import com.ykj.graduation_design.common.RestResult;
import com.ykj.graduation_design.module.Blog.entity.ArticleContent;
import com.ykj.graduation_design.module.Blog.services.ArticleContentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Ayelsh
 * @Date: 2024/01/29/13:20
 * @Description:
 */
@RestController
@RequestMapping("/Article")
@Slf4j
public class ArticleController {
    private final ArticleContentService articleContentService;

    @Autowired
    public ArticleController(ArticleContentService articleContentService) {
        this.articleContentService = articleContentService;
    }

    @GetMapping("/initPage")
    public List<ArticleContent> initPage(){
        return articleContentService.allContentQuery();
    }

    @PostMapping
    public void addArticle(@RequestBody ArticleContent articleContent, HttpServletRequest request, HttpServletResponse response){
        try{
            log.info(articleContent.toString());
            articleContentService.addContent(articleContent);
        }catch (Exception e){
            log.error(e.getMessage());
            RestResult.responseJson(response, new RestResult<>(500, "插入失败", e.getMessage()));
        }



    }
}
