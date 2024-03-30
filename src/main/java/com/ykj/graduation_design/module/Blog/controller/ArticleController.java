package com.ykj.graduation_design.module.Blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ykj.graduation_design.common.RestResult;
import com.ykj.graduation_design.module.Blog.Dto.ArticleDto;
import com.ykj.graduation_design.module.Blog.entity.Article;
import com.ykj.graduation_design.module.Blog.entity.ArticleContent;
import com.ykj.graduation_design.module.Blog.services.ArticleContentService;
import com.ykj.graduation_design.module.Blog.services.ArticleService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    private ArticleService articleService;

    @Autowired
    public ArticleController(ArticleContentService articleContentService) {
        this.articleContentService = articleContentService;
    }

    @GetMapping("/initPage")
    public void initPage(HttpServletResponse response) {


        RestResult.responseJson(response, new RestResult<>(200, "成功！", articleService.list()));

    }

    @GetMapping("/initPostPage/{acticleId}")
    public void initPostPage(HttpServletResponse response, @PathVariable("acticleId") Long acticleId) {

        ArticleContent articleContent = articleContentService.queryByArticleId(acticleId);

        if (Objects.isNull(articleContent)) {
            RestResult.responseJson(response, new RestResult<>(600, "请求数据为空！", null));
        } else {
            ArticleDto articleDto = new ArticleDto(articleService.getById(acticleId), articleContent.getArticleContent());
            articleDto.setArticleContent(articleContent.getArticleContent());
            RestResult.responseJson(response, new RestResult<>(200, "成功！", articleDto));
        }

    }

    @PostMapping
    public void addArticle(@RequestBody ArticleContent articleContent, HttpServletRequest request, HttpServletResponse response) {
        try {
            log.info(articleContent.toString());
            articleContentService.addContent(articleContent);
        } catch (Exception e) {
            log.error(e.getMessage());
            RestResult.responseJson(response, new RestResult<>(500, "插入失败", e.getMessage()));
        }


    }
}
