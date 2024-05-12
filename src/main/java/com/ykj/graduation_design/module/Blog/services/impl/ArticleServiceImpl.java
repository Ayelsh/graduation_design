package com.ykj.graduation_design.module.Blog.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykj.graduation_design.module.Blog.entity.Article;
import com.ykj.graduation_design.module.Blog.mapper.ArticleMapper;
import com.ykj.graduation_design.module.Blog.services.ArticleService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Ayelsh
 * @Date: 2024/03/06/13:26
 * @Description:
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Override
    public Page<Article> titleQuery(String keyValue,Integer pageNumber, Integer pageSize) {
        Page<Article> page = new Page<>(pageNumber,pageSize);
        QueryWrapper<Article> wrapper = new QueryWrapper<Article>();
        wrapper.like("article_title",keyValue);
        page =  this.baseMapper.selectPage(page,wrapper);
        return page;
    }
}