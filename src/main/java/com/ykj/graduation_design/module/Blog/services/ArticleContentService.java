package com.ykj.graduation_design.module.Blog.services;

import com.ykj.graduation_design.module.Blog.entity.ArticleContent;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Ayelsh
 * @Date: 2024/01/28/15:57
 * @Description:
 */
public interface ArticleContentService {

    List<ArticleContent> allContentQuery();

    void addContent(ArticleContent articleContent);
}
