package com.ykj.graduation_design.module.Blog.services;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ykj.graduation_design.module.Blog.entity.Article;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Ayelsh
 * @Date: 2024/03/06/13:23
 * @Description:
 */
public interface ArticleService extends IService<Article>{

    Page<Article> titleQuery(String keyValue, Integer pageNumber, Integer pageSize);
}
