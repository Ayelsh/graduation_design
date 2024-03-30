package com.ykj.graduation_design.module.Blog.Dto;

import com.ykj.graduation_design.module.Blog.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Ayelsh
 * @Date: 2024/03/20/18:10
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDto {
    private String userName;
    private String articleTitle;
    private String articleThumbnailUrl;
    private String articleContent;

    public ArticleDto(Article article,String articleContent){
        this.articleTitle = article.getArticleTitle();
        this.articleContent = articleContent;
        this.userName = String.valueOf(article.getArticleAuthorId());
        this.articleThumbnailUrl = article.getArticleThumbnailUrl();
    }

}
