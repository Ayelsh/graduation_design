package com.ykj.graduation_design.module.Blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ykj.graduation_design.module.Blog.entity.Article;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Ayelsh
 * @Date: 2024/03/06/13:25
 * @Description:
 */
public interface ArticleMapper extends BaseMapper<Article> {

    @Select("SELECT * FROM article WHERE article_title LIKE '${value}' ")
    List<Article> queryArticleTitle(@Param("value") String keyValue);
}
