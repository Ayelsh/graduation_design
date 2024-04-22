package com.ykj.graduation_design.module.Blog.controller;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ykj.graduation_design.common.RestResult;
import com.ykj.graduation_design.common.utils.UserUtils;
import com.ykj.graduation_design.module.Blog.Dto.ArticleDto;
import com.ykj.graduation_design.module.Blog.entity.Article;
import com.ykj.graduation_design.module.Blog.entity.ArticleContent;
import com.ykj.graduation_design.module.Blog.services.ArticleContentService;
import com.ykj.graduation_design.module.Blog.services.ArticleService;
import com.ykj.graduation_design.module.login.entity.LoginUser;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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

    @GetMapping("/initPagePage")
    public void initPagePage(HttpServletResponse response,Integer pageNumber, Integer pageSize) {

        try {
            Page<Article> page = new Page<>(pageNumber,pageSize);
            page = articleService.page(page);
            RestResult.responseJson(response, new RestResult<>(200, "成功！",page ));
        }catch (Exception e){

            RestResult.responseJson(response, new RestResult<>(600, "失败！", e.getMessage()));
        }
    }


    @GetMapping("/initPostPage/{acticleId}")
    public void initPostPage(HttpServletResponse response, @PathVariable("acticleId") Long acticleId) {
        log.info(String.valueOf(acticleId));
        ArticleContent articleContent = articleContentService.queryByArticleId(acticleId);

        if (Objects.isNull(articleContent)) {
            RestResult.responseJson(response, new RestResult<>(600, "请求数据为空！", null));
        } else {
            ArticleDto articleDto = new ArticleDto(articleService.getById(acticleId), articleContent.getArticleContent());
            log.info(articleDto.getArticleTitle());
            articleDto.setArticleContent(articleContent.getArticleContent());
            RestResult.responseJson(response, new RestResult<>(200, "成功！", articleDto));
        }

    }

    @PostMapping("newArticle")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addArticle(@RequestBody ArticleDto articleDto,HttpServletResponse response) {
        try {
            //标题 内容 封面
            LoginUser loginUser = UserUtils.getCurrentUser();
            Article article = new Article();
            ArticleContent articleContent = new ArticleContent();
            long id = IdUtil.getSnowflakeNextId();
            Date date = new Date();

            article.setArticleAuthorId(loginUser.getId());
            article.setArticleThumbnailUrl(articleDto.getArticleThumbnailUrl());
            article.setId(id);
            article.setCreatedTime(date);
            article.setArticleTitle(articleDto.getArticleTitle());
            articleContent.setArticleContent(articleDto.getArticleContent());
            articleContent.setIdArticle(id);
            articleContent.setCreatedTime(date);

            articleService.save(article);
            articleContentService.addContent(articleContent);
            RestResult.responseJson(response, new RestResult<>(200, "帖子发布成功！", null));
        } catch (Exception e) {
            log.error(e.getMessage());
            RestResult.responseJson(response, new RestResult<>(600, "帖子发布失败", e.getMessage()));
        }


    }
}
